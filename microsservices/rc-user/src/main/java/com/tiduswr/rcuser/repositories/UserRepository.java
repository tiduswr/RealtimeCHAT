package com.tiduswr.rcuser.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.tiduswr.rcuser.model.dto.PublicUserDTO;

import com.tiduswr.rcuser.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.userName = :username")
    Optional<User> findByUserName(@Param("username") String username);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.userName = :username")
    boolean existsByUsername(@Param("username") String username);

    @Query("SELECT DISTINCT new com.tiduswr.rcuser.model.dto.PublicUserDTO(u.userName, u.formalName) FROM User u " +
            "WHERE (u.userName LIKE %:query% OR u.formalName LIKE %:query%) AND NOT u.userName = :ownUsername")
    List<PublicUserDTO> findUsersByUsernameOrFormalname(@Param("query") String query, @Param("ownUsername") String ownUsername);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findUserByEmail(@Param("email") String email);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email AND u.userName != :username")
    boolean existsByEmailExceptUser(@Param("email") String email, @Param("username") String username);
}
