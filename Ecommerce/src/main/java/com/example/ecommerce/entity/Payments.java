package com.example.ecommerce.entity;

import com.example.ecommerce.enums.PaymentMethod;
import com.example.ecommerce.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payments extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_code", nullable = false, length = 100)
    private String orderCode;

    @Column(name = "payment_code", nullable = false, unique = true, length = 100)
    private String paymentCode;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod = PaymentMethod.BANK_TRANSFER;

    @Column(name = "receive_bank", length = 20)
    private String receiveBank;

    @Column(name = "receive_account", length = 50)
    private String receiveAccount;

    @Column(name = "send_bank", length = 20)
    private String sendBank;

    @Column(name = "send_account", length = 50)
    private String sendAccount;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

}