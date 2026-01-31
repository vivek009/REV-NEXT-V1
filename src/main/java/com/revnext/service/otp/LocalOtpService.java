package com.revnext.service.otp;

import com.revnext.domain.otp.OtpRecord;
import com.revnext.repository.otp.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class LocalOtpService implements OtpService {

    private static final int OTP_VALIDITY_MINUTES = 5;

    @Autowired
    private OtpRepository otpRepository;

    @Override
    public void sendOtp(String mobileNumber) {
        String otp = generateOtp();
        OtpRecord otpRecord = new OtpRecord(mobileNumber, otp, LocalDateTime.now().plusMinutes(OTP_VALIDITY_MINUTES));
        otpRepository.save(otpRecord);

        // Simulate sending OTP locally
        System.out.println("Local OTP for mobile " + mobileNumber + ": " + otp);
    }

    @Override
    public boolean verifyOtp(String mobileNumber, String otp) {
        OtpRecord otpRecord = otpRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new IllegalArgumentException("Invalid mobile number or OTP."));

        return otpRecord.getOtp().equals(otp) && otpRecord.getExpiryTime().isAfter(LocalDateTime.now());
    }

    private String generateOtp() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000)); // Generate a 6-digit OTP
    }
}