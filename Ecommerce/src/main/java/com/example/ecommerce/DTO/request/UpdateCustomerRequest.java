package com.example.ecommerce.DTO.request;

import com.example.ecommerce.enums.CustomerStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCustomerRequest {
    private String name;

    @Email(message = "Email không hợp lệ")
    private String email;

    private String phone;

    private CustomerStatus status;
}
