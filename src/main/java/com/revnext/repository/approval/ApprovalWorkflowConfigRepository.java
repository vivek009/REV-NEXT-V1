package com.revnext.repository.approval;

import com.revnext.domain.approval.ApprovalWorkflowConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApprovalWorkflowConfigRepository extends JpaRepository<ApprovalWorkflowConfig, UUID> {
    Optional<ApprovalWorkflowConfig> findByEntityType(String entityType);
}
