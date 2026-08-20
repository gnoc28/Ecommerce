package com.example.ecommerce.entity;

import com.example.ecommerce.enums.CustomerStatus;
import com.example.ecommerce.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customers extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_code", nullable = false, unique = true, length = 100)
    private String customerCode;

    @Column(unique = true, length = 50, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, unique = true, length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    private RoleType role = RoleType.CUSTOMER;

    @Enumerated(EnumType.STRING)
    private CustomerStatus status = CustomerStatus.ACTIVE;
}