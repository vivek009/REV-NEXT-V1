package com.revnext.controller.price.request;


import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;

@Data
public class PriceRequest {
    private String name;
    private String description;
    private BigDecimal ceilPrice;
    private BigDecimal floorPrice;
    private BigDecimal targetPrice;
    private UUID productId;
}
