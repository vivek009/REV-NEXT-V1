package com.revnext.domain.approval;

import com.revnext.domain.BaseData;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity(name = "APPROVAL_WORKFLOW_CONFIG")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalWorkflowConfig extends BaseData {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "entity_type", nullable = false, unique = true)
    private String entityType;

    @Column(name = "total_levels", nullable = false)
    private Integer totalLevels;

    // Roles could be stored as a comma-separated string for simplicity in this MVP,
    // or link to a separate table for cleaner design.
    @Column(name = "required_roles")
    private String requiredRoles;
}
