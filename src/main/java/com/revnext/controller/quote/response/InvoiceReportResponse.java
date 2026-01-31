package com.revnext.controller.quote.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class InvoiceReportResponse {
    private UUID orderId;
    private String financialYear;
    private String circleName;
    private String divisionName;
    private String paymentType;
    private String indentNo;
    private LocalDate indentDate;
    private Double indentAmount;
    private String invoiceNo;
    private LocalDate invoiceDate;
    private Double amountIssued;
    private String gatePassNo;
    private String invoiceStatus;
    private String paymentStatus;
    private String utr;
}
