package com.revnext.controller.catalog.mapper;

import com.revnext.controller.catalog.request.SuiteFormulaRequest;
import com.revnext.controller.catalog.response.SuiteFormulaResponse;
import com.revnext.controller.catalog.response.SuiteResponse;
import com.revnext.domain.catalog.Ingredient;
import com.revnext.domain.catalog.Product;
import com.revnext.domain.catalog.ProductFamily;
import com.revnext.domain.catalog.SuiteFormula;
import java.util.stream.Collectors;


public class SuiteFormulaMapper {

    public static SuiteFormula toEntity(SuiteFormulaRequest request) {
        SuiteFormula formula = new SuiteFormula();

        if (request.getFamilyId() != null) {
            ProductFamily family = new ProductFamily();
            family.setId(request.getFamilyId());
            formula.setFamily(family);
        }

        if (request.getIngredients() != null) {
            request.getIngredients().forEach(ingredientReq -> {
                Ingredient ingredient = new Ingredient();
                Product product = new Product();
                product.setId(ingredientReq.getProductId());

                ingredient.setProduct(product);
                ingredient.setQuantity(ingredientReq.getQuantity());
                ingredient.setMeasurementUnit(ingredientReq.getMeasurementUnit());

                formula.addIngredient(ingredient); // keeps bi-directional consistency
            });
        }

        return formula;
    }

    public static SuiteFormulaResponse toResponse(SuiteFormula formula) {
        SuiteFormulaResponse response = new SuiteFormulaResponse();

        response.setId(formula.getId());
        response.setFamilyId(formula.getFamily() != null ? formula.getFamily().getId() : null);

        // Map ingredients inline
        if (formula.getIngredients() != null) {
            response.setIngredients(
                    formula.getIngredients().stream().map(ingredient -> {
                        SuiteFormulaResponse.IngredientResponse ir = new SuiteFormulaResponse.IngredientResponse();
                        ir.setId(ingredient.getId());
                        ir.setProductId(ingredient.getProduct() != null ? ingredient.getProduct().getId() : null);
                        ir.setQuantity(ingredient.getQuantity());
                        ir.setMeasurementUnit(ingredient.getMeasurementUnit());
                        return ir;
                    }).collect(Collectors.toList())
            );
        }

        // Map suites inline
        if (formula.getSuites() != null) {
            response.setSuites(
                    formula.getSuites().stream().map(suite -> {
                        SuiteResponse sr = new SuiteResponse();
                        sr.setId(suite.getId());
                        sr.setName(suite.getName());
                        sr.setDescription(suite.getDescription());
                        sr.setFormulaId(formula.getId());
                        sr.setFamilyId(suite.getFamily() != null ? suite.getFamily().getId() : null);
                        sr.setRemark(suite.getRemark());
                        return sr;
                    }).collect(Collectors.toList())
            );
        }

        return response;
    }
}

