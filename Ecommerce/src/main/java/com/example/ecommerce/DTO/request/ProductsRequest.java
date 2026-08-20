package com.example.ecommerce.DTO.request;

import com.example.ecommerce.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductsRequest {
    private ProductStatus status;
    private String keyword;
    private String categoryCode;
}
