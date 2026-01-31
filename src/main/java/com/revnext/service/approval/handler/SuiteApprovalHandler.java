package com.revnext.service.approval.handler;

import com.revnext.domain.approval.ApprovalStatus;
import com.revnext.domain.catalog.Suite;
import com.revnext.repository.catalog.SuiteRepository;
import com.revnext.service.approval.ApprovalActionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SuiteApprovalHandler implements ApprovalActionHandler {

    private final SuiteRepository suiteRepository;

    @Override
    public String getEntityType() {
        return "SUITE";
    }

    @Override
    public void handleApproval(UUID entityId, ApprovalStatus finalStatus) {
        Suite suite = suiteRepository.findById(entityId)
                .orElseThrow(() -> new IllegalArgumentException("Suite not found"));
        suite.setApprovalStatus(finalStatus);
        suiteRepository.save(suite);
    }
}
