package com.example.ecommerce.DTO.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryTransactionRequest {

    @NotBlank(message = "Mã sản phẩm không được để trống")
    private String productCode;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng nhập/xuất phải lớn hơn 0")
    private Integer quantity;

    private String note;
}
