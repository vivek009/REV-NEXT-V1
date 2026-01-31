package com.revnext.domain.discount;

import com.revnext.domain.BaseData;
import com.revnext.domain.customer.CustomerSegment;
import com.revnext.domain.catalog.Product;
import com.revnext.domain.catalog.Suite;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import jakarta.persistence.GenerationType;

@Entity(name = "CUSTOMER_SEGMENT_DISCOUNT")
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = { "seg_id", "product_id", "discount_id" }),
        @UniqueConstraint(columnNames = { "seg_id", "suite_id", "discount_id" })
})
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSegmentDiscount extends BaseData {

    @Id
    @Column(name = "cust_seg_dis_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seg_id")
    private CustomerSegment customerSegment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "suite_id")
    private Suite suite;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id")
    private Discount discount;
}
