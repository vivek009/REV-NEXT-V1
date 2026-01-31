package com.revnext.controller.catalog.mapper;

import com.revnext.controller.catalog.request.ProductRequest;
import com.revnext.controller.catalog.response.ProductResponse;
import com.revnext.domain.catalog.Product;
import com.revnext.domain.catalog.ProductAttribute;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductMapper {

    public static Product toEntity(ProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .sku(request.getSku())
                .build();

        if (request.getAttributes() != null) {
            product.setAttributes(request.getAttributes().entrySet().stream()
                    .map(entry -> ProductAttribute.builder()
                            .key(entry.getKey())
                            .value(entry.getValue())
                            .product(product)
                            .build())
                    .collect(Collectors.toList()));
        }

        return product;
    }

    public static ProductResponse toResponse(Product product) {
        Map<String, String> attributes = new HashMap<>();
        if (product.getAttributes() != null) {
            product.getAttributes().forEach(attr -> attributes.put(attr.getKey(), attr.getValue()));
        }

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .sku(product.getSku())
                .approvalStatus(product.getApprovalStatus())
                .attributes(attributes)
                .build();
    }
}
