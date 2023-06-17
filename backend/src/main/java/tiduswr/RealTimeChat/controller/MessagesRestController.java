package tiduswr.RealTimeChat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tiduswr.RealTimeChat.model.dto.MessageReadCountDTO;
import tiduswr.RealTimeChat.model.dto.PrivateMessageDTO;
import tiduswr.RealTimeChat.model.dto.PublicMessageDTO;
import tiduswr.RealTimeChat.model.dto.PublicUserDTO;
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

    @GetMapping("/retrieve_users/talked")
    @ResponseStatus(HttpStatus.OK)
    public List<PublicUserDTO> retrieveTalkedUsers(@RequestHeader("Authorization") String auth){
        String username = jwtService.extractUsername(auth);
        return messageService.findAllTalkedUsers(username);
    }

    @GetMapping("/retrieve_count/by/{sender}")
    @ResponseStatus(HttpStatus.OK)
    public MessageReadCountDTO countMessagesUnreadedByTalkingPerson(@RequestHeader("Authorization") String auth,
                                                                    @PathVariable("sender") String sender){
        String username = jwtService.extractUsername(auth);
        return messageService.countUnreadedMessagesByUsernameAndReceiver(username, sender);
    }

    @GetMapping("/retrieve_count/total")
    @ResponseStatus(HttpStatus.OK)
    public MessageReadCountDTO countTotalMessagesUnreaded(@RequestHeader("Authorization") String auth){
        String username = jwtService.extractUsername(auth);
        return messageService.countTotalUnreadedMessage(username);
    }

    @PutMapping("/mark_messages_as_read/{sender}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markAllUnreadMessasAsReadByTalkingPerson(@RequestHeader("Authorization") String auth,
                                                         @PathVariable("sender") String sender){
        String username = jwtService.extractUsername(auth);
        messageService.markMessagesAsReadedBySenderAndReceiver(username, sender);
    }

}
