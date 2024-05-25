package com.co.judger.judge;

import com.co.common.constants.JudgeConsants;
import com.co.common.constants.LanguageConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    public Map<String, String> compile(Long judgeId,String code, String language) {
        Map<String,String> map = new HashMap<>();
        String directoryPath = "/codeconnect-sandbox/" + judgeId;
        Boolean isDirectory = sandBoxRun.createDirectory(directoryPath);
        if (isDirectory == false) {
            map.put("errMsg","mkdir err");
            return map;
        }
        String fileName;
        if (LanguageConstants.Language.JAVA.getLanguage().equals(language)) {
            fileName = LanguageConstants.SpecialRule.JAVA.getName() + LanguageConstants.Language.getExtensionFromLanguage(language);
        } else {
            fileName = judgeId + LanguageConstants.Language.getExtensionFromLanguage(language);
        }
        String objectName = directoryPath +"/" + fileName;
        try {
            FileWriter fileWriter = new FileWriter(objectName);
            fileWriter.write(code);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String compileInfo = null;

        compileInfo = sandBoxRun.compile(directoryPath, String.valueOf(judgeId), language);

        if (compileInfo != null) {
            map.put("errMsg",compileInfo);
        } else {
            if (LanguageConstants.Language.JAVA.getLanguage().equals(language)){
                map.put("success",directoryPath + "/" + LanguageConstants.SpecialRule.JAVA.getCompiledName());
            } else {
                map.put("success",directoryPath + "/" + judgeId);
            }
        }
        return map;
    }
}
