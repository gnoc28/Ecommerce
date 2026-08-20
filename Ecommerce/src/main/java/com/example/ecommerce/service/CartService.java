package com.example.ecommerce.service;

import com.example.ecommerce.DTO.request.CartItemRequest;
import com.example.ecommerce.DTO.request.DeleteCartRequest;
import com.example.ecommerce.DTO.request.UpdateCartRequest;
import com.example.ecommerce.DTO.response.CartResponse;
import org.hibernate.sql.Update;


public interface CartService {
    CartResponse getCart();
    CartResponse deleteCart(DeleteCartRequest request);
    CartResponse addToCart(CartItemRequest request);
    CartResponse updateCart(Long cartItemId, UpdateCartRequest request);
}
