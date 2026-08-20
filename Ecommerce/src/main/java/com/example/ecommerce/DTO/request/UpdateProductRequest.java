package com.example.ecommerce.DTO.request;

import com.example.ecommerce.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private MultipartFile image;
    private ProductStatus status;
}
