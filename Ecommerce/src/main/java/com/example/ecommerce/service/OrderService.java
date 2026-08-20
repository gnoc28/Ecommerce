package com.example.ecommerce.service;

import com.example.ecommerce.DTO.request.CreateOrderRequest;
import com.example.ecommerce.DTO.request.UpdateOrderStatusRequest;
import com.example.ecommerce.DTO.response.OrderResponse;
import com.example.ecommerce.DTO.response.OrderStatisticsResponse;
import com.example.ecommerce.DTO.response.PageResponse;
import com.example.ecommerce.enums.OrderStatus;

public interface OrderService {
     OrderResponse createOrder(CreateOrderRequest request);
     PageResponse<OrderResponse> getOrders(int page, int size, OrderStatus status);
     OrderResponse getOrderDetail(String orderCode);
     OrderResponse updateOrderStatus(String orderCode, UpdateOrderStatusRequest request);
     OrderStatisticsResponse getOrderStatistics();
}
