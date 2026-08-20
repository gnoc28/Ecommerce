package com.example.ecommerce.DTO.response;

import com.example.ecommerce.enums.OrderStatus;
import com.example.ecommerce.enums.PaymentMethod;
import com.example.ecommerce.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private String orderCode;
    private OrderStatus status;
    private LocalDateTime createdAt;

    private String customerName;
    private String phone;
    private String shippingAddress;

    private BigDecimal subtotal;
    private BigDecimal shippingCost;
    private BigDecimal totalAmount;

    private List<OrderItemResponse> items;
}
