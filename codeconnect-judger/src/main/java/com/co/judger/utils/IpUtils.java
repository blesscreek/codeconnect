package com.co.judger.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class IpUtils {

    public static String getServiceIp() {
        String publicIP = null;
        try {
            // 使用命令行工具获取公网 IP
            Process process = Runtime.getRuntime().exec("curl -s ifconfig.me");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            publicIP = reader.readLine();
            reader.close();
        } catch (Exception e) {
            System.err.println("获取公网 IP 地址失败: " + e.getMessage());
        }
        return publicIP;
    }
}
