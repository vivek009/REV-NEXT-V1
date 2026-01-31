package com.revnext.controller.customer.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSegmentResponse {
    private UUID id;
    private String name;
    private String description;
    private List<CustomerResponse> customers;
}
