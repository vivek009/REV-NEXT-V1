package com.revnext.service.catalog.validation;

import com.revnext.domain.catalog.Ingredient;
import com.revnext.domain.catalog.Product;
import com.revnext.domain.catalog.ProductAttribute;
import com.revnext.domain.catalog.SuiteFormula;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class SuiteRuleValidator {

    /**
     * Validates a suite configuration against the formula rules.
     * 
     * @param formula          The base formula defining the rules
     * @param selectedProducts Map of Product ID to requested Quantity
     */
    public void validate(SuiteFormula formula, Map<UUID, BigDecimal> selectedProducts) {
        log.info("Validating suite configuration for formula: {}", formula.getId());

        for (Ingredient ingredient : formula.getIngredients()) {
            Product ruleProduct = ingredient.getProduct();
            BigDecimal selectedQty = selectedProducts.getOrDefault(ruleProduct.getId(), BigDecimal.ZERO);

            // 1. Min/Max Quantity Check
            if (ingredient.getMinQuantity() != null
                    && selectedQty.compareTo(new BigDecimal(ingredient.getMinQuantity())) < 0) {
                if (selectedQty.compareTo(BigDecimal.ZERO) == 0 && ingredient.getIsOptional()) {
                    // Optional and not selected is fine
                } else {
                    throw new IllegalArgumentException(
                            "Minimum quantity for " + ruleProduct.getName() + " is " + ingredient.getMinQuantity());
                }
            }

            if (ingredient.getMaxQuantity() != null
                    && selectedQty.compareTo(new BigDecimal(ingredient.getMaxQuantity())) > 0) {
                throw new IllegalArgumentException(
                        "Maximum quantity for " + ruleProduct.getName() + " is " + ingredient.getMaxQuantity());
            }

            // 2. Attribute Logic (Example: Matching Material)
            // This is a simplified example. In a real system, you'd have more complex
            // predicate-based rules.
            // Requirement: "wooden table matches with wooden chairs"
            if (ruleProduct.getName().toLowerCase().contains("chair")) {
                validateAttributeMatch(formula, ruleProduct, "Material");
            }
        }
    }

    private void validateAttributeMatch(SuiteFormula formula, Product product, String attrKey) {
        // Find if there is a 'main' product in the formula to match against (e.g., the
        // Table)
        Product mainProduct = formula.getIngredients().stream()
                .map(Ingredient::getProduct)
                .filter(p -> p.getName().toLowerCase().contains("table"))
                .findFirst()
                .orElse(null);

        if (mainProduct != null) {
            String mainAttr = getAttributeValue(mainProduct, attrKey);
            String productAttr = getAttributeValue(product, attrKey);

            if (mainAttr != null && productAttr != null && !mainAttr.equals(productAttr)) {
                throw new IllegalArgumentException(
                        product.getName() + " must have the same " + attrKey + " as " + mainProduct.getName());
            }
        }
    }

    private String getAttributeValue(Product product, String key) {
        return product.getAttributes().stream()
                .filter(a -> a.getKey().equalsIgnoreCase(key))
                .map(ProductAttribute::getValue)
                .findFirst()
                .orElse(null);
    }
}
