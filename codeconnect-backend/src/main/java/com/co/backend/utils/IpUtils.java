package com.co.backend.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author: Himit_ZH
 * @Date: 2020/10/30 11:12
 * @Description:
 */
@Slf4j
public class IpUtils {

    public static String getUserIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    try {
                        ipAddress = InetAddress.getLocalHost().getHostAddress();
                    } catch (UnknownHostException e) {
                        log.error("用户ip获取异常------->{}", e.getMessage());
                    }
                }
            }
            // 通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null) {
                if (ipAddress.contains(",")) {
                    return ipAddress.split(",")[0];
                } else {
                    return ipAddress;
                }
            } else {
                return "";
            }
        } catch (Exception e) {
            log.error("用户ip获取异常------->{}", e.getMessage());
            return "";
        }
    }

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