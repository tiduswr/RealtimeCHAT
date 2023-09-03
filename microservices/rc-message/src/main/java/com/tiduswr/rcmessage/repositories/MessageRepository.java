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

    @Query("SELECT new com.tiduswr.rcmessage.model.dto.PrivateMessageDTO(m.id, m.sender.userName, m.receiver.userName, m.message, m.read, m.createdAt, m.status) " +
            "FROM Message m " +
            "WHERE (m.sender.userName = :username OR m.sender.userName = :receiver) " +
            "AND (m.receiver.userName = :username OR m.receiver.userName = :receiver) ORDER BY m.id")
    List<PrivateMessageDTO> filterByUsernameAndReceiver(@Param("username") String username, @Param("receiver") String receiver);

    @Query("SELECT new com.tiduswr.rcmessage.model.dto.PrivateMessageDTO(m.id, m.sender.userName, m.receiver.userName, m.message, m.read, m.createdAt, m.status) " +
            "FROM Message m " +
            "WHERE (m.sender.userName = :username OR m.sender.userName = :receiver) " +
            "AND (m.receiver.userName = :username OR m.receiver.userName = :receiver)")
    Page<PrivateMessageDTO> filterByUsernameAndReceiver(@Param("username") String username, @Param("receiver") String receiver, Pageable pageable);

    @Query("SELECT count(m) FROM Message m " +
            "WHERE (m.sender.userName = :username OR m.sender.userName = :receiver) " +
            "AND (m.receiver.userName = :username OR m.receiver.userName = :receiver)")
    long countByUsernameAndReceiver(@Param("username") String username, @Param("receiver") String receiver);

    @Query("SELECT new com.tiduswr.rcmessage.model.dto.PublicMessageDTO(m.id, m.message, m.sender.userName, m.read, m.createdAt, m.status) " +
            "FROM Message m " +
            "WHERE m.receiver IS NULL ORDER BY m.id")
    List<PublicMessageDTO> filterPublicChatMessages();

    @Query("SELECT new com.tiduswr.rcmessage.model.dto.PublicMessageDTO(m.id, m.message, m.sender.userName, m.read, m.createdAt, m.status) " +
            "FROM Message m " +
            "WHERE m.receiver IS NULL")
    Page<PublicMessageDTO> filterPublicChatMessages(Pageable pageable);

    @Query("SELECT DISTINCT " +
            "new com.tiduswr.rcmessage.model.dto.PublicUserDTO(" +
                "CASE " +
                    "WHEN m.receiver.userName = :username THEN m.sender.userName " +
                    "WHEN m.sender.userName = :username THEN m.receiver.userName " +
                    "ELSE '' " +
                "END, " +
                "CASE " +
                    "WHEN m.receiver.userName = :username THEN m.sender.formalName " +
                    "WHEN m.sender.userName = :username THEN m.receiver.formalName " +
                    "ELSE '' " +
                "END" +
            ") " +
            "FROM Message m " +
            "WHERE m.receiver.userName = :username OR m.sender.userName = :username")
    List<PublicUserDTO> findAllInteractedUsersByUsername(@Param("username") String username);

    @Query("SELECT m FROM Message m " +
            "WHERE (m.sender.userName = :sender AND m.receiver.userName = :receiver) " +
            "AND m.read = false")
    List<Message> countUnreadedMessagesBySenderAndReceiver(@Param("sender") String senderName, @Param("receiver") String receiver);

    @Query("SELECT count(m) FROM Message m " +
            "WHERE m.receiver.userName = :receiver " +
            "AND m.read = false")
    Long countUnreadedMessagesByReceiver(@Param("receiver") String receiver);

    @Query("SELECT count(m) FROM Message m " +
            "WHERE m.receiver IS NULL " +
            "ORDER BY m.id")
    long countPublicMessages();
}
