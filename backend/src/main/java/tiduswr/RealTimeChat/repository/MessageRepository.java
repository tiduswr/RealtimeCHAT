package tiduswr.RealTimeChat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tiduswr.RealTimeChat.model.Message;
import tiduswr.RealTimeChat.model.PrivateMessageDTO;
import tiduswr.RealTimeChat.model.PublicMessageDTO;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT new tiduswr.RealTimeChat.model.PrivateMessageDTO(m.receiver.userName, m.message, m.read, m.createdAt) " +
            "FROM Message m " +
            "WHERE m.sender.userName = :username AND m.receiver.userName = :receiver")
    List<PrivateMessageDTO> filterByUsernameAndReceiver(String username, String receiver);

    @Query("SELECT new tiduswr.RealTimeChat.model.PublicMessageDTO(m.message, m.read, m.createdAt) " +
            "FROM Message m " +
            "WHERE m.receiver.userName IS NULL")
    List<PublicMessageDTO> filterPublicChatMessages();

}
