package com.example.ecommerce.DTO.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequest {

    @NotBlank(message = "Mã sản phẩm không được để trống")
    private String productCode;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng mua phải lớn hơn 0")
    private Integer quantity;
}
