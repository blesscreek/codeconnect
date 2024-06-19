package com.co.backend.controller;

import com.co.backend.dao.user.UserEntityService;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.*;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-03-27 19:32
 */
@Slf4j
@RestController
@Api(tags = "测试接口")
public class TestController {
    @Autowired
    private UserEntityService userEntityService;
    @PostMapping("/hello")
//    @PreAuthorize("@ex.hasAuthority('sys:question:add')")
    public String hello(){

        System.out.println("hello");
        return "hello";
    }

    @PostMapping("/test")
//    @PreAuthorize("@ex.hasAuthority('sys:question:add')")
    public String test(){

//        try {
//            // 设置工作目录
//            String workingDirectory = "/codeconnect-sandbox";
//
//            // 构建命令序列
//            ProcessBuilder builder = new ProcessBuilder();
//            builder.directory(new File(workingDirectory)); // 设置工作目录
//            builder.command("gcc", "a.c", "-o", "a"); // 编译C程序
//            Process compileProcess = builder.start();
//            compileProcess.waitFor(); // 等待编译完成
//
//            // 运行C程序
//            builder.command("./runner", "-i", "./1.in", "-o", "./1.out", "-u", "tmp.out", "--", "./a");
//            Process runProcess = builder.start();
//
//            // 读取C程序的输出
//            BufferedReader reader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
//
//            // 等待C程序执行结束
//            int exitCode = runProcess.waitFor();
//            System.out.println("exitCode:" + exitCode);
//
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }


        String host = "123.60.15.140";
        int port = 22;
        String username = "root";
        String password = "Code1234";
        String[] commands = {
                "cd /codeconnect-sandbox",
                "gcc a.c -o a",
                "./runner -i ./1.in -o ./1.out -u tmp.out -- ./main"
        };

        try {
            JSch jsch = new JSch();

            // 建立SSH会话
            Session session = jsch.getSession(username, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            for (String command : commands) {
                // 打开执行命令的通道
                ChannelExec channel = (ChannelExec) session.openChannel("exec");
                channel.setCommand(command);

                // 获取命令执行的输出流
                InputStream in = channel.getInputStream();
                channel.connect();

                // 读取输出流的内容
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }

                // 关闭通道
                channel.disconnect();
            }

            // 关闭会话
            session.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }





}
