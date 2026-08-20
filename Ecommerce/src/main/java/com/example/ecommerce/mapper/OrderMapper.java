package com.example.ecommerce.mapper;

import com.example.ecommerce.DTO.response.OrderItemResponse;
import com.example.ecommerce.DTO.response.OrderResponse;
import com.example.ecommerce.entity.Customers;
import com.example.ecommerce.entity.OrderItems;
import com.example.ecommerce.entity.Orders;
import com.example.ecommerce.entity.Products;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;


@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "productCode", source = "item.productCode")
    @Mapping(target = "productName", source = "item.product.name")
    @Mapping(target = "productImageUrl", source = "item.product.imageUrl")
    @Mapping(target = "unitPrice", source = "item.unitPrice")
    @Mapping(target = "quantity", source = "item.quantity")
    @Mapping(target = "subtotal", expression = "java(calculateItemSubtotal(item))")
    OrderItemResponse toOrderItemResponse(OrderItems item);

    @Mapping(target = "customerName", source = "customer.name")
    @Mapping(target = "phone", source = "customer.phone")
    @Mapping(target = "items", source = "order.items")
    @Mapping(target = "subtotal", expression = "java(calculateOrderSubtotal(order.getItems()))")
    @Mapping(target = "totalAmount", expression = "java(calculateOrderTotalAmount(order))")
    @Mapping(target = "status", source = "order.status")
    @Mapping(target = "createdAt", source = "order.createdAt")
    OrderResponse toOrderResponse(Orders order, Customers customer);

    default BigDecimal calculateItemSubtotal(OrderItems item) {
        if (item == null || item.getUnitPrice() == null || item.getQuantity() == null) return BigDecimal.ZERO;
        return item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
    }

    default BigDecimal calculateOrderSubtotal(List<OrderItems> items) {
        if (items == null || items.isEmpty()) return BigDecimal.ZERO;
        return items.stream()
                .map(this::calculateItemSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    default BigDecimal calculateOrderTotalAmount(Orders order) {
        if (order == null) return BigDecimal.ZERO;
        BigDecimal subtotal = calculateOrderSubtotal(order.getItems());
        BigDecimal shippingCost = order.getShippingCost() != null ? order.getShippingCost() : BigDecimal.ZERO;
        return subtotal.add(shippingCost);
    }
}
