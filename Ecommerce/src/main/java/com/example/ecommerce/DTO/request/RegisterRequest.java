package com.example.ecommerce.DTO.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Thiếu username")
    private String username;

    @NotBlank(message = "Thiếu email")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotBlank(message = "Thiếu password")
    @Size(min = 8, message = "Password phải 8 kí tự trở lên")
    private String password;

    @NotBlank(message = "Thiếu số điện thoại")
    private String phone;

    @NotBlank(message = "Thiếu name")
    private String name;
}
