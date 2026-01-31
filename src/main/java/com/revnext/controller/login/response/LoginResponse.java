package com.revnext.controller.login.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class LoginResponse {
    private String userName;
    private String name;
    private String token;
    private String refreshToken;
    private Set<String> roles;
    private String email;
    private String mobileNumber;
    private String profilePictureUrl;
    private String divisionName;
    private String circleName;
    private String shopName;
    private String workshopName;
}
