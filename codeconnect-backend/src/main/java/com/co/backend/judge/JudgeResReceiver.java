package com.co.backend.judge;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.co.backend.dao.question.QuestionEntityService;
import com.co.backend.model.po.Question;
import com.co.backend.service.sse.SseService;
import com.co.backend.utils.IpUtils;
import com.co.common.model.JudgeInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author bless
 * @Version 1.0
 * @Description
 * @Date 2024-10-13 21:46
 */
@Component
@Data
@Slf4j
public class JudgeResReceiver {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    MessageConverter messageConverter;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    QuestionEntityService questionEntityService;

    @Autowired
    SseService sseService;

    @Value("${server.port}")
    private Integer port;

    public String queueName = "judgeRes";

    public String exchangeName = "base-oj";

    public String key = "judgeRes";

    public void init() {
        Queue queue = new Queue(queueName , true);
        DirectExchange exchange = new DirectExchange(exchangeName);
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(key);
        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareExchange(exchange);
        amqpAdmin.declareBinding(binding);

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(rabbitTemplate.getConnectionFactory());
        container.setQueueNames(queueName );
        container.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    byte[] body = message.getBody();
                    ObjectMapper mapper = new ObjectMapper();
                    JudgeInfo judgeInfo = mapper.readValue(body, JudgeInfo.class);
                    UpdateWrapper<Question> questionUpdateWrapper = new UpdateWrapper<>();
                    questionUpdateWrapper.eq("id",judgeInfo.getQid()).setSql("submit_num = submit_num + 1");

                    if (judgeInfo.getScore() != null && judgeInfo.getScore() == 100) {
                        questionUpdateWrapper.setSql("accept_num = accept_num + 1");
                    }
                    questionEntityService.update(questionUpdateWrapper);
                    log.info("QUEUE_JUDGE_COMMON_RES receive judge res : "  + judgeInfo.toString());
                    sseService.ssePushMsg(judgeInfo);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });
        container.start();
    }


}
