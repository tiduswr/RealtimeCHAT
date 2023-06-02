package tiduswr.RealTimeChat.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tiduswr.RealTimeChat.model.Message;
import tiduswr.RealTimeChat.model.PrivateMessageDTO;
import tiduswr.RealTimeChat.model.PublicMessageDTO;
import tiduswr.RealTimeChat.model.Status;
import tiduswr.RealTimeChat.repository.MessageRepository;

import java.time.LocalDateTime;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    public Message getMessageById(Long id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Transactional(readOnly = false)
    public PublicMessageDTO createPublicMessage(PublicMessageDTO message, String username) {
        var sender = userService.findUserByUsername(username);
        var newMessage = new Message();

        newMessage.setMessage(message.message());
        newMessage.setRead(false);
        newMessage.setSender(sender);
        newMessage.setStatus(Status.MESSAGE);
        newMessage = messageRepository.save(newMessage);

        return new PublicMessageDTO(newMessage.getMessage(), newMessage.getRead(), newMessage.getCreatedAt());
    }

    @Transactional(readOnly = false)
    public PrivateMessageDTO createPrivateMessage(PrivateMessageDTO message, String username) {
        var receiver = userService.findUserByUsername(message.receiver());
        var sender = userService.findUserByUsername(username);
        var newMessage = new Message();

        newMessage.setMessage(message.message());
        newMessage.setRead(false);
        newMessage.setSender(sender);
        newMessage.setReceiver(receiver);
        newMessage.setStatus(Status.MESSAGE);
        newMessage = messageRepository.save(newMessage);

        return new PrivateMessageDTO(newMessage.getReceiver().getUserName(),
                newMessage.getMessage(),newMessage.getRead(), newMessage.getCreatedAt());
    }

}
