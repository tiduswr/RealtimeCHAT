package tiduswr.RealTimeChat.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import tiduswr.RealTimeChat.model.Message;
import tiduswr.RealTimeChat.model.dto.MessageReadCountDTO;
import tiduswr.RealTimeChat.model.dto.PrivateMessageDTO;
import tiduswr.RealTimeChat.model.dto.PublicMessageDTO;
import tiduswr.RealTimeChat.model.Status;
import tiduswr.RealTimeChat.model.dto.PublicUserDTO;
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

    public Page<PublicMessageDTO> getPublicChatMessages(int page, int size){
        Pageable pageable = createPageable(page, size, messageRepository.countPublicMessages());
        return messageRepository.filterPublicChatMessages(pageable);
    }

    @Transactional(readOnly = true)
    public Page<PrivateMessageDTO> getPrivateMessagesBy(String username, String receiver, int page, int size){
        Pageable pageable = createPageable(page, size, messageRepository.countByUsernameAndReceiver(username, receiver));
        return messageRepository.filterByUsernameAndReceiver(username, receiver, pageable);
    }

    @Transactional(readOnly = true)
    public List<PublicUserDTO> findAllTalkedUsers(String username) {
        return messageRepository.findAllInteractedUsersByUsername(username);
    }

    @Transactional(readOnly = true)
    public MessageReadCountDTO countUnreadedMessagesByUsernameAndReceiver(String username, String sender){
        return new MessageReadCountDTO((long) messageRepository
                .countUnreadedMessagesBySenderAndReceiver(sender, username).size());
    }

    @Transactional
    public void markMessagesAsReadedBySenderAndReceiver(String receiver, String sender){
        var messages = messageRepository
                .countUnreadedMessagesBySenderAndReceiver(sender, receiver);
        messages.forEach(e -> {
            e.setRead(Boolean.TRUE);
            messageRepository.save(e);
        });
    }

    @Transactional(readOnly = true)
    public MessageReadCountDTO countTotalUnreadedMessage(String username){
        return new MessageReadCountDTO(messageRepository
                .countUnreadedMessagesByReceiver(username));
    }

    private Pageable createPageable(int page, int size, long totalRows){
        int totalPages = (int) Math.ceil((double) totalRows/size);
        int currentPage = totalPages - (page - 1);
        return PageRequest.of(page, size);
    }

}
