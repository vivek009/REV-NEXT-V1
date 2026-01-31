package com.revnext.domain.discount;

import com.revnext.domain.BaseData;
import com.revnext.domain.approval.Approvable;
import com.revnext.domain.approval.ApprovalStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

@Entity(name = "DISCOUNT")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Discount extends BaseData implements Approvable {
    @Id
    @Column(name = "discount_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Builder.Default
    private ApprovalStatus approvalStatus = ApprovalStatus.DRAFT;

    @Override
    public UUID getEntityId() {
        return this.id;
    }

    @Override
    public String getEntityType() {
        return "DISCOUNT";
    }

    @Override
    public void setApprovalStatus(ApprovalStatus status) {
        this.approvalStatus = status;
    }

    @Column(name = "name", nullable = false)
    private String discountName;

    // Store the discount rule or formula as a string
    @Column(name = "discount_rule", nullable = false, columnDefinition = "TEXT")
    private String discountRule;

    @Column(name = "start_Date")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "end_Date")
    @Temporal(TemporalType.DATE)
    private Date endDate;
}
