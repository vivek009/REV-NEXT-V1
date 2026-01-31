package com.revnext.controller.user.request;

import com.revnext.constants.Roles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {
    private String username;
    private String name;
    private String password;
    private String mobileNumber;
    private String email;
    private String circleName;
    private String divisionName;
    private String shopName;
    private Set<Roles> roles;
}

