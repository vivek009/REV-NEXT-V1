package com.revnext.controller.price.mapper;


import com.revnext.controller.price.request.PriceRequest;
import com.revnext.controller.price.response.PriceResponse;
import com.revnext.domain.catalog.Product;
import com.revnext.domain.price.Price;
import java.util.stream.Collectors;

public class PriceMapper {

    public static Price toEntity(PriceRequest request) {
        return Price.builder()
                .name(request.getName())
                .description(request.getDescription())
                .ceilPrice(request.getCeilPrice())
                .floorPrice(request.getFloorPrice())
                .targetPrice(request.getTargetPrice())
                .product(Product.builder().id(request.getProductId()).build())
                .build();
    }

    public static PriceResponse toResponse(Price entity) {
        return PriceResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .ceilPrice(entity.getCeilPrice())
                .floorPrice(entity.getFloorPrice())
                .targetPrice(entity.getTargetPrice())
                .productId(entity.getProduct().getId())
              .validities(entity.getValidities() != null ?
                        entity.getValidities().stream()
                   .map(PriceValidityMapper::toResponse)
                                .collect(Collectors.toList())
                       : null)
                .build();
    }
}

