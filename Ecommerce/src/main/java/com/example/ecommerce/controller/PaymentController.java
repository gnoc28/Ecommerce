package com.example.ecommerce.controller;

import com.example.ecommerce.DTO.request.CreatePaymentUrlRequest;
import com.example.ecommerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.payos.model.webhooks.Webhook;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService service;

    @PostMapping("/create-payment-link")
    public ResponseEntity<?> createPaymentLink(@RequestBody CreatePaymentUrlRequest request){
        try {
            return ResponseEntity.ok(service.createPaymentLink(request));
        } catch (Exception e){
            log.error("lỗi tạo link thanh toán", e);
            return ResponseEntity.badRequest().body(Map.of(
                    "error", -1,
                    "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> receiveWebhook(@RequestBody Webhook webhookBody) {
        return ResponseEntity.ok(service.handlePayOSWebhook(webhookBody));
    }

}
