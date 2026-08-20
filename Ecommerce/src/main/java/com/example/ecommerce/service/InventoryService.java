package com.example.ecommerce.service;

import com.example.ecommerce.DAO.InventoryDAO;
import com.example.ecommerce.DTO.request.InventoryTransactionRequest;
import com.example.ecommerce.DTO.request.UpdateInventoryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public interface InventoryService {

    Object getInventories(String productCode, int page, int size);
    void updateInventory(String productCode, UpdateInventoryRequest request);
}
