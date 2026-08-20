package com.example.ecommerce.service.impl;

import com.example.ecommerce.DAO.CartItemsDAO;
import com.example.ecommerce.DAO.CustomerDAO;
import com.example.ecommerce.DAO.OrderDAO;
import com.example.ecommerce.DAO.ProductDAO;
import com.example.ecommerce.DTO.request.CreateOrderRequest;
import com.example.ecommerce.DTO.request.OrderItemRequest;
import com.example.ecommerce.DTO.request.UpdateInventoryRequest;
import com.example.ecommerce.DTO.request.UpdateOrderStatusRequest;
import com.example.ecommerce.DTO.response.OrderResponse;
import com.example.ecommerce.DTO.response.OrderStatisticsResponse;
import com.example.ecommerce.DTO.response.PageResponse;
import com.example.ecommerce.entity.Customers;
import com.example.ecommerce.entity.OrderItems;
import com.example.ecommerce.entity.Orders;
import com.example.ecommerce.entity.Products;
import com.example.ecommerce.enums.OrderStatus;
import com.example.ecommerce.enums.ProductStatus;
import com.example.ecommerce.exception.AppException;
import com.example.ecommerce.mapper.OrderMapper;
import com.example.ecommerce.security.CustomerUserDetails;
import com.example.ecommerce.service.InventoryService;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    final private OrderDAO orderDAO;
    final private ProductDAO productDAO;
    final private InventoryService inventoryService;
    final private CustomerDAO customerDAO;
    final private CartItemsDAO cartItemsDAO;
    final private OrderMapper mapper;

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        Customers customer = customerDAO.findByUsername(CustomerUserDetails.getCurrentUsername())
                .orElseThrow(() -> new AppException.NotFoundException("Không tìm thấy người dùng"));

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new AppException.BadRequestException("Danh sách sản phẩm mua không được để trống!");
        }
        List<String> purchaseProductCodes = request.getItems().stream()
                .map(OrderItemRequest::getProductCode)
                .toList();

        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String random = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        String orderCode = "ORD" + date + random;

        List<OrderItems> orderItemsList = new ArrayList<>();

        for (OrderItemRequest item : request.getItems()) {
            Products product = productDAO.findByProductCode(item.getProductCode())
                    .orElseThrow(() -> new AppException.NotFoundException("Không tìm thấy sản phẩm"));

            if (product.getStatus() != ProductStatus.ACTIVE) {
                throw new AppException.BadRequestException("Sản phẩm " + product.getName() + " hiện ngừng kinh doanh!");
            }

            inventoryService.updateInventory(
                    product.getProductCode(),
                    UpdateInventoryRequest.builder()
                            .deltaStock(-item.getQuantity())
                            .deltaReservedStock(item.getQuantity())
                            .build()
            );

            OrderItems orderItems = OrderItems.builder()
                    .orderCode(orderCode)
                    .productCode(item.getProductCode())
                    .quantity(item.getQuantity())
                    .unitPrice(product.getPrice())
                    .product(product)
                    .build();

            orderItemsList.add(orderItems);
        }


        cartItemsDAO.deleteItemsFromCart(CustomerUserDetails.getCurrentCustomerCode(), purchaseProductCodes);

        Orders order = Orders.builder()
                .orderCode(orderCode)
                .customerCode(CustomerUserDetails.getCurrentCustomerCode())
                .shippingAddress(request.getShippingAddress())
                .shippingCost(new BigDecimal("20000"))
                .status(OrderStatus.CREATED)
                .items(orderItemsList)
                .build();

        Orders saveOrder = orderDAO.save(order);

        return mapper.toOrderResponse(saveOrder, customer);
    }

    @Override
    @Transactional
    public PageResponse<OrderResponse> getOrders(int page, int size, OrderStatus status) {
        boolean isAdmin = CustomerUserDetails.isAdmin();
        String currentCustomerCode = CustomerUserDetails.getCurrentCustomerCode();

        Customers currentCustomer = null;
        String customerCode = null;

        if (!isAdmin) {
            currentCustomer = customerDAO.findByCustomerCode(currentCustomerCode)
                    .orElseThrow(() -> new AppException.NotFoundException("Không tìm thấy user"));
            customerCode = currentCustomer.getCustomerCode();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Orders> ordersPage = orderDAO.findOrders(customerCode, status, pageable);

        Customers finalCurrentCustomer = currentCustomer;

        Page<OrderResponse> responsePage = ordersPage.map(order -> {
            Customers targetCustomer = isAdmin
                    ? customerDAO.findByCustomerCode(order.getCustomerCode()).orElse(null)
                    : finalCurrentCustomer;

            return mapper.toOrderResponse(order, targetCustomer);
        });

        return PageUtils.toPageResponse(responsePage);
    }

    @Override
    @Transactional
    public OrderResponse getOrderDetail(String orderCode) {
        Orders order = orderDAO.findByOrderCode(orderCode)
                .orElseThrow(() -> new AppException.NotFoundException("Không tìm thấy đơn hàng: " + orderCode));

        if (!CustomerUserDetails.isAdmin() && !order.getCustomerCode().equals(CustomerUserDetails.getCurrentCustomerCode())) {
            throw new AppException.ForbiddenException("Bạn không có quyền xem đơn hàng này!");
        }

        Customers customer = customerDAO.findByCustomerCode(order.getCustomerCode())
                .orElseThrow(() -> new AppException.NotFoundException("Không tìm thấy user"));

        return mapper.toOrderResponse(order, customer);
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(String orderCode, UpdateOrderStatusRequest request) {
        Orders order = orderDAO.findByOrderCode(orderCode)
                .orElseThrow(() -> new AppException.NotFoundException("Không tìm thấy đơn hàng: " + orderCode));

        OrderStatus targetStatus = changeStatus(
                order,
                request.getStatus(),
                CustomerUserDetails.getCurrentCustomerCode(),
                CustomerUserDetails.isAdmin()
        );

        changeInventory(order, targetStatus);

        order.setStatus(targetStatus);
        Orders updatedOrder = orderDAO.save(order);

        Customers customer = customerDAO.findByCustomerCode(order.getCustomerCode())
                .orElseThrow(() -> new AppException.NotFoundException("Không tìm thấy user"));

        return mapper.toOrderResponse(updatedOrder, customer);
    }

    @Override
    @Transactional
    public OrderStatisticsResponse getOrderStatistics() {

        Page<Orders> allOrdersPage = orderDAO.findOrders(null, null, Pageable.unpaged());
        List<Orders> allOrders = allOrdersPage.getContent();

        Map<String, Long> statusCounts = new HashMap<>();
        statusCounts.put("CREATED", 0L);
        statusCounts.put("PENDING_PAYMENT", 0L);
        statusCounts.put("PAID", 0L);
        statusCounts.put("CANCELLED", 0L);

        BigDecimal totalRevenue = BigDecimal.ZERO;

        for (Orders order : allOrders) {

            String statusName = order.getStatus().name();
            statusCounts.put(statusName, statusCounts.get(statusName) + 1);

            if (order.getStatus() == OrderStatus.PAID) {
                if (order.getItems() != null) {
                    for (OrderItems item : order.getItems()) {
                        BigDecimal itemTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                        totalRevenue = totalRevenue.add(itemTotal);
                    }
                }

                if (order.getShippingCost() != null) {
                    totalRevenue = totalRevenue.add(order.getShippingCost());
                }
            }
        }

        return OrderStatisticsResponse.builder()
                .totalOrders((long) allOrders.size())
                .totalRevenue(totalRevenue)
                .statusCounts(statusCounts)
                .build();
    }

    private OrderStatus changeStatus(Orders order, OrderStatus newStatus, String currentCustomerCode, boolean isAdmin) {
        OrderStatus currentStatus = order.getStatus();

        if (currentStatus == OrderStatus.PAID || currentStatus == OrderStatus.CANCELLED) {
            throw new AppException.BadRequestException("Không thể thay đổi trạng thái của đơn hàng đã hoàn tất hoặc đã hủy");
        }

        if (currentStatus == newStatus) {
            throw new AppException.BadRequestException("Trạng thái mới không được trùng với trạng thái hiện tại");
        }

        if (!isAdmin) {
            if (!order.getCustomerCode().equals(currentCustomerCode)) {
                throw new AppException.ForbiddenException("Bạn không có quyền thao tác trên đơn hàng này");
            }

            if (newStatus != OrderStatus.CANCELLED) {
                throw new AppException.BadRequestException("Khách hàng không được phép thực hiện thao tác này");
            }
        }

        return newStatus;
    }

    private void changeInventory(Orders order, OrderStatus newStatus) {
        if (newStatus == OrderStatus.CANCELLED) {
            for (OrderItems item : order.getItems()) {
                inventoryService.updateInventory(
                        item.getProductCode(),
                        UpdateInventoryRequest.builder()
                                .deltaStock(item.getQuantity())
                                .deltaReservedStock(-item.getQuantity())
                                .build()
                );
            }
        } else if (newStatus == OrderStatus.PAID) {
            for (OrderItems item : order.getItems()) {
                inventoryService.updateInventory(
                        item.getProductCode(),
                        UpdateInventoryRequest.builder()
                                .deltaStock(0)
                                .deltaReservedStock(-item.getQuantity())
                                .build()
                );
            }
        }
    }
}