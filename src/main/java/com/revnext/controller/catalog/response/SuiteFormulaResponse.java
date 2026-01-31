package com.revnext.controller.catalog.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class SuiteFormulaResponse {
    private UUID id;
    private UUID familyId;
    private List<IngredientResponse> ingredients;
    private List<SuiteResponse> suites;

    @Data
    public static class IngredientResponse {
        private UUID id;
        private UUID productId;
        private BigDecimal quantity;
        private String measurementUnit;
    }
}
