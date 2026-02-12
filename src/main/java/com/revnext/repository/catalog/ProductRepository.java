package com.revnext.repository.catalog;

import com.revnext.domain.approval.ApprovalStatus;
import com.revnext.domain.catalog.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    Optional<Product> getProductsById(UUID id);

    @Query("SELECT p FROM Product p, EntityStatus es WHERE p.id = es.entityId AND es.entityType = 'PRODUCT' AND es.status = :status")
    List<Product> findByApprovalStatus(ApprovalStatus status);

}
