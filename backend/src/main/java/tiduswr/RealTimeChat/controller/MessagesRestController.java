package tiduswr.RealTimeChat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tiduswr.RealTimeChat.model.dto.PrivateMessageDTO;
import tiduswr.RealTimeChat.model.dto.PublicMessageDTO;
import tiduswr.RealTimeChat.services.JwtService;
import tiduswr.RealTimeChat.services.MessageService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
@SuppressWarnings("unused")
public class MessagesRestController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/retrieve_messages/by/{receiver}")
    @ResponseStatus(HttpStatus.OK)
    public List<PrivateMessageDTO> getMessageByReceiver(@PathVariable("receiver") String receiver,
                                                        @RequestHeader("Authorization") String auth){

        String username = jwtService.extractUsername(auth);
        return messageService.getPrivateMessagesBy(username, receiver);
    }

    @GetMapping("/retrieve_messages/by/public")
    @ResponseStatus(HttpStatus.OK)
    public List<PublicMessageDTO> getPublicChatMessages(){
        return messageService.getPublicChatMessages();
    }

}
