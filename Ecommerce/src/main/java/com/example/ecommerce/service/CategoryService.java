package com.example.ecommerce.service;

import com.example.ecommerce.DTO.request.CreateCategoryRequest;
import com.example.ecommerce.DTO.request.UpdateCategoryRequest;
import com.example.ecommerce.DTO.response.CategoryAdminResponse;
import com.example.ecommerce.DTO.response.CategoryCustomerResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryAdminResponse> findAll();
    List<CategoryCustomerResponse> findAllActive();
    void createCategory(CreateCategoryRequest request);
    void updateCategory(String categoryCode, UpdateCategoryRequest request);
    CategoryCustomerResponse getCategory(String categoryCode);
}
