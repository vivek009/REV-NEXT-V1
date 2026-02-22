package com.revnext.controller.catalog.mapper;

import com.revnext.controller.catalog.request.ProductRequest;
import com.revnext.controller.catalog.response.ProductResponse;
import com.revnext.domain.approval.ApprovalStatus;
import com.revnext.domain.catalog.Image;
import com.revnext.domain.catalog.Product;
import com.revnext.domain.catalog.ProductAttribute;
import com.revnext.domain.catalog.ProductFamily;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductMapper {

    public static Product toEntity(ProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .sku(request.getSku())
                .productFamily(request.getFamilyId() != null ? ProductFamily.builder().id(request.getFamilyId()).build()
                        : null)
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

        if (request.getImageNames() != null) {
            product.setImages(request.getImageNames().stream()
                    .map(name -> Image.builder()
                            .name(name)
                            .uri("/images/" + name)
                            .product(product)
                            .build())
                    .collect(Collectors.toCollection(java.util.ArrayList::new)));
        }

        return product;
    }

    public static ProductResponse toResponse(Product product, ApprovalStatus status) {
        Map<String, String> attributes = new HashMap<>();
        if (product.getAttributes() != null) {
            product.getAttributes().forEach(attr -> attributes.put(attr.getKey(), attr.getValue()));
        }

        java.util.List<String> imageNames = new java.util.ArrayList<>();
        if (product.getImages() != null) {
            product.getImages().forEach(img -> imageNames.add(img.getName()));
        }

        String imageUrl = null;
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            imageUrl = product.getImages().get(0).getUri();
        }

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .sku(product.getSku())
                .familyId(product.getProductFamily() != null ? product.getProductFamily().getId() : null)
                .familyName(product.getProductFamily() != null ? product.getProductFamily().getName() : null)
                .imageUrl(imageUrl)
                .approvalStatus(status)
                .imageNames(imageNames)
                .attributes(attributes)
                .build();
    }
}
