package com.example.ecommerce.service.impl;

import com.example.ecommerce.DAO.OrderDAO;
import com.example.ecommerce.DAO.PaymentDAO;
import com.example.ecommerce.DAO.TransactionDAO;
import com.example.ecommerce.DTO.request.CreatePaymentUrlRequest;
import com.example.ecommerce.entity.Orders;
import com.example.ecommerce.entity.Payments;
import com.example.ecommerce.entity.Transactions;
import com.example.ecommerce.enums.*;
import com.example.ecommerce.exception.AppException;
import com.example.ecommerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
import vn.payos.model.webhooks.Webhook;
import vn.payos.model.webhooks.WebhookData;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderDAO orderDAO;
    private final PaymentDAO paymentDAO;
    private final TransactionDAO transactionDAO;
    private final PayOS payOS;

    @Override
    @Transactional
    public CreatePaymentLinkResponse createPaymentLink(CreatePaymentUrlRequest request) throws Exception{
        Orders order = orderDAO.findByOrderCode(request.getOrderCode())
                .orElseThrow(() -> new AppException.NotFoundException("Không tìm thấy đơn hàng"));

        BigDecimal itemTotal = (order.getItems() != null) ? order.getItems().stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add) : BigDecimal.ZERO;

        BigDecimal shippingCost = (order.getShippingCost() != null) ? order.getShippingCost() : BigDecimal.ZERO;
        BigDecimal totalAmount = itemTotal.add(shippingCost);

        Payments payment = paymentDAO.findByOrderCode(request.getOrderCode())
                .orElseGet(() -> Payments.builder()
                        .orderCode(request.getOrderCode())
                        .paymentCode("PAY" + UUID.randomUUID().toString().substring(0,8))
                        .paymentMethod(PaymentMethod.BANK_TRANSFER)
                        .status(PaymentStatus.PENDING)
                        .build());

        payment.setAmount(totalAmount);
        payment.setStatus(PaymentStatus.PENDING);
        paymentDAO.save(payment);

        order.setStatus(OrderStatus.PENDING_PAYMENT);
        orderDAO.save(order);

        CreatePaymentLinkRequest paymentRequest = CreatePaymentLinkRequest.builder()
                .orderCode(order.getId())
                .amount(totalAmount.longValue())
                .description(order.getOrderCode())
                .returnUrl(request.getReturnUrl())
                .cancelUrl(request.getCancelUrl())
                .build();

        try {
            return payOS.paymentRequests().create(paymentRequest);
        } catch (Exception e) {
            log.error("Lỗi khi gọi cổng payOS: ", e);
            throw new AppException.BadRequestException("Không thể tạo link thanh toán payOS: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Map<String, Object> handlePayOSWebhook(Webhook webhookBody) {
        try {

            WebhookData data = payOS.webhooks().verify(webhookBody);
            Long orderId = data.getOrderCode();
            log.info("Xác thực Webhook thành công cho orderId: {}", data.getOrderCode());

            var orderOpt = orderDAO.findByOrderId(orderId);
            if (orderOpt.isEmpty()) {
                log.warn("Webhook test hoặc không tìm thấy đơn hàng ID: {} trong DB.", orderId);
                return Map.of("error", 0, "message", "Webhook verified (Test ignored)");
            }

            Orders orders = orderOpt.get();

            // Chống xử lý trùng lặp (Idempotency) nếu payOS gửi lại webhook
            if (orders.getStatus() == OrderStatus.PAID) {
                log.info("Đơn hàng {} đã được thanh toán trước đó.", orders.getOrderCode());
                return Map.of("error", 0, "message", "Order already paid");
            }

            var paymentOpt = paymentDAO.findByOrderCode(orders.getOrderCode());
            if (paymentOpt.isEmpty()) {
                log.warn("Không tìm thấy thanh toán cho đơn: {}", orders.getOrderCode());
                return Map.of("error", 0, "message", "Payment record not found");
            }

            Payments payment = paymentOpt.get();

            if ("00".equals(data.getCode())) {
                payment.setStatus(PaymentStatus.SUCCESS);
                payment.setPaidAt(LocalDateTime.now());
                payment.setReceiveAccount(data.getAccountNumber());
                payment.setNote(data.getDescription());
                paymentDAO.save(payment);

                orders.setStatus(OrderStatus.PAID);
                orderDAO.save(orders);

                Transactions transaction = Transactions.builder()
                        .transactionsCode("GD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                        .paymentCode(payment.getPaymentCode())
                        .referenceCode(data.getReference())
                        .amount(BigDecimal.valueOf(data.getAmount()))
                        .transactionType(TransactionType.IN)
                        .status(TransactionStatus.SUCCESS)
                        .paymentNote(data.getDescription())
                        .build();
                transactionDAO.save(transaction);

                log.info("Đơn hàng {} thanh toán thành công", orders.getOrderCode());
            } else {
                payment.setStatus(PaymentStatus.FAILED);
                paymentDAO.save(payment);
                log.warn("Giao dịch payOS thất bại cho đơn {}", orders.getOrderCode());
            }

            return Map.of("error", 0, "message", "Webhook processed successfully");

        } catch (Exception e) {
            log.error("Lỗi xác thực hoặc xử lý Webhook: ", e);
            return Map.of("error", -1, "message", e.getMessage());
        }
    }
}
