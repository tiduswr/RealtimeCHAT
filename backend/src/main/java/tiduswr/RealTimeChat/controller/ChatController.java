package tiduswr.RealTimeChat.controller;

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
import tiduswr.RealTimeChat.exceptions.UnauthorizedException;
import tiduswr.RealTimeChat.model.ErrorResponse;
import tiduswr.RealTimeChat.model.Status;
import tiduswr.RealTimeChat.model.dto.PrivateMessageDTO;
import tiduswr.RealTimeChat.model.dto.PublicMessageDTO;
import tiduswr.RealTimeChat.services.MessageService;

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
    @SendTo("/chatroom/public")
    public PublicMessageDTO receivePublicMessage(@Payload @Valid PublicMessageDTO message,
                                                 Principal principal){

        return messageService.createPublicMessage(message, principal.getName());
    }

    @MessageMapping("/private-message")
    public PrivateMessageDTO receivePrivateMessage(@Payload @Valid PrivateMessageDTO message,
                                                   Principal principal){

        if(message.getStatus().equals(Status.READ)){
            simpMessagingTemplate.convertAndSendToUser(message.getReceiver(), "/private",  message);
            return message;
        }

        PrivateMessageDTO persistedMessage = messageService.createPrivateMessage(message, principal.getName());
        simpMessagingTemplate.convertAndSendToUser(message.getReceiver(), "/private", persistedMessage);
        simpMessagingTemplate.convertAndSendToUser(persistedMessage.getSender(), "/private", persistedMessage);
        return persistedMessage;
    }

    @MessageExceptionHandler
    public void handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, Principal principal) {
        simpMessagingTemplate.convertAndSendToUser(principal.getName(),
                "/errors", new ErrorMessage(exception));
    }

    @MessageExceptionHandler
    public void handleAccessDeniedException(AccessDeniedException exception, Principal principal) {
        simpMessagingTemplate.convertAndSendToUser(principal.getName(),
                "/errors", new ErrorMessage(exception));
    }

}
