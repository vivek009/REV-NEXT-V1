package com.revnext.domain.quote;

import com.revnext.domain.catalog.Product;
import com.revnext.domain.catalog.Suite;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.GenerationType;

@Entity(name = "quote_line")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuoteLine {

    @Id
    @Column(name = "quote_Line_Id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "quote_id", nullable = false, insertable = false, updatable = false)
    private Quote quote;

    // TODO handle at application level that either product or suite should have a
    // value

    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    @OneToOne
    @JoinColumn(name = "suite_id", insertable = false, updatable = false)
    private Suite suite;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "line_amount", nullable = false)
    private BigDecimal lineAmount;

    @Column(name = "discount_amount")
    private BigDecimal discountAmount;

    @Column(name = "tax_amount")
    private BigDecimal taxAmount;

    @Column(name = "net_amount")
    private BigDecimal netAmount;

    @Column(name = "notes")
    private String notes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Getters and setters
}
