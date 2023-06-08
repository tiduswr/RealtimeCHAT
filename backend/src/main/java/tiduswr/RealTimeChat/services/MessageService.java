package tiduswr.RealTimeChat.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import tiduswr.RealTimeChat.model.Message;
import tiduswr.RealTimeChat.model.dto.PrivateMessageDTO;
import tiduswr.RealTimeChat.model.dto.PublicMessageDTO;
import tiduswr.RealTimeChat.model.Status;
import tiduswr.RealTimeChat.repository.MessageRepository;

import java.util.List;

@Service
@SuppressWarnings("unused")
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public PublicMessageDTO createPublicMessage(PublicMessageDTO message, String username) {
        var sender = userService.findUserByUsername(username);
        var newMessage = new Message();

        newMessage.setMessage(message.getMessage());
        newMessage.setRead(null);
        newMessage.setSender(sender);
        newMessage.setReceiver(null);
        newMessage.setStatus(Status.MESSAGE);
        newMessage = messageRepository.save(newMessage);

        return PublicMessageDTO.from(newMessage);
    }

    @Transactional
    public PrivateMessageDTO createPrivateMessage(PrivateMessageDTO message, String username) {
        var receiver = userService.findUserByUsername(message.getReceiver());
        var sender = userService.findUserByUsername(username);
        var newMessage = new Message();

        newMessage.setMessage(message.getMessage());
        newMessage.setRead(false);
        newMessage.setSender(sender);
        newMessage.setReceiver(receiver);
        newMessage.setStatus(Status.MESSAGE);
        newMessage = messageRepository.save(newMessage);

        return PrivateMessageDTO.from(newMessage);
    }

    @Transactional(readOnly = true)
    public List<PrivateMessageDTO> getPrivateMessagesBy(String username, String receiver) {
        return messageRepository.filterByUsernameAndReceiver(username, receiver);
    }

    @Transactional(readOnly = true)
    public List<PublicMessageDTO> getPublicChatMessages(){
        return messageRepository.filterPublicChatMessages();
    }

}
