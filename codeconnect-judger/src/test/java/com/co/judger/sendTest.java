package com.co.judger;

import com.co.common.config.RabbitmqConfig;
import com.co.common.utils.RabbitMQUtil;
import com.co.judger.model.Judge;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-05-18 17:29
 */
@SpringBootTest
public class sendTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RabbitMQUtil rabbitMQUtil;
    @Test
    public void sendTest() throws Exception {
        Judge judge = new Judge();
        judge.setCode("aaaa");
        byte[] bytesFromObject = rabbitMQUtil.getBytesFromObject(judge);
        System.out.println(bytesFromObject);
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_TOPIC_JUDGE,"judge.common",bytesFromObject);

    }
}
