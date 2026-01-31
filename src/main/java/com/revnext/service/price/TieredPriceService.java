package com.revnext.service.price;

import com.revnext.domain.price.Price;
import com.revnext.domain.price.TieredPrice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class TieredPriceService {

    public BigDecimal calculateTieredPrice(Price price, int quantity) {
        log.info("Calculating tiered price for product: {}, quantity: {}", price.getProduct().getName(), quantity);

        // Find the valid tiered price based on quantity
        // Assuming Price -> PriceValidity -> TieredPrice
        return price.getValidities().stream()
                .flatMap(v -> v.getTiers().stream())
                .filter(t -> t.getMinQuantity() <= quantity
                        && (t.getMaxQuantity() == null || t.getMaxQuantity() >= quantity))
                .findFirst()
                .map(TieredPrice::getUnitPrice)
                .orElse(price.getTargetPrice()); // Fallback to target price if no tier matches
    }
}
