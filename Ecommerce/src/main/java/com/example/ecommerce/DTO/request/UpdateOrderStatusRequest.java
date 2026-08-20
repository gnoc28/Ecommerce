package com.example.ecommerce.DTO.request;

import com.example.ecommerce.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderStatusRequest {
    @NotNull(message = "Trạng thái đơn hàng không được để trống")
    private OrderStatus status;
}
