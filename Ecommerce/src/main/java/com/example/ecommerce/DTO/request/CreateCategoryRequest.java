package com.example.ecommerce.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCategoryRequest {
    @NotBlank(message = "Mã danh mục không được để trống")
    @Size(max = 100, message = "Mã danh mục không quá 100 ký tự")
    private String categoryCode;

    @NotBlank(message = "Tên danh mục không được để trống")
    @Size(max = 100, message = "Tên danh mục không quá 100 ký tự")
    private String name;
}
