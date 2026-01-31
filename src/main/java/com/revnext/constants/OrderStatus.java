package com.revnext.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum OrderStatus {
    PENDING("Pending",0),
    COMPLETED("Completed",10),
    PARTIALLY_COMPLETED("Partially Completed",20),
    REJECTED("Rejected",30),
    APPROVED("Approved",40),
    PARTIALLY_APPROVED("Partially Approved",50),
    INVOICE_DENIED("Invoice Denied",60),
    INVOICE_GENERATED("Invoice Generated",70),
    INVOICE_APPROVED("Invoice Approved",80),
    GATEPASS_DENIED("Gatepass Denied",90),
    GATEPASS_GENERATED("Gatepass Generated",100),
    PAYMENT_RECEIVED("Payment Received",110);

    private final String label;
    private final int level;

    public static OrderStatus fromLabel(String label) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.label.equalsIgnoreCase(label)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unexpected label: " + label);
    }
}

