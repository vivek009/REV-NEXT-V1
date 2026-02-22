package com.revnext.controller.search.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GlobalSearchResponse {
    private Page<?> products;
    private Page<?> suites;
    private Page<?> customers;
    private Page<?> discounts;
}
