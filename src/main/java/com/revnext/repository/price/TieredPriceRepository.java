package com.revnext.repository.price;


import com.revnext.domain.price.TieredPrice;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TieredPriceRepository extends JpaRepository<TieredPrice, UUID> {

    // Find tiers by validity
    List<TieredPrice> findByPriceValidityId(UUID validityId);

    // Find applicable tier for given quantity
    List<TieredPrice> findByPriceValidityIdAndMinQuantityLessThanEqualAndMaxQuantityGreaterThanEqual(
            UUID validityId, Integer minQuantity, Integer maxQuantity
    );
}
