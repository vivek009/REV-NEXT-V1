package com.revnext.service.otp;

import org.springframework.stereotype.Service;

@Service
public class ExternalOtpService implements OtpService {

    @Override
    public void sendOtp(String mobileNumber) {
        // Simulate sending OTP via an external service
        System.out.println("External OTP sent for mobile " + mobileNumber);

        // Example: Call the external service API here
        // e.g., externalOtpProvider.sendOtp(mobileNumber);
    }

    @Override
    public boolean verifyOtp(String mobileNumber, String otp) {
        // Simulate verifying OTP via an external service
        System.out.println("External OTP verification for mobile " + mobileNumber);

        // Example: Call the external service API here
        // e.g., return externalOtpProvider.verifyOtp(mobileNumber, otp);
        return true; // Assuming always successful for demonstration
    }
}
