package com.revnext.service.user;

import com.revnext.constants.UserStatus;
import com.revnext.domain.user.User;
import com.revnext.repository.user.UserRepository;
import com.revnext.service.user.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Value("${refreshToken.duration.days}")
    private long durationDays;

    @Value("${refreshToken.duration.hours}")
    private long durationHours;

    @Value("${refreshToken.duration.minutes}")
    private long durationMinutes;

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User getUserForLogin(String userName, String password) {
        return userRepository.findByUserNameAndPassword(userName, password).orElseThrow(() -> new UserNotFoundException("User not found!!"));
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUserName(username);
    }

    @Cacheable(cacheNames = "userCache", key = "#userId", unless = "#result == null")
    public User findByUserId(UUID userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));
        return user;
    }

    public User verifyExpirationAndGetUser(String refreshToken) {
        return null;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }



    public void deleteUser(String username) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    /**
     * Update the username of a user
     */
    @CachePut(cacheNames = "userCache", key = "#userId")
    public void updateUserName(UUID userId, String newUserName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Optional: Check if the username is already taken
        if (userRepository.existsByUserName(newUserName)) {
            throw new RuntimeException("Username already exists");
        }

        user.setUserName(newUserName);
        userRepository.save(user);
    }

    /**
     * Update the password of a user
     */

    public String updateUserPassword(UUID userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(newPassword); // Encrypt password
        userRepository.save(user);
        return "User password updated successfully";
    }

    /**
     * Activate or deactivate a user by ID.
     */
    @CachePut(cacheNames = "userCache", key = "#userId")
    public void updateUserStatus(UUID userId, UserStatus status) {
        userRepository.updateUserStatus(userId, status);
    }
}
