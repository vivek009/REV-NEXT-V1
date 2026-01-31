package com.revnext.domain.catalog;

import com.revnext.domain.BaseData;
import com.revnext.domain.approval.Approvable;
import com.revnext.domain.approval.ApprovalStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

@Entity(name = "PRODUCT")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseData implements Approvable {

    @Id
    @Column(name = "product_id")
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
        return "PRODUCT";
    }

    @Override
    public void setApprovalStatus(ApprovalStatus status) {
        this.approvalStatus = status;
    }

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "sku")
    private String sku;

    @ManyToOne
    @JoinColumn(name = "product_family_id")
    private ProductFamily productFamily;

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductAttribute> attributes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Image> images = new ArrayList<>();

}
