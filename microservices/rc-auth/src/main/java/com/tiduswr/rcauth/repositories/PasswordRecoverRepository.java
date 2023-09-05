package com.tiduswr.rcauth.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tiduswr.rcauth.models.PasswordRecover;

public interface PasswordRecoverRepository extends JpaRepository<PasswordRecover, Long>{

    @Query("SELECT pr FROM PasswordRecover pr WHERE pr.code = :code")
    Optional<PasswordRecover> findByCode(@Param("code") String code);

    @Query("SELECT pr FROM PasswordRecover pr WHERE pr.username = :username")
    Optional<PasswordRecover> findByUsername(@Param("username") String username);
    
}
