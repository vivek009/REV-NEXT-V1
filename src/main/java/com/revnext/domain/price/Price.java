package com.revnext.domain.price;

import com.revnext.domain.catalog.Product;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.GenerationType;

@Entity(name = "PRICE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Price {

    @Id
    @Column(name = "price_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "ceil_price", nullable = false)
    private BigDecimal ceilPrice;

    @Column(name = "floor_price", nullable = false)
    private BigDecimal floorPrice;

    @Column(name = "target_price", nullable = false)
    private BigDecimal targetPrice;

    // one product may have multiple prices (different validity rules)
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // 👇 link to multiple validity conditions
    @Builder.Default
    @OneToMany(mappedBy = "price", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PriceValidity> validities = new ArrayList<>();
}
