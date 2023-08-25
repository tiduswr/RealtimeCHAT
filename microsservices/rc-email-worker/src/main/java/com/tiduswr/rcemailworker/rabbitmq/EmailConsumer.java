package com.tiduswr.rcemailworker.rabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tiduswr.rcemailworker.config.EmailConfigRabbitMQ;
import com.tiduswr.rcemailworker.model.dto.EmailDTO;
import com.tiduswr.rcemailworker.services.EmailService;

@Component
public class EmailConsumer {
    
    @Autowired
    private EmailService emailService;

    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue(EmailConfigRabbitMQ.QUEUE),
            exchange = @Exchange(name = EmailConfigRabbitMQ.EXCHANGE),
            key = { EmailConfigRabbitMQ.ROUTING_KEY }
        )
    )
    public void consumeEmail(Message message, EmailDTO dto){
        emailService.sendEmail(dto);
    }

}
