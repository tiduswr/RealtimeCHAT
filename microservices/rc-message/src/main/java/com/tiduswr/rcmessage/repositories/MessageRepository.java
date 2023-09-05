package com.tiduswr.rcmessage.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.tiduswr.rcmessage.model.Message;
import com.tiduswr.rcmessage.model.dto.PrivateMessageDTO;
import com.tiduswr.rcmessage.model.dto.PublicMessageDTO;
import com.tiduswr.rcmessage.model.dto.PublicUserDTO;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT new com.tiduswr.rcmessage.model.dto.PrivateMessageDTO(m.id, m.sender, m.receiver, m.message, m.read, m.createdAt, m.status) " +
            "FROM Message m " +
            "WHERE (m.sender = :username OR m.sender = :receiver) " +
            "AND (m.receiver = :username OR m.receiver = :receiver) ORDER BY m.id")
    List<PrivateMessageDTO> filterByUsernameAndReceiver(@Param("username") String username, @Param("receiver") String receiver);

    @Query("SELECT new com.tiduswr.rcmessage.model.dto.PrivateMessageDTO(m.id, m.sender, m.receiver, m.message, m.read, m.createdAt, m.status) " +
            "FROM Message m " +
            "WHERE (m.sender = :username OR m.sender = :receiver) " +
            "AND (m.receiver = :username OR m.receiver = :receiver)")
    Page<PrivateMessageDTO> filterByUsernameAndReceiver(@Param("username") String username, @Param("receiver") String receiver, Pageable pageable);

    @Query("SELECT count(m) FROM Message m " +
            "WHERE (m.sender = :username OR m.sender = :receiver) " +
            "AND (m.receiver = :username OR m.receiver = :receiver)")
    long countByUsernameAndReceiver(@Param("username") String username, @Param("receiver") String receiver);

    @Query("SELECT new com.tiduswr.rcmessage.model.dto.PublicMessageDTO(m.id, m.message, m.sender, m.read, m.createdAt, m.status) " +
            "FROM Message m " +
            "WHERE m.receiver IS NULL ORDER BY m.id")
    List<PublicMessageDTO> filterPublicChatMessages();

    @Query("SELECT new com.tiduswr.rcmessage.model.dto.PublicMessageDTO(m.id, m.message, m.sender, m.read, m.createdAt, m.status) " +
            "FROM Message m " +
            "WHERE m.receiver IS NULL")
    Page<PublicMessageDTO> filterPublicChatMessages(Pageable pageable);

    @Query("SELECT DISTINCT m FROM Message m " +
            "WHERE m.receiver = :username OR m.sender = :username")
    List<Message> findAllInteractedMessagesByUsername(@Param("username") String username);

    @Query("SELECT m FROM Message m " +
            "WHERE (m.sender = :sender AND m.receiver = :receiver) " +
            "AND m.read = false")
    List<Message> countUnreadedMessagesBySenderAndReceiver(@Param("sender") String senderName, @Param("receiver") String receiver);

    @Query("SELECT count(m) FROM Message m " +
            "WHERE m.receiver = :receiver " +
            "AND m.read = false")
    Long countUnreadedMessagesByReceiver(@Param("receiver") String receiver);

    @Query("SELECT count(m) FROM Message m " +
            "WHERE m.receiver IS NULL " +
            "ORDER BY m.id")
    long countPublicMessages();
}
