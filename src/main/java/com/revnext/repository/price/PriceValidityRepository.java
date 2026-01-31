package com.revnext.repository.price;


import com.revnext.domain.price.PriceValidity;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceValidityRepository extends JpaRepository<PriceValidity, UUID> {

    // Find all validities for a specific price
    List<PriceValidity> findByPriceId(UUID priceId);

    // Find validities active on a given date
    List<PriceValidity> findByValidFromBeforeAndValidTillAfter(Date from, Date till);
}

