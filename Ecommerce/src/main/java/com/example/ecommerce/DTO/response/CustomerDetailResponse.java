package com.example.ecommerce.DTO.response;

import com.example.ecommerce.enums.CustomerStatus;
import com.example.ecommerce.enums.RoleType;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDetailResponse {
    private String customerCode;
    private String username;
    private String name;
    private String email;
    private String phone;
    private RoleType role;
    private CustomerStatus status;
}
