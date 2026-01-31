package com.revnext.controller.customer.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSegmentRequest {
    private String name;
    private String description;
    private List<UUID> customerIds;
}
