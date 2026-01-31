package com.revnext.controller.catalog.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductRequest {
    private String name;
    private String description;
    private String sku;
    private Map<String, String> attributes;
}
