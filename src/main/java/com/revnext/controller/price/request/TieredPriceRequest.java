package com.revnext.controller.price.request;


import java.math.BigDecimal;
import lombok.Data;

@Data
public class TieredPriceRequest {
    private Integer minQuantity;
    private Integer maxQuantity;
    private BigDecimal unitPrice;
}

