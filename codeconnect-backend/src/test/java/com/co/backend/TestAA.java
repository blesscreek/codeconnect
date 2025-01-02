package com.co.backend;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;


/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-04-10 23:09
 */
@SpringBootTest
public class TestAA {
    @Autowired
    private NacosDiscoveryProperties discoveryProperties;
    @Test
    public void test() {
        NamingService namingService = discoveryProperties.namingServiceInstance();
        try {
            // 获取该微服务的所有健康实例
            List<Instance> instances =
                    namingService.selectInstances("codeconnectjudger", true);
            List<String> keyList = new ArrayList<>();
            // 获取当前健康实例取出ip和port拼接
            for (Instance instance : instances) {
                System.out.println(instance.getIp() + ":" + instance.getPort());
            }

        } catch (NacosException e) {
            System.out.println(11);

        }
    }
}
