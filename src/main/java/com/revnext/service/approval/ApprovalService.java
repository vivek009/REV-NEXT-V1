package com.revnext.service.approval;

import com.revnext.domain.approval.Approvable;
import com.revnext.domain.approval.ApprovalRequest;
import com.revnext.domain.approval.ApprovalStatus;
import com.revnext.domain.approval.ApprovalWorkflowConfig;
import com.revnext.domain.approval.EntityStatus;
import com.revnext.repository.approval.ApprovalRequestRepository;
import com.revnext.repository.approval.ApprovalWorkflowConfigRepository;
import com.revnext.repository.approval.EntityStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApprovalService {

    private final ApprovalRequestRepository requestRepository;
    private final ApprovalWorkflowConfigRepository configRepository;
    private final EntityStatusRepository entityStatusRepository;
    private final List<ApprovalActionHandler> handlers;

    @Transactional
    public ApprovalRequest submitForApproval(Approvable entity, String requester) {
        log.info("Submitting {} with ID {} for approval", entity.getEntityType(), entity.getEntityId());

        if (configRepository.findByEntityType(entity.getEntityType()).isEmpty()) {
            throw new IllegalArgumentException("No workflow configured for " + entity.getEntityType());
        }

        ApprovalRequest request = ApprovalRequest.builder()
                .entityId(entity.getEntityId())
                .entityType(entity.getEntityType())
                .status(ApprovalStatus.PENDING_APPROVAL)
                .currentLevel(1)
                .requestedBy(requester)
                .build();

        updateEntityStatus(entity.getEntityId(), entity.getEntityType(), ApprovalStatus.PENDING_APPROVAL);
        return requestRepository.save(request);
    }

    private void updateEntityStatus(UUID entityId, String entityType, ApprovalStatus status) {
        EntityStatus entityStatus = entityStatusRepository.findByEntityIdAndEntityType(entityId, entityType)
                .orElse(EntityStatus.builder()
                        .entityId(entityId)
                        .entityType(entityType)
                        .build());

        entityStatus.setStatus(status);
        entityStatus.setLastUpdated(Instant.now());
        entityStatusRepository.save(entityStatus);
    }

    @Transactional
    public void processApproval(UUID requestId, boolean approved, String comment) {
        ApprovalRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Approval request not found"));

        ApprovalWorkflowConfig config = configRepository.findByEntityType(request.getEntityType())
                .orElseThrow(() -> new IllegalArgumentException("Workflow config not found"));

        if (approved) {
            if (request.getCurrentLevel() >= config.getTotalLevels()) {
                request.setStatus(ApprovalStatus.APPROVED);
                log.info("Final approval received for {} with ID {}", request.getEntityType(), request.getEntityId());
                updateEntityStatus(request.getEntityId(), request.getEntityType(), ApprovalStatus.APPROVED);
                notifyHandlers(request.getEntityId(), request.getEntityType(), ApprovalStatus.APPROVED);
            } else {
                request.setCurrentLevel(request.getCurrentLevel() + 1);
                log.info("Level {} approval received for {} with ID {}", request.getCurrentLevel() - 1,
                        request.getEntityType(), request.getEntityId());
            }
        } else {
            request.setStatus(ApprovalStatus.REJECTED);
            log.warn("Rejection received for {} with ID {}", request.getEntityType(), request.getEntityId());
            updateEntityStatus(request.getEntityId(), request.getEntityType(), ApprovalStatus.REJECTED);
            notifyHandlers(request.getEntityId(), request.getEntityType(), ApprovalStatus.REJECTED);
        }

        request.setComments(comment);
        requestRepository.save(request);
    }

    private void notifyHandlers(UUID entityId, String entityType, ApprovalStatus status) {
        handlers.stream()
                .filter(h -> h.getEntityType().equals(entityType))
                .forEach(h -> h.handleApproval(entityId, status));
    }
}
