package com.co.judger.messager;

import com.co.common.model.JudgeInfo;
import com.co.common.utils.RabbitMQUtil;
import com.co.judger.dao.JudgeEntityService;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-05-27 9:05
 */
@Component
@Slf4j
public class MQJudgeSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String exchange="base-oj";
    private static final String ROUTING_KEY="judgeRes";
    public void sendTask(JudgeInfo judgeInfo) {
        try {
            rabbitTemplate.convertAndSend(exchange, ROUTING_KEY, judgeInfo);

        } catch (Exception e) {
            log.error("调用mq判题结果放入等待队列异常--------------->{}", e.getMessage());
        }
    }
}
