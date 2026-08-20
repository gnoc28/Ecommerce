package com.example.ecommerce.controller;

import com.example.ecommerce.DTO.request.CreateCategoryRequest;
import com.example.ecommerce.DTO.request.UpdateCategoryRequest;
import com.example.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @GetMapping
    public ResponseEntity<?> getAllActive(){
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllActive());
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
    }

    @GetMapping("/{categoryCode}")
    public ResponseEntity<?> getByCode(@PathVariable String categoryCode) {
        return ResponseEntity.ok(service.getCategory(categoryCode));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        service.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Tạo danh mục thành công");
    }

    @PutMapping("/{categoryCode}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateCategory(
            @PathVariable String categoryCode,
            @Valid @RequestBody UpdateCategoryRequest request) {
        service.updateCategory(categoryCode, request);
        return ResponseEntity.ok("Cập nhật danh mục thành công");
    }
}
