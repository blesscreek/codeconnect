package com.co.judger.messager;

import com.co.common.config.RabbitmqConfig;
import com.co.common.model.JudgeInfo;
import com.co.common.utils.RabbitMQUtil;
import com.co.judger.service.JudgeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-05-15 16:08
 */
@Component
@Slf4j
public class MQJudgeReceiver {
    @Autowired
    private RabbitMQUtil rabbitMQUtil;
    @Autowired
    private JudgeService judgeService;

    @RabbitListener(queues = {RabbitmqConfig.QUEUE_JUDGE_COMMON})
    public void receiveCommon(byte[] bytes) throws Exception {
        JudgeInfo judgeInfo = (JudgeInfo) rabbitMQUtil.getObjectFromBytes(bytes);
        log.info("Queue_JUDGE_COMMON receive judge" + judgeInfo.getId());
        judgeService.judgeProcess(judgeInfo);
    }
    @RabbitListener(queues = {RabbitmqConfig.QUEUE_JUDGE_CONTEST})
    public void receiveContest(byte[] bytes) throws Exception {
        System.out.println(bytes);
//        Judge q = (Judge) rabbitMQUtil.getObjectFromBytes(bytes);
//        System.out.println("Queue_COMMON msg : " + q.toString());
    }


}
