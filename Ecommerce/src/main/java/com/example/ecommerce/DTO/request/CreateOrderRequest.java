package com.example.ecommerce.DTO.request;

import com.example.ecommerce.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {

    @NotBlank(message = "Địa chỉ nhận hàng không được để trống")
    private String shippingAddress;

    private List<OrderItemRequest> items;
}
