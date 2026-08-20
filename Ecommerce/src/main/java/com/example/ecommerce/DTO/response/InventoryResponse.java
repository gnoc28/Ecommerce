package com.example.ecommerce.DTO.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryResponse {
    private String inventoryCode;
    private String productCode;
    private String productName;
    private Integer stock;
    private Integer reservedStock;
    private Integer availableStock;
}
