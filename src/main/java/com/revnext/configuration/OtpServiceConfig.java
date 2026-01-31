package com.revnext.configuration;

import com.revnext.service.otp.ExternalOtpService;
import com.revnext.service.otp.LocalOtpService;
import com.revnext.service.otp.OtpService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OtpServiceConfig {

    @Value("${otp.service.use-external:false}")
    private boolean useExternalOtpService;

    @Bean
    public OtpService otpService(LocalOtpService localOtpService, ExternalOtpService externalOtpService) {
        return useExternalOtpService ? externalOtpService : localOtpService;
    }
}
