package tiduswr.RealTimeChat.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import tiduswr.RealTimeChat.model.dto.PrivateMessageDTO;
import tiduswr.RealTimeChat.model.dto.PublicMessageDTO;
import tiduswr.RealTimeChat.services.MessageService;

import java.security.Principal;

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

        PrivateMessageDTO persistedMessage = messageService.createPrivateMessage(message, principal.getName());
        simpMessagingTemplate.convertAndSendToUser(message.getReceiver(), "/private", message);

        return persistedMessage;
    }

}
