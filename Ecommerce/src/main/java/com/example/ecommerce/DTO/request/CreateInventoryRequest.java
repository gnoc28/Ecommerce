package com.example.ecommerce.DTO.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateInventoryRequest {

    @NotBlank(message = "Mã sản phẩm không được để trống")
    private String productCode;
    @Min(value = 0, message = "Số lượng kho không được nhỏ hơn 0")
    private Integer stock;
}
