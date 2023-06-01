package tiduswr.RealTimeChat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tiduswr.RealTimeChat.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
