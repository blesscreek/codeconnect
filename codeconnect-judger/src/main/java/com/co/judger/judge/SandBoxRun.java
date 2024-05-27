package com.co.judger.judge;

import cn.hutool.json.JSONObject;
import com.co.common.constants.JudgeConsants;
import com.co.common.constants.LanguageConstants;
import com.co.common.model.JudgeInfo;
import com.co.judger.model.Question;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;

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
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.directory(new File(filepath)); // 设置工作目录
            String fileName = null;
            switch (language) {
                case "C": {
                    fileName = judgeId + LanguageConstants.Language.getExtensionFromLanguage(language);
                    builder.command("gcc", fileName, "-o", judgeId);
                    break;
                }
                case "Cpp": {
                    fileName = judgeId + LanguageConstants.Language.getExtensionFromLanguage(language);
                    builder.command("g++", fileName, "-o", judgeId);
                    break;
                }
                case "Java": {
                    fileName = LanguageConstants.SpecialRule.JAVA.getName() + LanguageConstants.Language.getExtensionFromLanguage(language);
                    builder.command("javac", fileName);
                    break;
                }
            }
            builder.redirectErrorStream(true); // 将错误流合并到标准输出流
            Process compileProcess = builder.start();

            // 读取编译输出
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(compileProcess.getInputStream()))) {
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
                int exitCode = compileProcess.waitFor(); // 等待编译完成
                if (exitCode != 0) {
                    log.error("compile err");
                    return output.toString();
                } else {
                    return null;
                }
            } finally {
                compileProcess.destroy(); // 确保进程被关闭
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public JSONObject judge(Question question, JudgeInfo judgeInfo, String inPath, String outPath, String compiledPath, Long caseId, String directoryPath) {
        try {
            ProcessBuilder builder = new ProcessBuilder();
            String sandboxPath = JudgeConsants.EnvName.SANDBOXPATH.getName();
            String inPartPath = "." + inPath.split(sandboxPath)[1];
            String outPartPath = "." + outPath.split(sandboxPath)[1];
            String compiledPartPath = null;
            if (compiledPath != null) {
               compiledPartPath = "." + compiledPath.split(sandboxPath)[1];
            }
            String noCompiledPath = "./"+judgeInfo.getId() + "/" +  judgeInfo.getId() + LanguageConstants.Language.getExtensionFromLanguage(judgeInfo.getLanguage());
            String tmpPartPath = "./"+judgeInfo.getId() + "/" +  caseId + JudgeConsants.EnvName.OUTPUTPATH.getName();
            builder.directory(new File(sandboxPath));
            System.out.println("inPartPath : " + inPartPath);
            System.out.println("outPartPath : " + outPartPath);
            System.out.println("noCompiledPath : " + noCompiledPath);
            System.out.println("tmpPartPath : " + tmpPartPath);
            switch (judgeInfo.getLanguage()) {
                case "C":
                case "Cpp": {
                    System.out.println("./runner " + " -t " + question.getTimeLimit().toString() + " -m " + question.getMemoryLimit().toString() +
                            " --mco " + " -i " + inPartPath + " -o " + outPartPath + " -u " + tmpPartPath + " -- " + compiledPartPath);
                    builder.command("./runner", "-t", question.getTimeLimit().toString(), "-m", question.getMemoryLimit().toString(), "--mco",
                            "-i", inPartPath, "-o", outPartPath, "-u", tmpPartPath, "--", compiledPartPath);
                    break;
                }
                case "Java": {
                    builder.command("./runner", "-t", question.getTimeLimit().toString(), "-m", question.getMemoryLimit().toString(), "--mco",
                            "-i", inPartPath, "-o", outPartPath, "-u", tmpPartPath, "-- java Main");
                    break;
                }
                case "Python": {
                    System.out.println("cmmand");
                    System.out.println("./runner " + " -t " + question.getTimeLimit().toString() + " -m " + question.getMemoryLimit().toString() +
                            " --mco " + " -i " + inPartPath + " -o " + outPartPath + " -u " + tmpPartPath + " -- python3 " + noCompiledPath);
                    builder.command("./runner", "-t", question.getTimeLimit().toString(), "-m", question.getMemoryLimit().toString(), "--mco",
                            "-i", inPartPath, "-o", outPartPath, "-u", tmpPartPath, "-- python3" + noCompiledPath);
                    break;
                }
                case "JavaScript": {
                    System.out.println("./runner " + " -t " + question.getTimeLimit().toString() + " -m " + question.getMemoryLimit().toString() +
                        " --mco " + " -i " + inPartPath + " -o " + outPartPath + " -u " + tmpPartPath + " -- node " + noCompiledPath);
                    builder.command("./runner", "-t", question.getTimeLimit().toString(), "-m", question.getMemoryLimit().toString(), "--mco",
                            "-i", inPartPath, "-o", outPartPath, "-u", tmpPartPath, "-- node" + noCompiledPath);
                    break;
                }
            }
            builder.redirectErrorStream(true); // 将错误流合并到标准输出流
            Process process = builder.start();

            // 读取编译输出和错误信息
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
                // 等待编译完成并获取退出码
                int exitCode = process.waitFor();
                System.out.println("exitCode : " + exitCode);
                if (exitCode == 0) {
                    JSONObject jsonObject = new JSONObject(output.toString());
                    System.out.println(jsonObject.toString());
                    return jsonObject;
                } else {
                    log.error("judge exitCode err");
                    return null;
                }
            } finally {
                process.destroy();
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public Boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            boolean delete = file.delete();
            if (delete) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }
    public boolean deleteFolder(File folder) {
        if (folder == null || !folder.exists()) {
            return false;
        }

        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteFolder(file);
                }
            }
        }

        return folder.delete();
    }

    public Boolean createDirectory(String filePath) {
        File directory = new File(filePath);
        boolean mkdir = directory.mkdir();
        if (mkdir) {
            return true;
        } else {
            return false;
        }
    }
}
