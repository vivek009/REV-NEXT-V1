package com.revnext.controller.price.response;


import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PriceResponse {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal ceilPrice;
    private BigDecimal floorPrice;
    private BigDecimal targetPrice;
    private UUID productId;
    private List<PriceValidityResponse> validities;
}
