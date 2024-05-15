package com.co.backend;

import com.co.common.config.RabbitmqConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-05-07 17:48
 */
@SpringBootTest
public class ReceiveHandler{
    @Test
    @RabbitListener(queues = {RabbitmqConfig.QUEUE_JUDGE_COMMON})
    public void receiveCommon(String msg) throws InterruptedException{
        System.out.println("Queue_COMMON msg" + msg);
    }
    @RabbitListener(queues = {RabbitmqConfig.QUEUE_JUDGE_CONTEST})
    public void receiveSpecial(String msg) {
        System.out.println("Queue_SPECIAL msg" + msg);
    }
}
