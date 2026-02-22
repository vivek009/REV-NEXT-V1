package com.revnext.repository.catalog;

import com.revnext.domain.approval.ApprovalStatus;
import com.revnext.domain.catalog.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    Optional<Product> getProductsById(UUID id);

    @Query("SELECT p FROM Product p, EntityStatus es WHERE p.id = es.entityId AND es.entityType = 'PRODUCT' AND es.status = :status")
    List<Product> findByApprovalStatus(ApprovalStatus status);

    @Query(value = "SELECT p.* FROM PRODUCT p WHERE to_tsvector('english', coalesce(p.name,'') || ' ' || coalesce(p.description,'') || ' ' || coalesce(p.sku,'')) @@ plainto_tsquery('english', :query)", countQuery = "SELECT count(*) FROM PRODUCT p WHERE to_tsvector('english', coalesce(p.name,'') || ' ' || coalesce(p.description,'') || ' ' || coalesce(p.sku,'')) @@ plainto_tsquery('english', :query)", nativeQuery = true)
    Page<Product> searchByText(@Param("query") String query, Pageable pageable);

    @Override
    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = { "productFamily" })
    Page<Product> findAll(Pageable pageable);

}
