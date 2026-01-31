package com.revnext.domain.price;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.GenerationType;

@Entity(name = "PRICE_VALIDITY")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceValidity {

    @Id
    @Column(name = "validity_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "location")
    private String location;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "valid_from", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date validFrom;

    @Column(name = "valid_till", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date validTill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "price_id", nullable = false)
    private Price price;

    @Builder.Default
    @OneToMany(mappedBy = "priceValidity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("minQuantity ASC")
    private List<TieredPrice> tiers = new ArrayList<>();

    // Convenience helpers to keep the bidirectional association in sync
    public void addTier(TieredPrice tier) {
        tiers.add(tier);
        tier.setPriceValidity(this);
    }

    public void removeTier(TieredPrice tier) {
        tiers.remove(tier);
        tier.setPriceValidity(null);
    }
}
