package com.tiduswr.rcimageworker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tiduswr.rcimageworker.models.ProfileImage;

import java.util.Optional;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
    @Query("SELECT pi FROM ProfileImage pi WHERE pi.user.userName = :username")
    Optional<ProfileImage> findByUserName(@Param("username") String username);
}
