package com.example.ecommerce.entity;

import com.example.ecommerce.enums.CategoryStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categories extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_code", nullable = false, unique = true, length = 100)
    private String categoryCode;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    private CategoryStatus status = CategoryStatus.ACTIVE;

}
