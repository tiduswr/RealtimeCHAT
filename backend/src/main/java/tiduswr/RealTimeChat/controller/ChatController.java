package tiduswr.RealTimeChat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import tiduswr.RealTimeChat.model.PrivateMessageDTO;
import tiduswr.RealTimeChat.model.PublicMessageDTO;
import tiduswr.RealTimeChat.services.JwtService;
import tiduswr.RealTimeChat.services.MessageService;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessageService messageService;

    @Autowired
    private JwtService jwtService;

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public PublicMessageDTO receivePublicMessage(@Payload PublicMessageDTO message,
                                                   @RequestHeader("Authorization") String authorizationHeader){

        String username = jwtService.extractUsername(authorizationHeader);
        return messageService.createPublicMessage(message, username);
    }

    @MessageMapping("/private-message")
    public PrivateMessageDTO receivePrivateMessage(@Payload PrivateMessageDTO message,
                                                   @RequestHeader("Authorization") String authorizationHeader){

        String username = jwtService.extractUsername(authorizationHeader);
        simpMessagingTemplate.convertAndSendToUser(message.receiver(), "/private", message);
        return messageService.createPrivateMessage(message, username);
    }

}
