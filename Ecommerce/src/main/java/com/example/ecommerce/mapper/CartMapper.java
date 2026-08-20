package com.example.ecommerce.mapper;

import com.example.ecommerce.DTO.response.CartResponse;
import com.example.ecommerce.entity.Carts;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartResponse toCartResponse(Carts cart);
}
