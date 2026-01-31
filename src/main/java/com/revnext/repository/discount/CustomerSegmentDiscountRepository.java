package com.revnext.repository.discount;

import com.revnext.domain.discount.CustomerSegmentDiscount;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerSegmentDiscountRepository extends JpaRepository<CustomerSegmentDiscount, UUID> {
}
