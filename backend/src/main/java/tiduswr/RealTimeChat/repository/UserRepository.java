package tiduswr.RealTimeChat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tiduswr.RealTimeChat.model.User;
import tiduswr.RealTimeChat.model.dto.PublicUserDTO;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.userName = :username")
    Optional<User> findByUserName(@Param("username") String username);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.userName = :username")
    boolean existsByUsername(@Param("username") String username);

    @Query("SELECT DISTINCT new tiduswr.RealTimeChat.model.dto.PublicUserDTO(u.userName, u.formalName) FROM User u " +
            "WHERE (u.userName LIKE %:query% OR u.formalName LIKE %:query%) AND NOT u.userName = :ownUsername")
    List<PublicUserDTO> findUsersByUsernameOrFormalname(@Param("query") String query, @Param("ownUsername") String ownUsername);
}
