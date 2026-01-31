package com.revnext.controller.login.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MobileOtpRequest {
    private String mobileNumber;
    private String otp; // Optional for sending OTP
}
