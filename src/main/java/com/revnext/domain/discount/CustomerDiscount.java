package com.revnext.domain.discount;

import com.revnext.domain.BaseData;
import com.revnext.domain.customer.Customer;
import com.revnext.domain.catalog.Product;
import com.revnext.domain.catalog.Suite;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity(name = "CUSTOMER_DISCOUNT")
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = { "customer_id", "product_id", "discount_id" }),
        @UniqueConstraint(columnNames = { "customer_id", "suite_id", "discount_id" })
})
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDiscount extends BaseData {

    @Id
    @Column(name = "customer_discount_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne
    @JoinColumn(name = "suite_id")
    private Suite suite;

    @OneToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;
}
