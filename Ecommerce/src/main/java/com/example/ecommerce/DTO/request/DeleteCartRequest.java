package com.example.ecommerce.DTO.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DeleteCartRequest {
    private List<Long> cartItemIds;
}
