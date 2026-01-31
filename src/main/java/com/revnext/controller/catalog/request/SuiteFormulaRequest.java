package com.revnext.controller.catalog.request;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class SuiteFormulaRequest {
    private UUID familyId;
    private List<IngredientRequest> ingredients;


    @Data
    public static class IngredientRequest {
        private UUID productId;
        private BigDecimal quantity;
        private String measurementUnit;
    }
}
