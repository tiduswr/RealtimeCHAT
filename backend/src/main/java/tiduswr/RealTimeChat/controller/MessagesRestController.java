package tiduswr.RealTimeChat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tiduswr.RealTimeChat.model.PrivateMessageDTO;
import tiduswr.RealTimeChat.model.PublicMessageDTO;
import tiduswr.RealTimeChat.services.JwtService;
import tiduswr.RealTimeChat.services.MessageService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
public class MessagesRestController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/receiver/{receiver}")
    @ResponseStatus(HttpStatus.OK)
    public List<PrivateMessageDTO> getMessageByReceiver(@PathVariable("receiver") String receiver,
                                                        @RequestHeader("Authorization") String auth){
        String username = jwtService.extractUsername(auth);
        return messageService.getPrivateMessagesBy(username, receiver);
    }

    @GetMapping("/public")
    @ResponseStatus(HttpStatus.OK)
    public List<PublicMessageDTO> getPublicChatMessages(){
        return messageService.getPublicChatMessages();
    }

}
