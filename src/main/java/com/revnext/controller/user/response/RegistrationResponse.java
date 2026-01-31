package com.revnext.controller.user.response;

import com.revnext.constants.Roles;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class RegistrationResponse {
    private String name;
    private String token;
    private String refreshToken;
    private Set<Roles> roles;
    private String email;
    private String mobileNumber;
    private String profilePictureUrl;
}
