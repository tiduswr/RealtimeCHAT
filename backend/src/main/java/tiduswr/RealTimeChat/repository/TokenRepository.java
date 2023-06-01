package tiduswr.RealTimeChat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tiduswr.RealTimeChat.model.security.RefreshToken;
import tiduswr.RealTimeChat.model.User;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<RefreshToken, Long> {
    @Query("SELECT CASE " +
            "WHEN COUNT(t) > 0 " +
            "THEN true ELSE false END From RefreshToken t WHERE t.token = :token")
    boolean existsByToken(@Param("token") String token);

    @Query("SELECT t FROM RefreshToken t WHERE t.token = :token")
    Optional<RefreshToken> findByToken(@Param("token") String token);

    @Modifying
    @Query("DELETE FROM RefreshToken t WHERE t.user = :user")
    void deleteByUser(@Param("user") User user);
}
