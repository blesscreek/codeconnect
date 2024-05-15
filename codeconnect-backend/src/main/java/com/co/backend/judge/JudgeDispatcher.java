package com.co.backend.judge;

import com.co.backend.model.po.Judge;
import com.co.common.config.RabbitmqConfig;
import com.co.backend.constant.JudgeConsants;
import com.co.backend.dao.judge.JudgeEntityService;
import com.co.common.utils.RabbitMQUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-05-09 22:36
 */

@Component
@Slf4j
public class JudgeDispatcher {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RabbitMQUtil rabbitMQUtil;
    @Autowired
    private JudgeEntityService judgeEntityService;
    public void sendTask(Judge judge) {
        try {

            byte[] bytesFromObject = rabbitMQUtil.getBytesFromObject(judge);
            if (judge.getCid() == 0) {
                rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_TOPIC_JUDGE, RabbitmqConfig.ROUTINGKEY_JUDGE_COMMON, bytesFromObject);
            }
        } catch (Exception e) {
            judge.setStatus(JudgeConsants.Judge.STATUS_SUBMITTED_FAILED.getStatus())
                            .setErrorMessage("Call MQ to push task error. Please try to submit again!");
            judgeEntityService.updateById(judge);
            log.error("调用mq将判题纳入判题等待队列异常--------------->{}", e.getMessage());
        }
    }

}
