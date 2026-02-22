package com.revnext.repository.catalog;

import com.revnext.domain.approval.ApprovalStatus;
import com.revnext.domain.catalog.Suite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SuiteRepository extends JpaRepository<Suite, UUID> {
    @Query("SELECT s FROM Suite s, EntityStatus es WHERE s.id = es.entityId AND es.entityType = 'SUITE' AND es.status = :status")
    List<Suite> findByApprovalStatus(ApprovalStatus status);

    @Query(value = "SELECT * FROM suite WHERE to_tsvector('english', coalesce(name,'') || ' ' || coalesce(description,'') || ' ' || coalesce(remark,'')) @@ plainto_tsquery('english', :query)", countQuery = "SELECT count(*) FROM suite WHERE to_tsvector('english', coalesce(name,'') || ' ' || coalesce(description,'') || ' ' || coalesce(remark,'')) @@ plainto_tsquery('english', :query)", nativeQuery = true)
    Page<Suite> searchByText(@Param("query") String query, Pageable pageable);
}
