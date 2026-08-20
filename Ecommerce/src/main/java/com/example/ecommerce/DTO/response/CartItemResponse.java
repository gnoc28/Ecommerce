package com.example.ecommerce.DTO.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {
    private Long id;
    private String name;
    private Integer quantity;
    private BigDecimal price;
    private String imageUrl;
    private BigDecimal subtotal;
}
