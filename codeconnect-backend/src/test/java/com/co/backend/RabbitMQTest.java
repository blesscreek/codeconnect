package com.co.backend;

import com.co.backend.model.po.Question;
import com.co.common.config.RabbitmqConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-05-07 17:42
 */
@SpringBootTest
public class RabbitMQTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Test
    public void producer () throws Exception {
        Question question = new Question();
        question.setTitle("aaa");
        byte[] bytesFromObject = getBytesFromObject(question);
        System.out.println(bytesFromObject);
        String message = "hello world";
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_TOPIC_JUDGE,"judge.common",bytesFromObject);
    }

    //对象转化为字节码
    public  byte[] getBytesFromObject(Serializable obj) throws Exception {
        if (obj == null) {
            return null;
        }
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(obj);
        return bo.toByteArray();
    }

}
