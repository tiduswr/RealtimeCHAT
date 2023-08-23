package com.tiduswr.rcmessage.resources;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import com.tiduswr.rcmessage.model.Status;
import com.tiduswr.rcmessage.model.dto.PrivateMessageDTO;
import com.tiduswr.rcmessage.model.dto.PublicMessageDTO;
import com.tiduswr.rcmessage.services.MessageService;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@SuppressWarnings("unused")
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessageService messageService;

    @MessageMapping("/message")
    @SendTo("/topic/chatroom.public")
    public PublicMessageDTO receivePublicMessage(@Payload @Valid PublicMessageDTO message,
                                                 Principal principal){

        return messageService.createPublicMessage(message, principal.getName());
    }

    @MessageMapping("/private-message")
    public PrivateMessageDTO receivePrivateMessage(@Payload @Valid PrivateMessageDTO message,
                                                   Principal principal){

        if(message.getStatus().equals(Status.READ)){
            simpMessagingTemplate.convertAndSend("/topic/private." + message.getReceiver(), message);
            return message;
        }

        PrivateMessageDTO persistedMessage = messageService.createPrivateMessage(message, principal.getName());
        simpMessagingTemplate.convertAndSend("/topic/private." + message.getReceiver(), persistedMessage);
        simpMessagingTemplate.convertAndSend("/topic/private." + persistedMessage.getSender(), persistedMessage);
        return persistedMessage;
    }

    @MessageExceptionHandler
    public void handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, Principal principal) {
        simpMessagingTemplate.convertAndSend("/topic/errors." + principal.getName(), new ErrorMessage(exception));
    }

}
