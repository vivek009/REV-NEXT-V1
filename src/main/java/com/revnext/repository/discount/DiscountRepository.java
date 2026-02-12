package com.revnext.repository.discount;

import com.revnext.domain.approval.ApprovalStatus;
import com.revnext.domain.discount.Discount;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DiscountRepository extends JpaRepository<Discount, UUID> {
    @Query("SELECT d FROM Discount d, EntityStatus es WHERE d.id = es.entityId AND es.entityType = 'DISCOUNT' AND es.status = :status")
    List<Discount> findByApprovalStatus(ApprovalStatus status);
}
