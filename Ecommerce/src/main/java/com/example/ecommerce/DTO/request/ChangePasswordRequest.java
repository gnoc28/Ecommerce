package com.example.ecommerce.DTO.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {

    @NotBlank(message = "Không được để trống")
    private String oldPassword;
    @NotBlank(message = "Không được để trống")
    private String newPassword;
    @NotBlank(message = "Không được để trống")
    private String accessPassword;

}
