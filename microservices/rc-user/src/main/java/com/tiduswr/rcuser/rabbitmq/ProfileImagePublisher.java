package com.tiduswr.rcuser.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tiduswr.rcuser.config.ProfileImageConfigRabbitMQ;
import com.tiduswr.rcuser.model.dto.ImageDTO;

@Component
public class ProfileImagePublisher {
    
    @Autowired
    private RabbitTemplate rabbiTemplate;

    public void publishProfileImageSaveRequest(ImageDTO dto){
        rabbiTemplate.setMessageConverter(jsonMessageConverter());
        rabbiTemplate.convertAndSend(ProfileImageConfigRabbitMQ.EXCHANGE, 
            ProfileImageConfigRabbitMQ.ROUTING_KEY, 
            dto
        );
    }

    private MessageConverter jsonMessageConverter(){
        final ObjectMapper mapper = new ObjectMapper();

        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());
        
        return new Jackson2JsonMessageConverter(mapper);
    }

}
