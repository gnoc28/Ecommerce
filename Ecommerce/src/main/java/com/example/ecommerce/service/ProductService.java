package com.example.ecommerce.service;

import com.example.ecommerce.DTO.request.CreateProductRequest;
import com.example.ecommerce.DTO.request.ProductsRequest;
import com.example.ecommerce.DTO.request.UpdateProductRequest;
import com.example.ecommerce.DTO.response.PageResponse;
import com.example.ecommerce.DTO.response.ProductAdminDetailResponse;
import com.example.ecommerce.DTO.response.ProductCustomerDetailResponse;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {
    void createdProduct(CreateProductRequest request);
    void updateProduct(String productCode, UpdateProductRequest request);
    void deleteProduct(String productCode);
    PageResponse<?> getProducts(ProductsRequest request, int page, int size);
    Object getProductDetail(String productCode);
}
