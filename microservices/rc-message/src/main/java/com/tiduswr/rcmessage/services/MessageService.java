package com.tiduswr.rcmessage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import com.tiduswr.rcmessage.feignclients.UserService;
import com.tiduswr.rcmessage.model.Message;
import com.tiduswr.rcmessage.model.dto.MessageReadCountDTO;
import com.tiduswr.rcmessage.model.dto.PrivateMessageDTO;
import com.tiduswr.rcmessage.model.dto.PublicMessageDTO;
import com.tiduswr.rcmessage.model.Status;
import com.tiduswr.rcmessage.model.dto.PublicUserDTO;
import com.tiduswr.rcmessage.repositories.MessageRepository;

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

    @Transactional(readOnly = true)
    public Page<PublicMessageDTO> getPublicChatMessages(int page, int size){
        Pageable pageable = createIdInversedPageable(page, size);
        return messageRepository.filterPublicChatMessages(pageable);
    }

    @Transactional(readOnly = true)
    public Page<PrivateMessageDTO> getPrivateMessagesBy(String username, String receiver, int page, int size){
        Pageable pageable = createIdInversedPageable(page, size);
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

    private Pageable createIdInversedPageable(int page, int size){
        Sort sort = Sort.by(Sort.Order.desc("id"));
        return PageRequest.of(page, size, sort);
    }

}
