package com.tiduswr.rcauth.config;

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
    Queue queue(){
        return QueueBuilder.durable(QUEUE).build();
    }

    Binding binding(){
        return BindingBuilder.bind(queue())
            .to(emailExchange())
            .with(ROUTING_KEY);
    }

}
