package com.revnext.controller.discount.response;

import com.revnext.domain.approval.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountResponse {
    private UUID id;
    private String name;
    private String rule;
    private Date startDate;
    private Date endDate;
    private ApprovalStatus approvalStatus;
}
