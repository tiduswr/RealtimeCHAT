package com.tiduswr.rcimageworker.rabbitmq;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tiduswr.rcimageworker.config.ProfileImageConfigRabbitMQ;
import com.tiduswr.rcimageworker.exceptions.ImageNotSupportedException;
import com.tiduswr.rcimageworker.models.dto.ImageDTO;
import com.tiduswr.rcimageworker.services.ImageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ProfileImageConsumer {
    
    @Autowired
    private ImageService imageService;

    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue(ProfileImageConfigRabbitMQ.QUEUE),
            exchange = @Exchange(name = ProfileImageConfigRabbitMQ.EXCHANGE),
            key = { ProfileImageConfigRabbitMQ.ROUTING_KEY }
        )
    )
    public void processProfileImage(Message message, final ImageDTO dto){
        try{
            
            imageService.saveProfileImage(dto.username(), dto.image());

        }catch(IOException ex){
            log.error("Erro ao salvar imagem", ex);
        }catch(ImageNotSupportedException ex){
            log.error("Imagem não suportada", ex);
        }
    }

}
