package com.revnext.service.approval.handler;

import com.revnext.domain.approval.ApprovalStatus;
import com.revnext.service.approval.ApprovalActionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductApprovalHandler implements ApprovalActionHandler {

    @Override
    public String getEntityType() {
        return "PRODUCT";
    }

    @Override
    public void handleApproval(UUID entityId, ApprovalStatus finalStatus) {
        log.info("Finalizing approval for PRODUCT with ID {} to status {}", entityId, finalStatus);
        // Central status is already updated by ApprovalService.
        // Side effects like cache eviction or notifications can be added here.
    }
}
