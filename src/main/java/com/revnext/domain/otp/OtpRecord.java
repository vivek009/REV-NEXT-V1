package com.revnext.domain.otp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "otp_record")
public class OtpRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mobileNumber;

    private String otp;

    private LocalDateTime expiryTime;

    public OtpRecord(String mobileNumber, String otp, LocalDateTime expiryTime) {
        this.mobileNumber = mobileNumber;
        this.otp = otp;
        this.expiryTime = expiryTime;
    }
}
