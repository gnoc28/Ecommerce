package com.example.ecommerce.DTO.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCustomerResponse {
    private String productCode;
    private String name;
    private String categoryCode;
    private BigDecimal price;
    private String imageUrl;
    private Integer quantity;
    private Boolean outOfStock;
}
