package com.revnext.repository.discount;

import com.revnext.domain.discount.SuiteDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SuiteDiscountRepository extends JpaRepository<SuiteDiscount, UUID> {
}
