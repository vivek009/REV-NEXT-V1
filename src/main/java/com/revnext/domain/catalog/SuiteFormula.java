package com.revnext.domain.catalog;

import com.revnext.domain.BaseData;
import com.revnext.domain.approval.Approvable;
import com.revnext.domain.approval.ApprovalStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "suite_formula")
public class SuiteFormula extends BaseData implements Approvable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ApprovalStatus approvalStatus = ApprovalStatus.DRAFT;

    @Override
    public UUID getEntityId() {
        return this.id;
    }

    @Override
    public String getEntityType() {
        return "SUITE_FORMULA";
    }

    @Override
    public void setApprovalStatus(ApprovalStatus status) {
        this.approvalStatus = status;
    }

    @OneToMany(mappedBy = "formula", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Suite> suites;

    @OneToMany(mappedBy = "formula", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingredient> ingredients;

    @ManyToOne
    @JoinColumn(name = "family_id", referencedColumnName = "id", nullable = false)
    private ProductFamily family;

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
        ingredient.setFormula(this);
    }

    public void removeIngredient(Ingredient ingredient) {
        ingredients.remove(ingredient);
        ingredient.setFormula(null);
    }

    public void addSuite(Suite suite) {
        suites.add(suite);
        suite.setFormula(this);
    }

    public void removeSuite(Suite suite) {
        suites.remove(suite);
        suite.setFormula(null);
    }
}
