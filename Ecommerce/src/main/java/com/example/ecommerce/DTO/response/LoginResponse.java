package com.example.ecommerce.DTO.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private String tokenType = "Bearer";

    public LoginResponse(String token){
        this.token = token;
    }
}
