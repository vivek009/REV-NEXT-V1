package com.revnext.domain.catalog;

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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "suite")
public class Suite extends BaseData implements Approvable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ApprovalStatus approvalStatus = ApprovalStatus.DRAFT;

    @Override
    public UUID getEntityId() {
        return this.id;
    }

    @Override
    public String getEntityType() {
        return "SUITE";
    }

    @Override
    public void setApprovalStatus(ApprovalStatus status) {
        this.approvalStatus = status;
    }

    @Column(nullable = false, unique = true, length = 150)
    private String name;

    @Column(length = 500)
    private String description;

    @ManyToOne
    @JoinColumn(name = "formula_id", referencedColumnName = "id")
    private SuiteFormula formula;

    @ManyToOne
    @JoinColumn(name = "family_id", referencedColumnName = "id")
    private ProductFamily family;
    @Column
    private String remark;

}
