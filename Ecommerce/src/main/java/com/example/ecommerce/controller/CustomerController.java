package com.example.ecommerce.controller;

import com.example.ecommerce.DTO.request.ChangePasswordRequest;
import com.example.ecommerce.DTO.request.UpdateCustomerRequest;
import com.example.ecommerce.DTO.response.CustomerDetailResponse;
import com.example.ecommerce.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(service.getCustomers(page, size));
    }


    @GetMapping("/profile")
    public ResponseEntity<CustomerDetailResponse> getProfile() {
        return ResponseEntity.ok(service.getCustomerDetail(null));
    }

    @GetMapping("/{customerCode}")
    public ResponseEntity<CustomerDetailResponse> getCustomerByCode(@PathVariable String customerCode) {
        return ResponseEntity.ok(service.getCustomerDetail(customerCode));
    }

    @PostMapping("/{customerCode}/update")
    public ResponseEntity<?> updateCustomer(@PathVariable String customerCode,
                                            @RequestBody @Valid UpdateCustomerRequest request) {
        service.updateCustomer(customerCode, request);
        return ResponseEntity.status(HttpStatus.OK).body("Cập nhật thành công");
    }

    @DeleteMapping("/{customerCode}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCustomer(@PathVariable String customerCode) {
        service.deleteCustomer(customerCode);
        return ResponseEntity.status(HttpStatus.OK).body("Xóa thành công");
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        service.changePassword(request);
        return ResponseEntity.status(HttpStatus.OK).body("Đổi mk thành công");
    }
}
