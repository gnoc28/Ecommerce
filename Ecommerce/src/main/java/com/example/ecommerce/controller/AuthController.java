package com.example.ecommerce.controller;

import com.example.ecommerce.DTO.request.LoginRequest;
import com.example.ecommerce.DTO.request.RegisterRequest;
import com.example.ecommerce.DTO.response.LoginResponse;
import com.example.ecommerce.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final CustomerService service;

    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody @Valid RegisterRequest request){
        service.createdCustomer(request);
        return ResponseEntity.status(HttpStatus.OK).body("Đăng kí thành công");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginCustomer(@RequestBody @Valid LoginRequest request){
        LoginResponse response = service.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
