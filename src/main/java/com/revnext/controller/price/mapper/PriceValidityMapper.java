package com.revnext.controller.price.mapper;

import com.revnext.controller.price.request.PriceValidityRequest;
import com.revnext.controller.price.response.PriceValidityResponse;
import com.revnext.domain.price.Price;
import com.revnext.domain.price.PriceValidity;
import java.util.stream.Collectors;

public class PriceValidityMapper {

    public static PriceValidity toEntity(PriceValidityRequest request, Price price) {
        return PriceValidity.builder()
                .location(request.getLocation())
                .currency(request.getCurrency())
                .country(request.getCountry())
                .validFrom(request.getValidFrom())
                .validTill(request.getValidTill())
                .price(price)
                .build();
    }

    public static PriceValidityResponse toResponse(PriceValidity entity) {
        return PriceValidityResponse.builder()
                .id(entity.getId())
                .location(entity.getLocation())
                .currency(entity.getCurrency())
                .country(entity.getCountry())
                .validFrom(entity.getValidFrom())
                .validTill(entity.getValidTill())
                .tiers(entity.getTiers() != null ?
                        entity.getTiers().stream()
                                .map(TieredPriceMapper::toResponse)
                                .collect(Collectors.toList())
                        : null)
                .build();
    }
}
