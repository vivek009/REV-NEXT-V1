package com.revnext.controller.catalog.response;

import com.revnext.domain.approval.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuiteResponse {
    private UUID id;
    private String name;
    private String description;
    private UUID formulaId;
    private UUID familyId;
    private String remark;
    private ApprovalStatus approvalStatus;
}
