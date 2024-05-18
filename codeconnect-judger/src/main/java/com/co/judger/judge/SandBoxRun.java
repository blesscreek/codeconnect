package com.co.judger.judge;

import com.co.common.constants.LanguageConstants;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * @Author co
 * @Version 1.0
 * @Description 内部编译运行的命令交互
 * @Date 2024-05-16 22:35
 */
@Component
public class SandBoxRun {
    public void compile(String filepath, String judgeId, String language) {
        // 构建命令序列
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.directory(new File(filepath)); // 设置工作目录
            switch (language) {
                case "C":
                    builder.command("gcc",judgeId + LanguageConstants.Language.getExtensionFromLanguage(language),
                            "-o", judgeId);
                    break;
                case "C++":
                    builder.command("g++",judgeId + LanguageConstants.Language.getExtensionFromLanguage(language),
                            "-o", judgeId);
                    break;
                case "Java":
                    builder.command("javac",judgeId + LanguageConstants.Language.getExtensionFromLanguage(language));
                    break;
            }
            Process compileProcess = builder.start();
            compileProcess.waitFor(); // 等待编译完成
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
