package com.revnext.controller.quote.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class IndentReportResponse {
    private String id;
    private Long invoiceId;
    private String financialYear;
    private String circleName;
    private String divisionName;
    private String paymentType;
    private String indentNo;
    private LocalDate indentDate; // ISO format: '2025-06-01'
    private Double indentAmount;
    private String invoiceNo;
    private LocalDate invoiceDate; // ISO format
    private Double amountIssued;
    private String gatePassNo;
    private String invoiceStatus;
    private String paymentStatus;
    private String utr;
    private String status;
    private String itemIndent;
}
