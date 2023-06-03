package tiduswr.RealTimeChat.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import tiduswr.RealTimeChat.model.dto.PrivateMessageDTO;
import tiduswr.RealTimeChat.model.dto.PublicMessageDTO;
import tiduswr.RealTimeChat.services.JwtService;
import tiduswr.RealTimeChat.services.MessageService;

@Controller
@SuppressWarnings("unused")
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessageService messageService;

    @Autowired
    private JwtService jwtService;

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public PublicMessageDTO receivePublicMessage(@Payload @Valid PublicMessageDTO message,
                                                   @RequestHeader("Authorization") String authorizationHeader){

        String username = jwtService.extractUsername(authorizationHeader);
        return messageService.createPublicMessage(message, username);
    }

    @MessageMapping("/private-message")
    public PrivateMessageDTO receivePrivateMessage(@Payload @Valid PrivateMessageDTO message,
                                                   @RequestHeader("Authorization") String authorizationHeader){

        String username = jwtService.extractUsername(authorizationHeader);
        PrivateMessageDTO persistedMessage = messageService.createPrivateMessage(message, username);
        simpMessagingTemplate.convertAndSendToUser(message.getReceiver(), "/private", message);

        return persistedMessage;
    }

}