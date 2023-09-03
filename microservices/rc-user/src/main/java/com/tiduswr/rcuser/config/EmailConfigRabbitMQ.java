package com.tiduswr.rcuser.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfigRabbitMQ {
    
    public static final String QUEUE = "EmailQueue";
    public static final String EXCHANGE = "EmailExchange";
    public static final String ROUTING_KEY = "sendEmail";

    @Bean
    DirectExchange emailExchange(){
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    Queue emailQueue(){
        return QueueBuilder.durable(QUEUE).build();
    }

    @Bean
    Binding emailBinding(){
        return BindingBuilder.bind(emailQueue())
            .to(emailExchange())
            .with(ROUTING_KEY);
    }

}
