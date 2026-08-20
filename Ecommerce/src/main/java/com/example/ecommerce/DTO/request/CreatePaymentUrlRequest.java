package com.example.ecommerce.DTO.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePaymentUrlRequest {
    private String orderCode;
    private String returnUrl;
    private String cancelUrl;
}
