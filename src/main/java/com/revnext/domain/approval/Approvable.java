package com.revnext.domain.approval;

import java.util.UUID;

public interface Approvable {
    UUID getEntityId();

    String getEntityType();

    void setApprovalStatus(ApprovalStatus status);
}
