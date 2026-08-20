package com.example.ecommerce.DTO.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {
    List<CartItemResponse> items;
    BigDecimal totalAmount;
}
