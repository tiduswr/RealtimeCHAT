package tiduswr.RealTimeChat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tiduswr.RealTimeChat.model.ProfileImage;

import java.util.Optional;

@Repository
public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
    @Query("SELECT pi FROM ProfileImage pi WHERE pi.user.userName = :username")
    Optional<ProfileImage> findByUserName(@Param("username") String username);
}
