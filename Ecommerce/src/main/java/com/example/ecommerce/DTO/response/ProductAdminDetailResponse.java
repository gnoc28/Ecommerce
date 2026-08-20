package com.example.ecommerce.DTO.response;

import com.example.ecommerce.enums.ProductStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAdminDetailResponse{
    private String productCode;
    private String name;
    private BigDecimal price;
    private String categoryCode;
    private String imageUrl;
    private Integer quantity;
    private Integer reservedStock;
    private Boolean outOfStock;
    private ProductStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String description;
}
