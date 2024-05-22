package com.co.judger.judge;

import com.co.common.constants.LanguageConstants;
import com.co.common.model.JudgeInfo;
import com.co.judger.model.Question;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Path;

/**
 * @Author co
 * @Version 1.0
 * @Description 内部编译运行的命令交互
 * @Date 2024-05-16 22:35
 */
@Component
@Slf4j
public class SandBoxRun {
    public String compile(String filepath, String judgeId, String language) {
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
                    builder.command("javac",LanguageConstants.SpecialRule.JAVA.getName() + LanguageConstants.Language.getExtensionFromLanguage(language));
                    break;
            }
            builder.redirectErrorStream(true); // 将错误流合并到标准输出流
            Process compileProcess = builder.start();

            // 读取编译输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(compileProcess.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            int exitCode = compileProcess.waitFor(); // 等待编译完成

            if (exitCode != 0) {
                return output.toString();
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void judge(Question question, JudgeInfo judgeInfo, String inPath, String outPath, String compiledPath) {


    }
}
