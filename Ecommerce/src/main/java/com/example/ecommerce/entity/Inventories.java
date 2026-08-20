package com.example.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventories extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inventory_code", nullable = false, unique = true, length = 100)
    private String inventoryCode;

    @Column(nullable = false)
    @Builder.Default
    private Integer stock = 0;

    @Column(name = "reserved_stock", nullable = false)
    @Builder.Default
    private Integer reservedStock = 0;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_code", referencedColumnName = "product_code")
    private Products product;
}
