package com.co.judger.judge;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.co.common.constants.JudgeConsants;
import com.co.common.model.JudgeInfo;
import com.co.common.utils.MinioUtil;
import com.co.judger.dao.QuestionCaseEntityService;
import com.co.judger.model.Question;
import com.co.judger.model.QuestionCase;
import com.co.judger.utils.ThreadPoolUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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

    public List<JSONObject> judgeAllCase(Question question, JudgeInfo judgeInfo, String compiledPath) throws ExecutionException, InterruptedException {
        Long testTime = null, testMemory = null;
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

        ArrayList<FutureTask<JSONObject>> futureTasks = new ArrayList<>();

        QueryWrapper<QuestionCase> questionCaseQueryWrapper = new QueryWrapper<>();
        questionCaseQueryWrapper.eq("qid", question.getId())
                .eq("is_show", 0);
        ArrayList<QuestionCase> questionCases = (ArrayList<QuestionCase>) questionCaseEntityService.list(questionCaseQueryWrapper);
        if (questionCases.size() <= 0) {
            log.error("no questionCase");
            return null;
        }

        String directoryPath = "/codeconnect-sandbox/" + judgeInfo.getId();
        int score = 100 / questionCases.size();
        for (int i = 0; i < questionCases.size(); i++) {
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

            int finalI = i;
            futureTasks.add(new FutureTask<>(() -> {
                JSONObject res = sandBoxRun.judge(question, judgeInfo,
                        directoryPath + "/" + inFileName, directoryPath + "/" + outFileName, compiledPath, caseId, directoryPath);
                res.set("judgeId", judgeInfo.getId());
                res.set("caseId",caseId);
                if ((int)res.get("status") <= 1) {
                    res.set("score", score);
                } else {
                    res.set("score", 0);
                }
                return res;
            }));
        }
        return submitBatchTask2ThreadPool(futureTasks, directoryPath);

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
//        boolean deletedFolder = sandBoxRun.deleteFolder(new File(directoryPath));
//        if (deletedFolder == false) {
//            log.error("delete folder err");
//            return null;
//        }
        return result;
    }

}
