package com.co.judger.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.co.judger.utils.IpUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;


@Configuration
public class NacosConfig {

    private static final int cpuNum = Runtime.getRuntime().availableProcessors();



    @Value("${server.port}")
    private Integer port;



    /**
     * 用于改变程序自动获取的本机ip
     */
    @Bean
    @Primary
    public NacosDiscoveryProperties nacosProperties() {
        NacosDiscoveryProperties nacosDiscoveryProperties = new NacosDiscoveryProperties();
        //此处我只改了ip，其他参数可以根据自己的需求改变
        nacosDiscoveryProperties.setIp(IpUtils.getServiceIp());
        HashMap<String, String> meta = new HashMap<>();
        int max = cpuNum * 2 + 1;
        meta.put("maxTaskNum", String.valueOf(max));
        nacosDiscoveryProperties.setMetadata(meta);
        nacosDiscoveryProperties.setPort(port);

        nacosDiscoveryProperties.setService("judge-server");
        return nacosDiscoveryProperties;
    }

}