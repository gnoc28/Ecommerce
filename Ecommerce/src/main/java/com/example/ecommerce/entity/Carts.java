package com.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Carts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cart_code", nullable = false, unique = true, length = 100)
    private String cartCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_code", referencedColumnName = "customer_code")
    private Customers customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItems> cartItems;
}
