package com.revnext.controller.price.response;


import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PriceValidityResponse {
    private UUID id;
    private String location;
    private String currency;
    private String country;
    private Date validFrom;
    private Date validTill;
    private List<TieredPriceResponse> tiers;
}
