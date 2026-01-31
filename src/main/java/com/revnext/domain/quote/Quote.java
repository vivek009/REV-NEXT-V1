package com.revnext.domain.quote;

import com.revnext.domain.customer.Customer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.GenerationType;

@Entity(name = "quote")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Quote {

    @Id
    @Column(name = "quote_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "customer_id", insertable = false, updatable = false, nullable = false)
    private Customer customer;

    // TODO DO FOLLOWING WHILE DOING USER MANAGEMENT
    /*
     * @ManyToOne
     * 
     * @JoinColumn(name = "salesperson_id", nullable = false)
     */
    private String salesperson;

    @Column(name = "quote_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate quoteDate;

    @Column(name = "expiration_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate expirationDate;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "currency", nullable = false)
    private String currency;

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
