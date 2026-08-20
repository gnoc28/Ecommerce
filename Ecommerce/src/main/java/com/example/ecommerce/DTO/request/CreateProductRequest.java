package com.example.ecommerce.DTO.request;

import com.example.ecommerce.enums.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class CreateProductRequest {
    @NotBlank(message = "Tên không được để trống")
    private String name;

    private String description;

    @NotBlank(message = "Giá không được để trống")
    private BigDecimal price;

    private String categoryCode;

    @NotBlank(message = "Ảnh ko được để trống")
    private MultipartFile image;

    private ProductStatus status;

    private Integer quantity;
}
