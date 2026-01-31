package com.revnext.controller.login;

import com.revnext.controller.BaseController;
import com.revnext.controller.login.request.MobileOtpRequest;
import com.revnext.controller.login.response.LoginResponse;
import com.revnext.service.otp.OtpService;
import jakarta.validation.Valid;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController("otp")
@CommonsLog
@Validated
public class MobileOtpAuthController extends BaseController {

    @Autowired
    private OtpService otpService;

    @PostMapping(value = "/send-otp", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendOtp(@Valid @RequestBody MobileOtpRequest mobileOtpRequest) {
        otpService.sendOtp(mobileOtpRequest.getMobileNumber());
        return ResponseEntity.ok("OTP sent successfully.");
    }

    @PostMapping(value = "/verify-otp", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> verifyOtp(@Valid @RequestBody MobileOtpRequest mobileOtpRequest) {
        boolean isVerified = otpService.verifyOtp(mobileOtpRequest.getMobileNumber(), mobileOtpRequest.getOtp());
        if (isVerified) {
            return ResponseEntity.ok(LoginResponse.builder()
                    .name("User with Mobile " + mobileOtpRequest.getMobileNumber())
                    .token("mock-jwt-token") // Replace with real JWT logic
                    .build());
        } else {
            throw new IllegalArgumentException("Invalid or expired OTP.");
        }
    }
}
