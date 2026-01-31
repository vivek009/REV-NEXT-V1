package com.revnext.repository.user;

import com.revnext.constants.UserStatus;
import com.revnext.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUserNameAndPassword(String username, String password);

    Optional<User> findByUserName(String username);

    Optional<User> findByUserId(UUID userId);

    boolean existsByUserName(String userName);

    @Modifying
    @Query("UPDATE User u SET u.status = :status WHERE u.userId = :userId")
    void updateUserStatus(@Param("userId") UUID userId, @Param("status") UserStatus status);

}