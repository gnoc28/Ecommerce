package com.example.ecommerce.DTO.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Không được để trống username")
    private String username;

    @NotBlank(message = "Không được để trống password")
    private String password;
}
