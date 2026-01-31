package com.revnext.service.otp;

public interface OtpService {
    void sendOtp(String mobileNumber);

    boolean verifyOtp(String mobileNumber, String otp);
}
