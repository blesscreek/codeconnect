package com.co.backend.controller;

import com.co.backend.dao.user.UserEntityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-03-27 19:32
 */
@Slf4j
@RestController
public class TestController {
    @Autowired
    private UserEntityService userEntityService;
    @RequestMapping("/hello")
//    @PreAuthorize("@ex.hasAuthority('sys:question:add')")
    public String hello(){

        System.out.println("hello");
        return "hello";
    }

    @RequestMapping("/test")
//    @PreAuthorize("@ex.hasAuthority('sys:question:add')")
    public String test(){

        try {
            // 设置工作目录
            String workingDirectory = "/codeconnect-sandbox";

            // 构建命令序列
            ProcessBuilder builder = new ProcessBuilder();
            builder.directory(new File(workingDirectory)); // 设置工作目录
            builder.command("gcc", "a.c", "-o", "a"); // 编译C程序
            Process compileProcess = builder.start();
            compileProcess.waitFor(); // 等待编译完成

            // 运行C程序
            builder.command("./runner", "-i", "./1.in", "-o", "./1.out", "-u", "tmp.out", "--", "./a");
            Process runProcess = builder.start();

            // 读取C程序的输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 等待C程序执行结束
            int exitCode = runProcess.waitFor();
            System.out.println("exitCode:" + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return "ok";
    }


}
