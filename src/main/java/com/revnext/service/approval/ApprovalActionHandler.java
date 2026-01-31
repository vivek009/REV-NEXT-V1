package com.revnext.service.approval;

import com.revnext.domain.approval.ApprovalStatus;
import java.util.UUID;

public interface ApprovalActionHandler {
    String getEntityType();

    void handleApproval(UUID entityId, ApprovalStatus finalStatus);
}
