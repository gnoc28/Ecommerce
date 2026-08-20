package com.example.ecommerce.controller;

import com.example.ecommerce.DTO.request.CreateProductRequest;
import com.example.ecommerce.DTO.request.ProductsRequest;
import com.example.ecommerce.DTO.request.UpdateProductRequest;
import com.example.ecommerce.DTO.response.PageResponse;
import com.example.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createdProduct(@Valid  @ModelAttribute CreateProductRequest request){
        service.createdProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Tạo sản phẩm thành công");
    }

    @PutMapping(value = "/{productCode}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateProduct(@PathVariable String productCode, @ModelAttribute UpdateProductRequest request){
        service.updateProduct(productCode, request);
        return ResponseEntity.ok("Cập nhật sản phẩm thành công");
    }

    @DeleteMapping("/{productCode}")
    @PreAuthorize("hasRole('ADMIN')")
    public  ResponseEntity<?> deleteProduct(@PathVariable String productCode){
        service.deleteProduct(productCode);

        return ResponseEntity.ok("Xóa thành công sản phẩm");
    }

    @GetMapping
    public ResponseEntity<PageResponse<?>> getProducts(
            @ModelAttribute ProductsRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(service.getProducts(request, page, size));
    }

    @GetMapping("/{productCode}")
    public ResponseEntity<?> getProductDetail(@PathVariable String productCode){
        return ResponseEntity.ok(service.getProductDetail(productCode));
    }

}
