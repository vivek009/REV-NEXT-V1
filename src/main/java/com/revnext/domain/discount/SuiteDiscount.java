package com.revnext.domain.discount;

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
import jakarta.persistence.GenerationType;

@Entity(name = "SUITE_DISCOUNT")
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = { "discount_id", "suite_id" })
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuiteDiscount {

    @Id
    @Column(name = "suite_dis_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;

    @OneToOne
    @JoinColumn(name = "suite_id")
    private Suite suite;

}
