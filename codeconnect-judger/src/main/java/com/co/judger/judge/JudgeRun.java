package com.co.judger.judge;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.co.common.constants.JudgeConsants;
import com.co.common.model.JudgeInfo;
import com.co.common.model.JudgeServer;
import com.co.common.utils.MinioUtil;
import com.co.judger.dao.JudgeServerEntityService;
import com.co.judger.dao.QuestionCaseEntityService;
import com.co.judger.model.Question;
import com.co.judger.model.QuestionCase;
import com.co.judger.utils.IpUtils;
import com.co.judger.utils.ThreadPoolUtils;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-05-20 21:43
 */
@Component
@Slf4j
public class JudgeRun {
    @Autowired
    private QuestionCaseEntityService questionCaseEntityService;
    @Autowired
    private MinioUtil minioUtil;
    @Autowired
    private SandBoxRun sandBoxRun;
    @Value("${minio.bucketName2}")
    private String  bucket_questioncases;
    @Autowired
    private JudgeServerEntityService judgeServerEntityService;
    private String ip;
    @Value("${server.port}")
    private Integer port;


    public List<JSONObject> judgeAllCase(Question question, JudgeInfo judgeInfo, String directoryPath, String compiledPath) throws ExecutionException, InterruptedException {
        Long testTime = null;
        //设置判题时间、空间，默认给题目限制时间+200ms测评
        if (question.getTimeLimit() != null) {
            testTime = (long) (question.getTimeLimit() + 200);
        } else if (question.getTimeLimit() == null) {
            testTime = JudgeConsants.LimitNum.TIME_LIMIT.getNum();
        }
        if (question.getMemoryLimit() == null) {
            question.setMemoryLimit (JudgeConsants.LimitNum.MEMORY_LIMIT.getNum());
        }
        question.setTimeLimit(testTime);


        //获取题目所有测试用例
        QueryWrapper<QuestionCase> questionCaseQueryWrapper = new QueryWrapper<>();
        questionCaseQueryWrapper.eq("qid", question.getId())
                .eq("is_show", 0);
        ArrayList<QuestionCase> questionCases = (ArrayList<QuestionCase>) questionCaseEntityService.list(questionCaseQueryWrapper);
        if (questionCases.size() <= 0) {
            log.error("no questionCase");
            return null;
        }
        //异步判题
        ArrayList<FutureTask<JSONObject>> futureTasks = new ArrayList<>();
        int score = 100 / questionCases.size();

        for (int i = 0; i < questionCases.size(); i++) {
            log.info("questionCase " + i);
            Long caseId = questionCases.get(i).getId();
            //从minio下载样例，并上传到系统对应文件夹路径
            String inputFileUrl = questionCases.get(i).getInput();
            String outputFileUrl = questionCases.get(i).getOutput();
            File inputFile = minioUtil.downloadFile(bucket_questioncases, inputFileUrl.split(bucket_questioncases + "/")[1], ".in");
            File outputFile = minioUtil.downloadFile(bucket_questioncases, outputFileUrl.split(bucket_questioncases + "/")[1], ".out");
            String inFileName = judgeInfo.getId() + "judge" + i + ".in";
            String outFileName = judgeInfo.getId() + "judge" + i + ".out";
            try {
                File inFile = new File(directoryPath, inFileName);
                File outFile = new File(directoryPath, outFileName);
                Files.copy(inputFile.toPath(),inFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(outputFile.toPath(),outFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //将判题任务加入任务列表
            int finalI = i;
            futureTasks.add(new FutureTask<>(() -> {
                JSONObject res = sandBoxRun.judge(question, judgeInfo,
                        directoryPath + "/" + inFileName, directoryPath + "/" + outFileName, compiledPath, caseId, directoryPath);
                res.set("judgeId", judgeInfo.getId());
                res.set("caseId",caseId);
                res.set("time", LocalDateTime.now());
                if ((int)res.get("status") <= 1) {
                    res.set("score", score);
                } else {
                    res.set("score", 0);
                }
                releaseJudgeServer();
                return res;
            }));
        }
        return submitBatchTask2ThreadPool(futureTasks, directoryPath);

    }
    /**
     * 释放评测机资源
     *
     * @param
     */
    public void releaseJudgeServer() {
        ip = IpUtils.getServiceIp();
        String url = ip + ":" + port;
        UpdateWrapper<JudgeServer> judgeServerUpdateWrapper = new UpdateWrapper<>();
        judgeServerUpdateWrapper.setSql("task_number = task_number-1")
                .eq("url", url);
        boolean isOk = judgeServerEntityService.update(judgeServerUpdateWrapper);
        if (!isOk) { // 重试八次
            tryAgainUpdateJudge(judgeServerUpdateWrapper);
        }
    }
    public void tryAgainUpdateJudge(UpdateWrapper<JudgeServer> updateWrapper) {
        boolean retryable;
        int attemptNumber = 0;
        do {
            boolean success = judgeServerEntityService.update(updateWrapper);
            if (success) {
                return;
            } else {
                attemptNumber++;
                retryable = attemptNumber < 8;
                if (attemptNumber == 8) {
                    break;
                }
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } while (retryable);
    }
    private List<JSONObject> submitBatchTask2ThreadPool(ArrayList<FutureTask<JSONObject>> futureTasks, String directoryPath) throws ExecutionException, InterruptedException {
        //提交到线程池进行执行
        for (FutureTask<JSONObject> futureTask : futureTasks) {
            ThreadPoolUtils.getInstance().getThreadPool().submit(futureTask);
        }
        List<JSONObject> result = new LinkedList<>();
        while (futureTasks.size() > 0) {
            Iterator<FutureTask<JSONObject>> iterator = futureTasks.iterator();
            while (iterator.hasNext()){
                FutureTask<JSONObject> future = iterator.next();
                if (future.isDone() && !future.isCancelled()) {
                    //获取线程返回结果
                    JSONObject tmp = future.get();
                    result.add(tmp);
                    //任务完成移除任务
                    iterator.remove();
                } else {
                    Thread.sleep(10);//避免CPU高速运转
                }
            }
        }
        boolean deletedFolder = sandBoxRun.deleteFolder(new File(directoryPath));
        if (deletedFolder == false) {
            log.error("delete folder err");
            return null;
        }
        return result;
    }

}
