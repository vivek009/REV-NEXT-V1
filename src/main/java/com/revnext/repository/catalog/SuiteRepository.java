package com.revnext.repository.catalog;

import com.revnext.domain.approval.ApprovalStatus;
import com.revnext.domain.catalog.Suite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SuiteRepository extends JpaRepository<Suite, UUID> {
    @Query("SELECT s FROM Suite s, EntityStatus es WHERE s.id = es.entityId AND es.entityType = 'SUITE' AND es.status = :status")
    List<Suite> findByApprovalStatus(ApprovalStatus status);
}
