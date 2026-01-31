package com.revnext.repository.otp;

import com.revnext.domain.otp.OtpRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpRecord, Long> {
    Optional<OtpRecord> findByMobileNumber(String mobileNumber);
}
