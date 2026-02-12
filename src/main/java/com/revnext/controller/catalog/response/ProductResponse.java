package com.revnext.controller.catalog.response;

import com.revnext.domain.approval.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private UUID id;
    private String name;
    private String description;
    private String sku;
    private ApprovalStatus approvalStatus;
    private List<String> imageNames;
    private Map<String, String> attributes;
}
