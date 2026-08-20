package com.example.ecommerce.controller;

import com.example.ecommerce.DTO.request.UpdateInventoryRequest;
import com.example.ecommerce.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventories")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class InventoryController {

    private final InventoryService inventoryService;


    @GetMapping
    public ResponseEntity<?> getInventories(
            @RequestParam(required = false) String productCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(inventoryService.getInventories(productCode, page, size));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{productCode}")
    public ResponseEntity<String> updateInventory(
            @PathVariable String productCode,
            @Valid @RequestBody UpdateInventoryRequest request) {
        inventoryService.updateInventory(productCode, request);
        return ResponseEntity.ok("Cập nhật kho thành công");
    }

}
