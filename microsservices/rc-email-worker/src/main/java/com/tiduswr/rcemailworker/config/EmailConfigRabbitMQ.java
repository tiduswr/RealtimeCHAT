package com.tiduswr.rcemailworker.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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

    @Bean
    MessageConverter jsonMessageConverter(){
        final ObjectMapper mapper = new ObjectMapper();

        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());
        
        return new Jackson2JsonMessageConverter(mapper);
    }

}
