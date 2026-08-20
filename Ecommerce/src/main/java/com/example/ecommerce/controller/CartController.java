package com.example.ecommerce.controller;

import com.example.ecommerce.DTO.request.CartItemRequest;
import com.example.ecommerce.DTO.request.DeleteCartRequest;
import com.example.ecommerce.DTO.request.UpdateCartRequest;
import com.example.ecommerce.DTO.response.CartResponse;
import com.example.ecommerce.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService service;

    @GetMapping
    public ResponseEntity<CartResponse> getCart() {
        return ResponseEntity.ok(service.getCart());
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addToCart(@Valid @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(service.addToCart(request));
    }

    @PatchMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponse> updateCart(
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartRequest request) {
        return ResponseEntity.ok(service.updateCart(cartItemId, request));
    }

    @DeleteMapping("/items/delete")
    public ResponseEntity<CartResponse> deleteCart(@RequestBody DeleteCartRequest request) {
        return ResponseEntity.ok(service.deleteCart(request));
    }

}
