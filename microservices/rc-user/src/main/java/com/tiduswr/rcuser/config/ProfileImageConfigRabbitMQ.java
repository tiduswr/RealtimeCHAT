package com.tiduswr.rcuser.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProfileImageConfigRabbitMQ {
    
    public static final String QUEUE = "ProfileImageQueue";
    public static final String EXCHANGE = "ExchangeProfileImage";
    public static final String ROUTING_KEY = "saveProfileImage";

    @Bean
    DirectExchange imageExchange(){
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    Queue queue(){
        return QueueBuilder.durable(QUEUE).build();
    }

    @Bean
    Binding binding(){
        return BindingBuilder.bind(queue()).to(imageExchange()).with(ROUTING_KEY);
    }

}
