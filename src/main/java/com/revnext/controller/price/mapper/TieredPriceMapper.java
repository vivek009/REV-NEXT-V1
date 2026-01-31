package com.revnext.controller.price.mapper;


import com.revnext.controller.price.request.TieredPriceRequest;
import com.revnext.controller.price.response.TieredPriceResponse;
import com.revnext.domain.price.PriceValidity;
import com.revnext.domain.price.TieredPrice;

public class TieredPriceMapper {

    public static TieredPrice toEntity(TieredPriceRequest request, PriceValidity validity) {
        return TieredPrice.builder()
                .minQuantity(request.getMinQuantity())
                .maxQuantity(request.getMaxQuantity())
                .unitPrice(request.getUnitPrice())
                .priceValidity(validity)
                .build();
    }

    public static TieredPrice toEntity(TieredPriceRequest request) {
        return toEntity(request, null);
    }

    public static TieredPriceResponse toResponse(TieredPrice entity) {
        return TieredPriceResponse.builder()
                .id(entity.getId())
                .minQuantity(entity.getMinQuantity())
                .maxQuantity(entity.getMaxQuantity())
                .unitPrice(entity.getUnitPrice())
                .build();
    }
}
