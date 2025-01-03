package com.co.judger.messager;

import com.co.common.model.JudgeInfo;
import com.co.judger.service.JudgeService;
import com.co.judger.utils.IpUtils;
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
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-05-15 16:08
 */
@Component
@Data
@Slf4j
public class MQJudgeReceiver {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    MessageConverter messageConverter;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    private JudgeService judgeService;

    @Value("${server.port}")
    private Integer port;

    public String queueName = "judge";

    public String exchangeName = "base-oj";

    public String key = "judge";

    public void init() {
        Queue queue = new Queue(queueName + IpUtils.getServiceIp() + ":" + port, true);
        DirectExchange exchange = new DirectExchange(exchangeName);
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(key + IpUtils.getServiceIp() + ":" + port);
        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareExchange(exchange);
        amqpAdmin.declareBinding(binding);

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(rabbitTemplate.getConnectionFactory());
        container.setQueueNames(queueName + IpUtils.getServiceIp() + ":" + port);
        container.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {

                try {
                    byte[] body = message.getBody();
                    ObjectMapper mapper = new ObjectMapper();
                    JudgeInfo judgeInfo = mapper.readValue(body, JudgeInfo.class);
                    log.info("QUEUE_JUDGE_COMMON receive judge : "  + judgeInfo.toString());
                    judgeService.judgeProcess(judgeInfo);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });
        container.start();
    }



//    @Autowired
//    private RabbitMQUtil rabbitMQUtil;
//    @Autowired
//    private JudgeService judgeService;
//
//    @RabbitListener(queues = {RabbitmqConfig.QUEUE_JUDGE_COMMON})
//    public void receiveCommon(byte[] bytes) throws Exception {
//        JudgeInfo judgeInfo = (JudgeInfo) rabbitMQUtil.getObjectFromBytes(bytes);
//        log.info("Queue_JUDGE_COMMON receive judge" + judgeInfo.getId());
//        judgeService.judgeProcess(judgeInfo);
//    }
//    @RabbitListener(queues = {RabbitmqConfig.QUEUE_JUDGE_CONTEST})
//    public void receiveContest(byte[] bytes) throws Exception {
//        System.out.println(bytes);
////        Judge q = (Judge) rabbitMQUtil.getObjectFromBytes(bytes);
////        System.out.println("Queue_COMMON msg : " + q.toString());
//    }


}
