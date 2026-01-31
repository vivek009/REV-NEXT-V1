package com.revnext.controller;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class URLConstants {
    public static final String REGISTRATION = "/registration";
    public static final String AUTHENTICATE = "/authenticate";
    public static final String REFRESH_TOKEN = "/refresh_token";
    public static final String UPDATE_USER = "/update_user";
}
