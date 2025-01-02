package com.co.judger.judge;

import com.co.common.constants.JudgeConsants;
import com.co.common.constants.LanguageConstants;
import com.co.common.model.JudgeInfo;
import com.co.judger.model.Judge;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-05-26 1:01
 */
@Component
@Slf4j
public class PreparationJudge {
    @Autowired
    private SandBoxRun sandBoxRun;
    public String codeToFile(JudgeInfo judgeInfo) {
        String directoryPath = JudgeConsants.EnvName.SANDBOXPATH.getName() + "/" + judgeInfo.getId();
        Boolean isDirectory = sandBoxRun.createDirectory(directoryPath);
        if (isDirectory == false) {
            return null;
        }
        String fileName;
        if (LanguageConstants.Language.JAVA.getLanguage().equals(judgeInfo.getLanguage())) {
            fileName = LanguageConstants.SpecialRule.JAVA.getName() + LanguageConstants.Language.getExtensionFromLanguage(judgeInfo.getLanguage());
        } else {
            fileName = judgeInfo.getId() + LanguageConstants.Language.getExtensionFromLanguage(judgeInfo.getLanguage());
        }
        String objectName = directoryPath +"/" + fileName;
        try {
            FileWriter fileWriter = new FileWriter(objectName);
            fileWriter.write(judgeInfo.getCode());
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("写入文件codeToFile");
        return directoryPath;
    }
    public Map<String, String> compile(String directoryPath, Long judgeId, String language) {
        Map<String,String> map = new HashMap<>();
        String compileInfo = null;

        compileInfo = sandBoxRun.compile(directoryPath, String.valueOf(judgeId), language);

        log.info("编译结果compileInfo:{}",compileInfo);
        if (compileInfo != null) {
            map.put("errMsg",compileInfo);
            boolean deletedFolder = sandBoxRun.deleteFolder(new File(directoryPath));
            if (deletedFolder == false) {
                log.error("delete folder err");
                return null;
            }
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
