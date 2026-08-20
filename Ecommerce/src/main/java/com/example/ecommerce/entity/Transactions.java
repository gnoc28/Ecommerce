package com.example.ecommerce.entity;

import com.example.ecommerce.enums.TransactionStatus;
import com.example.ecommerce.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transactions_code", nullable = false, unique = true, length = 100)
    private String transactionsCode;

    @Column(name = "payment_code", nullable = false, length = 100)
    private String paymentCode;

    @Column(name = "reference_code", nullable = false, unique = true, length = 100)
    private String referenceCode;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType = TransactionType.IN;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status = TransactionStatus.SUCCESS;

    @Column(name = "payment_note", length = 255)
    private String paymentNote;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
