package com.tiduswr.rcauth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tiduswr.rcauth.models.RefreshToken;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<RefreshToken, Long> {
    @Query("SELECT CASE " +
            "WHEN COUNT(t) > 0 " +
            "THEN true ELSE false END From RefreshToken t WHERE t.token = :token")
    boolean existsByToken(@Param("token") String token);

    @Query("SELECT t FROM RefreshToken t WHERE t.token = :token")
    Optional<RefreshToken> findByToken(@Param("token") String token);

    @Modifying
    @Query("DELETE FROM RefreshToken t WHERE t.username = :username")
    void deleteByUser(@Param("username") String username);
}
