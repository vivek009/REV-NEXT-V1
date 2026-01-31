package com.revnext.controller.customer.mapper;

import com.revnext.controller.customer.request.CustomerRequest;
import com.revnext.controller.customer.response.CustomerResponse;
import com.revnext.domain.customer.Customer;

public class CustomerMapper {

    public static Customer toEntity(CustomerRequest request) {
        return Customer.builder()
                .name(request.getName())
                .description(request.getDescription())
                .address(request.getAddress())
                .build();
    }

    public static CustomerResponse toResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .description(customer.getDescription())
                .address(customer.getAddress())
                .build();
    }
}
