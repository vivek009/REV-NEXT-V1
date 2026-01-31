package com.revnext.controller.quote.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class OrderResponse {
    private String id;
    private Long invoiceId;
    private Long indentId;
    private Long gatepassId;
    private String paymentBy;
    private String transferredTo;
    private String paymentType;
    private LocalDate date;
    private String status;
    private Double amount;
    private Double approvedAmount;
    private List<OrderItemResponse> orderItems;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
    private LocalDateTime processedDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
    private LocalDateTime partialProcessedDate;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode
    public static class OrderItemResponse {
        private String id;
        private UUID productId;
        private String productName;
        private String measurementUnit;
        private double quantity;
        private double approvedQuantity;
        private double availableQuantity;
        private double price;
    }
}
