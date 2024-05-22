package com.co.judger.judge;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.co.common.model.JudgeInfo;
import com.co.common.utils.MinioUtil;
import com.co.judger.dao.QuestionCaseEntityService;
import com.co.judger.model.Question;
import com.co.judger.model.QuestionCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.concurrent.FutureTask;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-05-20 21:43
 */
@Component
public class JudgeRun {
    @Autowired
    private QuestionCaseEntityService questionCaseEntityService;
    @Autowired
    private MinioUtil minioUtil;
    @Autowired
    private SandBoxRun sandBoxRun;
    @Value("${minio.bucketName2}")
    private String  bucket_questioncases;

    public void judgeAllCase(Question question, JudgeInfo judgeInfo, String compiledPath) {
        //默认给题目限制时间+200ms测评
        Long testTime = (long) (question.getTimeLimit() + 200);
        ArrayList<FutureTask<JSONObject>> futureTasks = new ArrayList<>();
        QueryWrapper<QuestionCase> questionCaseQueryWrapper = new QueryWrapper<>();
        questionCaseQueryWrapper.eq("qid", question.getId())
                .eq("is_show", 0);
        ArrayList<QuestionCase> questionCases = (ArrayList<QuestionCase>) questionCaseEntityService.list(questionCaseQueryWrapper);
        //文件名，输出内容的文件名，时间限制，内存限制， 输入文件，日志文件 主程序文件
        //输出提示信息
        for (int i = 1; i <= questionCases.size(); i++) {
            String inputFileUrl = questionCases.get(i).getInput();
            String outputFileUrl = questionCases.get(i).getOutput();
            File inputFile = minioUtil.downloadFile(bucket_questioncases, inputFileUrl.split(bucket_questioncases + "/")[1], ".in");
            File outputFile = minioUtil.downloadFile(bucket_questioncases, outputFileUrl.split(bucket_questioncases + "/")[1], ".out");
            String directoryPath = "/codeconnect-sandbox/files";
            String inFileName = i + ".in";
            String outFileName = i + ".out";
            try {
                File inFile = new File(directoryPath, inFileName);
                File outFile = new File(directoryPath, outFileName);
                Files.copy(inputFile.toPath(),inFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(outputFile.toPath(),outFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            futureTasks.add(new FutureTask<>(() -> {
//                sandBoxRun.judge(question, judgeInfo,
//                        directoryPath + "/" + inFileName, directoryPath + "/" + outFileName ,compiledPath);
//            }))
        }

    }
}
