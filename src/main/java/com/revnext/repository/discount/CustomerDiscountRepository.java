package com.revnext.repository.discount;

import com.revnext.domain.discount.CustomerDiscount;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerDiscountRepository extends JpaRepository<CustomerDiscount, UUID> {
}
