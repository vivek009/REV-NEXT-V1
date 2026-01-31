package com.revnext.controller.user;

import com.revnext.constants.Roles;
import com.revnext.constants.UserStatus;
import com.revnext.controller.BaseController;
import com.revnext.controller.URLConstants;
import com.revnext.controller.user.request.RegistrationRequest;
import com.revnext.controller.user.response.UserResponse;
import com.revnext.domain.user.Role;
import com.revnext.domain.user.User;
import com.revnext.repository.user.UserRoleRepository;
import com.revnext.service.user.UserService;
import com.revnext.service.user.exception.UserNotAuthorizedException;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@CommonsLog
@Validated
public class UserController extends BaseController {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserService userService;

    @PostMapping(value = URLConstants.REGISTRATION, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registration(@RequestBody RegistrationRequest registrationRequest)
            throws UserNotAuthorizedException {
        return getResponse(() -> {
            User user = new User();
            user.setUserName(registrationRequest.getUsername());
            user.setPassword("Admin@123");
            user.setEmail(registrationRequest.getEmail());
            user.setMobileNumber(registrationRequest.getMobileNumber());
            user.setRefreshToken(UUID.randomUUID().toString());
            user.setDepartment("");
            user.setName(registrationRequest.getName());
            user.setDivisionName(registrationRequest.getDivisionName());
            user.setCircleName(registrationRequest.getCircleName());
            user.setShopName(registrationRequest.getShopName());
            user.setStatus(UserStatus.ACTIVE);

            Set<Role> roles = new HashSet<>();
            for (Roles role : registrationRequest.getRoles()) {
                Role userRole = userRoleRepository.findByName(role).orElse(null);
                roles.add(userRole);
            }

            user.setRoles(roles);
            userService.saveUser(user);

            return "User registered successfully";
        });
    }

    @PutMapping(value = URLConstants.UPDATE_USER
            + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateUser(@PathVariable UUID id,
            @RequestBody RegistrationRequest registrationRequest) throws UserNotAuthorizedException {

        return getResponse(() -> {
            // Find the existing user based on their userId (assumed to be passed in
            // RegistrationRequest)
            User user = userService.findByUserId(id);

            // Update user fields
            user.setUserName(StringUtils.isEmpty(registrationRequest.getUsername()) ? user.getUserName()
                    : registrationRequest.getUsername());
            user.setPassword(
                    user.getPassword() // don't overwrite password if not provided
            );
            user.setEmail(registrationRequest.getEmail());
            user.setMobileNumber(registrationRequest.getMobileNumber());
            user.setName(registrationRequest.getName());
            user.setDivisionName(registrationRequest.getDivisionName());
            user.setCircleName(registrationRequest.getCircleName());
            user.setShopName(registrationRequest.getShopName());
            // Set the workshop (either from the request or fallback)

            // Update user roles
            Set<Role> roles = new HashSet<>();
            for (Roles role : registrationRequest.getRoles()) {
                Role userRole = userRoleRepository.findByName(role).orElse(null);
                roles.add(userRole);
            }
            user.setRoles(roles);
            // Save the updated user
            userService.saveUser(user);

            return "User updated successfully";
        });
    }

    @DeleteMapping(value = "/delete/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        return getResponse(() -> {
            userService.deleteUser(username);
            return "User deleted successfully";
        });
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserResponse>> getAllUsers(@RequestAttribute(name = "UserId") UUID userId) {
        List<User> users = userService.getAllUsers().stream().filter(user -> !user.getUserId().equals(userId)).toList();
        List<UserResponse> userResponses = users.stream()
                .map(user -> new UserResponse(user.getUserId(), user.getUserName(), user.getName(), user.getEmail(),
                        user.getMobileNumber(), user.getRolesAsString(), user.getRolesAsValue(),
                        user.getProfilePictureUrl(), user.getCircleName(), user.getDivisionName(), user.getShopName()))
                .toList();

        return ResponseEntity.ok(userResponses);
    }

    @PutMapping("/name")
    public ResponseEntity<String> updateUserName(
            @RequestAttribute(name = "UserId") UUID userId,
            @RequestParam String newUserName) {

        return getResponse(() -> {
            userService.updateUserName(userId, newUserName);
            return "Name updated successfully";
        });
    }

    @PutMapping("/password")
    public ResponseEntity<String> updateUserPassword(
            @RequestAttribute(name = "UserId") UUID userId,
            @RequestBody String newPassword) {
        return getResponse(() -> {
            userService.updateUserPassword(userId, newPassword);
            return "Password updated successfully";
        });
    }

    /**
     * Update the user status
     */
    @PutMapping("/{userId}/status")
    public ResponseEntity<String> updateUserStatus(
            @PathVariable UUID userId,
            @RequestParam UserStatus status) {
        userService.updateUserStatus(userId, status);
        return ResponseEntity.ok("User " + status + "updated successfully.");
    }

}
