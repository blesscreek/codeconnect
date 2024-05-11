package com.co.codeconnectjudge.judge;

import com.co.codeconnectjudge.config.RabbitmqConfig;
import com.co.codeconnectjudge.model.po.Judge;
import com.co.codeconnectjudge.utils.RabbitMQUtil;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-05-07 20:05
 */


@Component
public class MQJudgeReceiver {
    @Autowired
    private RabbitMQUtil rabbitMQUtil;

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