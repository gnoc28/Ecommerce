package com.example.ecommerce.service.impl;

import com.example.ecommerce.DAO.InventoryDAO;
import com.example.ecommerce.DTO.request.UpdateInventoryRequest;
import com.example.ecommerce.entity.Inventories;
import com.example.ecommerce.exception.AppException;
import com.example.ecommerce.mapper.InventoryMapper;
import com.example.ecommerce.service.InventoryService;
import com.example.ecommerce.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryDAO inventoryDAO;
    private final InventoryMapper mapper;

    @Override
    public Object getInventories(String productCode, int page, int size){
        if(StringUtils.hasText(productCode)){
            Inventories inventory = inventoryDAO.findByProduct_ProductCode(productCode)
                    .orElseThrow(() -> new AppException.NotFoundException("Không tìm thấy kho của sp"));
            return mapper.toInventoryResponse(inventory);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Inventories> inventoriesPage = inventoryDAO.findAll(pageable);
        return PageUtils.toPageResponse(inventoriesPage.map(mapper::toInventoryResponse));
    }

    @Override
    @Transactional
    public void updateInventory(String productCode, UpdateInventoryRequest request){
        Inventories inventory = inventoryDAO.findByProduct_ProductCode(productCode)
                .orElseThrow(() -> new AppException.NotFoundException("Không tìm thấy kho"));

        int currentStock = (inventory.getStock() != null) ? inventory.getStock() : 0;
        int currentReserved = (inventory.getReservedStock() != null) ? inventory.getReservedStock() : 0;

        int deltaStock = (request.getDeltaStock() != null) ? request.getDeltaStock() : 0;
        int deltaReserved = (request.getDeltaReservedStock() != null) ? request.getDeltaReservedStock() : 0;

        int newStock = currentStock + deltaStock;
        int newReserved = currentReserved + deltaReserved;

        if (newStock < 0) {
            throw new AppException.BadRequestException("Sản phẩm không đủ số lượng tồn kho!");
        }

        if (newReserved < 0) {
            newReserved = 0;
        }

        inventory.setStock(newStock);
        inventory.setReservedStock(newReserved);

        inventoryDAO.save(inventory);
    };
}
