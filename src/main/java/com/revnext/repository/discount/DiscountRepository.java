package com.revnext.repository.discount;

import com.revnext.domain.discount.Discount;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount, UUID> {
}
