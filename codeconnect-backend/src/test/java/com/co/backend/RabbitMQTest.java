package com.co.backend;

import com.co.backend.model.po.Question;
import com.co.backend.service.sse.SseService;
import com.co.common.config.RabbitmqConfig;
import com.co.common.model.JudgeInfo;
import com.co.common.utils.RabbitMQUtil;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
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
    private RabbitMQUtil rabbitMQUtil;
    @Autowired
    private SseService sseService;

    @RabbitListener(queues = {RabbitmqConfig.QUEUE_JUDGE_COMMON})
    public void receiveCommon(byte[] bytes) throws Exception {
        JudgeInfo judgeInfo = (JudgeInfo) rabbitMQUtil.getObjectFromBytes(bytes);
//        sseService.sendJudgeRes(judgeInfo);
    }

}
