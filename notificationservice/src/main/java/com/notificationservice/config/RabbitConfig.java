package com.notificationservice.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${notification.exchange}")
    private String exchange;

    @Value("${notification.queue}")
    private String queue;

    @Value("${notification.routingKey}")
    private String routingKey;

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(queue).build();
    }

    @Bean
    public Binding binding(Queue notificationQueue, TopicExchange topicExchange) {
        return BindingBuilder
                .bind(notificationQueue)
                .to(topicExchange)
                .with(routingKey);
    }
}

