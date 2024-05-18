package com.co.judger.judge;

import com.co.common.constants.LanguageConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;

/**
 * @Author co
 * @Version 1.0
 * @Description 编译处理逻辑 不负责内部代码命令的交互
 * @Date 2024-05-16 21:53
 */
@Component
public class Compiler {
    @Autowired
    private SandBoxRun sandBoxRun;
    public String compile(Long judgeId,String code, String language) {
        String filePath = "/codeconnect-sandbox/files" ;
        String fileName = judgeId + LanguageConstants.Language.getExtensionFromLanguage(language);
        String objectName = filePath +"/" + fileName;
        try {
            FileWriter fileWriter = new FileWriter(objectName);
            fileWriter.write(code);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String filepath = "/codeconnect-sandbox/files";
        String filename = judgeId + ".cpp";
        sandBoxRun.compile(filepath, String.valueOf(judgeId),language);
        return null;
    }
}
