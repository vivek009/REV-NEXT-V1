package com.revnext.repository.discount;

import com.revnext.domain.approval.ApprovalStatus;
import com.revnext.domain.discount.Discount;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DiscountRepository extends JpaRepository<Discount, UUID> {
    @Query("SELECT d FROM Discount d, EntityStatus es WHERE d.id = es.entityId AND es.entityType = 'DISCOUNT' AND es.status = :status")
    List<Discount> findByApprovalStatus(ApprovalStatus status);

    @Query(value = "SELECT * FROM DISCOUNT WHERE to_tsvector('english', coalesce(name,'') || ' ' || coalesce(discount_rule,'')) @@ plainto_tsquery('english', :query)", countQuery = "SELECT count(*) FROM DISCOUNT WHERE to_tsvector('english', coalesce(name,'') || ' ' || coalesce(discount_rule,'')) @@ plainto_tsquery('english', :query)", nativeQuery = true)
    Page<Discount> searchByText(@Param("query") String query, Pageable pageable);
}
