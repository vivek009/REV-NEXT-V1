package com.revnext.controller.customer.mapper;

import com.revnext.controller.customer.request.CustomerSegmentRequest;
import com.revnext.controller.customer.response.CustomerSegmentResponse;
import com.revnext.domain.customer.CustomerSegment;

import java.util.stream.Collectors;

public class CustomerSegmentMapper {

    public static CustomerSegment toEntity(CustomerSegmentRequest request) {
        return CustomerSegment.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    public static CustomerSegmentResponse toResponse(CustomerSegment segment) {
        return CustomerSegmentResponse.builder()
                .id(segment.getId())
                .name(segment.getName())
                .description(segment.getDescription())
                .customers(segment.getCustomers() != null ? segment.getCustomers().stream()
                        .map(CustomerMapper::toResponse)
                        .collect(Collectors.toList()) : null)
                .build();
    }
}
