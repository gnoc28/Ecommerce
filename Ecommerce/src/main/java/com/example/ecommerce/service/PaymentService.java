package com.example.ecommerce.service;

import com.example.ecommerce.DTO.request.CreatePaymentUrlRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
import vn.payos.model.webhooks.Webhook;

import java.util.Map;

public interface PaymentService {
    CreatePaymentLinkResponse createPaymentLink(CreatePaymentUrlRequest request) throws Exception;
    Map<String, Object> handlePayOSWebhook(Webhook webhookBody);
}
