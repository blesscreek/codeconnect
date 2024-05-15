package com.co.common.config;


import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-05-07 16:06
 */

@Configuration
public class RabbitmqConfig {
    public static final String QUEUE_JUDGE_COMMON = "queue_judge_common";
    public static final String QUEUE_JUDGE_CONTEST = "queue_judge_contest";
    public static final String EXCHANGE_TOPIC_JUDGE = "exchange_topic_judge";
    public static final String ROUTINGKEY_JUDGE_COMMON = "judge.common";
    public static final String ROUTINGKEY_JUDGE_CONTEST = "judge.contest";

    @Bean(EXCHANGE_TOPIC_JUDGE)
    public Exchange topicExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPIC_JUDGE).durable(true).build();
    }
    @Bean(QUEUE_JUDGE_COMMON)
    public Queue JudgeCommonQueue() {
        return new Queue(QUEUE_JUDGE_COMMON);
    }
    @Bean(QUEUE_JUDGE_CONTEST)
    public Queue JudgeSpjQueue() {
        return new Queue(QUEUE_JUDGE_CONTEST);
    }
    @Bean
    public Binding bindingJudgeCommon(@Qualifier(QUEUE_JUDGE_COMMON) Queue queue,@Qualifier(EXCHANGE_TOPIC_JUDGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_JUDGE_COMMON).noargs();
    }
    @Bean
    public Binding bindingJudgeSpj(@Qualifier(QUEUE_JUDGE_CONTEST) Queue queue,@Qualifier(EXCHANGE_TOPIC_JUDGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_JUDGE_CONTEST).noargs();
    }
}
