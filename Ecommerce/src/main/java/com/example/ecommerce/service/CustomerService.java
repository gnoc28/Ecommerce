package com.example.ecommerce.service;

import com.example.ecommerce.DTO.request.ChangePasswordRequest;
import com.example.ecommerce.DTO.request.LoginRequest;
import com.example.ecommerce.DTO.request.RegisterRequest;
import com.example.ecommerce.DTO.request.UpdateCustomerRequest;
import com.example.ecommerce.DTO.response.CustomerDetailResponse;
import com.example.ecommerce.DTO.response.LoginResponse;
import com.example.ecommerce.DTO.response.PageResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {

    void createdCustomer(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    void updateCustomer(String customerCode, UpdateCustomerRequest request);

    void deleteCustomer(String customerCode);

    void changePassword(ChangePasswordRequest request);

    Object getCustomers(int page, int size);

    CustomerDetailResponse getCustomerDetail(String customerCode);
}
