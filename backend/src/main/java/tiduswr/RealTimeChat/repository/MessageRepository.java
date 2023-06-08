package tiduswr.RealTimeChat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tiduswr.RealTimeChat.model.Message;
import tiduswr.RealTimeChat.model.dto.PrivateMessageDTO;
import tiduswr.RealTimeChat.model.dto.PublicMessageDTO;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT new tiduswr.RealTimeChat.model.dto.PrivateMessageDTO(m.sender.userName, m.receiver.userName, m.message, m.read, m.createdAt, m.status) " +
            "FROM Message m " +
            "WHERE m.sender.userName = :username AND m.receiver.userName = :receiver")
    List<PrivateMessageDTO> filterByUsernameAndReceiver(String username, String receiver);

    @Query("SELECT new tiduswr.RealTimeChat.model.dto.PublicMessageDTO(m.message, m.sender.userName, m.read, m.createdAt, m.status) " +
            "FROM Message m " +
            "WHERE m.receiver IS NULL")
    List<PublicMessageDTO> filterPublicChatMessages();

}
