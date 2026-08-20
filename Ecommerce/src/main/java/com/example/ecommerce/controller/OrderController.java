package com.example.ecommerce.controller;

import com.example.ecommerce.DTO.request.CreateOrderRequest;
import com.example.ecommerce.DTO.request.ProductsRequest;
import com.example.ecommerce.DTO.request.UpdateOrderStatusRequest;
import com.example.ecommerce.DTO.response.OrderResponse;
import com.example.ecommerce.DTO.response.OrderStatisticsResponse;
import com.example.ecommerce.DTO.response.PageResponse;
import com.example.ecommerce.enums.OrderStatus;
import com.example.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createOrder(request));
    }

    @GetMapping
    public  ResponseEntity<PageResponse<OrderResponse>> getOrders(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(service.getOrders(page,size,status));
    }

    @GetMapping("/{orderCode}")
    public ResponseEntity<OrderResponse> getOrderDetail(@PathVariable String orderCode) {
        return ResponseEntity.ok(service.getOrderDetail(orderCode));
    }

    @PatchMapping("/{orderCode}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable String orderCode,
            @Valid @RequestBody UpdateOrderStatusRequest request) {
        return ResponseEntity.ok(service.updateOrderStatus(orderCode, request));
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderStatisticsResponse> getOrderStatistics() {
        return ResponseEntity.ok(service.getOrderStatistics());
    }
}
