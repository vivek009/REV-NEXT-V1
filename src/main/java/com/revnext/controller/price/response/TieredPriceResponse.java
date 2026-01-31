package com.revnext.controller.price.response;


import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TieredPriceResponse {
    private UUID id;
    private Integer minQuantity;
    private Integer maxQuantity;
    private BigDecimal unitPrice;
}

