package com.revnext.controller.user.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserResponse {
    private UUID userId;
    private String username;
    private String name;
    private String email;
    private String mobileNumber;
    private String role;
    private String roleValue;
    private String profilePictureUrl;
    private String circleName;
    private String divisionName;
    private String shopName;
}
