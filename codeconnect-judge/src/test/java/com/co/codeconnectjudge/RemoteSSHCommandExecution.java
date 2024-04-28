package com.co.codeconnectjudge;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-04-18 15:51
 */
import com.jcraft.jsch.*;

public class RemoteSSHCommandExecution {

    public static void main(String[] args) {
        String host = "123.60.15.140";
        String user = "root"; // 替换为您的用户名
        String password = "Code1234"; // 替换为您的密码

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, 22);

            // 设置密码
            session.setPassword(password);

            // 通过配置忽略主机密钥检查
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            // 连接
            session.connect();

            // 创建Channel
            Channel channel = session.openChannel("exec");

            // 在指定目录中执行命令
//            ((ChannelExec) channel).setCommand("mkdir aa");
            ((ChannelExec) channel).setCommand("cd /;  cd /code-runnner/code-runner; ./runner -l echo.log -u echo.out echo 123");
            // 获取输入流
            channel.setInputStream(null);

            // 获取输出流
            ((ChannelExec) channel).setErrStream(System.err);

            // 连接Channel
            channel.connect();

            // 等待直到Channel执行完成
            while (!channel.isEOF()) {
                Thread.sleep(1000);
            }

            // 断开连接
            channel.disconnect();
            session.disconnect();
        } catch (JSchException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}