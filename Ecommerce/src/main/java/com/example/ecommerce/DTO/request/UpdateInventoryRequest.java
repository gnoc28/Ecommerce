package com.example.ecommerce.DTO.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateInventoryRequest {

    @NotNull(message = "Số lượng không được để trống")
    private Integer deltaStock;
    private Integer deltaReservedStock;

}
