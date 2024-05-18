package com.co.judger;

import com.co.common.config.RabbitmqConfig;
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
    @Test
    @RabbitListener(queues = {RabbitmqConfig.QUEUE_JUDGE_COMMON})
    public void receiveCommon(byte[] bytes) throws Exception {
        Judge q = (Judge) rabbitMQUtil.getObjectFromBytes(bytes);
        System.out.println("Queue_COMMON msg : " + q.toString());
    }
    @RabbitListener(queues = {RabbitmqConfig.QUEUE_JUDGE_CONTEST})
    public void receiveContest(byte[] bytes) throws Exception {
        System.out.println(bytes);
        Judge q = (Judge) rabbitMQUtil.getObjectFromBytes(bytes);
        System.out.println("Queue_COMMON msg : " + q.toString());
    }
}
