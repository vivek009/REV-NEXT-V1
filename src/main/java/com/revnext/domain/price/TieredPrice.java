package com.revnext.domain.price;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.GenerationType;

@Entity(name = "TIERED_PRICE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TieredPrice {

    @Id
    @Column(name = "tier_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "min_quantity", nullable = false)
    private Integer minQuantity;

    @Column(name = "max_quantity")
    private Integer maxQuantity; // can be null for "infinity" upper bound

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    // 👇 link back to PriceValidity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "validity_id", nullable = false)
    private PriceValidity priceValidity;
}
