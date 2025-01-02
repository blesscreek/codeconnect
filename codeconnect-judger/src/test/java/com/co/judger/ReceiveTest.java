package com.co.judger;

import com.co.common.utils.RabbitMQUtil;
import com.co.judger.model.Judge;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-05-18 17:19
 */
@SpringBootTest
public class ReceiveTest {
    @Autowired
    private RabbitMQUtil rabbitMQUtil;
}
