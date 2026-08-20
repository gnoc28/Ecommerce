package com.example.ecommerce.service.impl;

import com.example.ecommerce.DAO.CategoryDAO;
import com.example.ecommerce.DAO.InventoryDAO;
import com.example.ecommerce.DAO.ProductDAO;
import com.example.ecommerce.DTO.request.CreateProductRequest;
import com.example.ecommerce.DTO.request.ProductsRequest;
import com.example.ecommerce.DTO.request.UpdateProductRequest;
import com.example.ecommerce.DTO.response.*;
import com.example.ecommerce.entity.Inventories;
import com.example.ecommerce.entity.Products;
import com.example.ecommerce.enums.ProductStatus;
import com.example.ecommerce.exception.AppException;
import com.example.ecommerce.mapper.ProductMapper;
import com.example.ecommerce.security.CustomerUserDetails;
import com.example.ecommerce.service.CloudinaryService;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.utils.PageUtils;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductDAO productDAO;
    private final InventoryDAO inventoryDAO;
    private final CategoryDAO categoryDAO;
    private final ProductMapper mapper;
    private final CloudinaryService cloudinaryService;

    @Override
    public void createdProduct(CreateProductRequest request){

        categoryDAO.findByCategoryCode(request.getCategoryCode())
                .orElseThrow(() -> new AppException.NotFoundException("Không tìm thấy danh mục"));

        String imageUrl = cloudinaryService.upload( request.getImage());

        long count = productDAO.countByCategoryCode(request.getCategoryCode());

        String productCode = request.getCategoryCode() + String.format("%04d", count + 1);

        Products product = Products.builder()
                .productCode(productCode)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .imageUrl(imageUrl)
                .categoryCode(request.getCategoryCode())
                .status(request.getStatus())
                .build();

        productDAO.save(product);

        Inventories inventory = Inventories.builder()
                .inventoryCode("INV" + productCode)
                .stock(request.getQuantity() != null ? request.getQuantity() : 0)
                .product(product)
                .build();

        inventoryDAO.save(inventory);
    }

    @Override
    @Transactional
    public void updateProduct(String productCode, UpdateProductRequest request){
        Products product = productDAO.findByProductCode(productCode)
                .orElseThrow(() -> new AppException.NotFoundException("Không tìm thấy sản phẩm"));

        mapper.updateProductFromDto(request, product);

        if(request.getImage() != null){
            String newImageUrl = cloudinaryService.upload(request.getImage());
            product.setImageUrl(newImageUrl);
        }

        productDAO.save(product);
    }

    @Override
    public void deleteProduct(String productCode){
        Products product = productDAO.findByProductCode(productCode)
                .orElseThrow(() -> new AppException.NotFoundException("Không tìm thấy sản phẩm"));
        productDAO.deleteByProductCode(productCode);
    }

    @Override
    public PageResponse<?> getProducts(ProductsRequest request, int page, int size) {
        boolean isAdmin = CustomerUserDetails.isAdmin();
        Pageable pageable = PageRequest.of(page, size);

        ProductStatus targetStatus = isAdmin ? request.getStatus() : ProductStatus.ACTIVE;

        Page<Products> products = productDAO.getProducts(
                request.getKeyword(),
                request.getCategoryCode(),
                targetStatus,
                pageable
        );

        if (isAdmin) {
            return PageUtils.toPageResponse(products.map(mapper::toProductAdminResponse));
        }
        return PageUtils.toPageResponse(products.map(mapper::toProductCustomerResponse));
    }

    @Override
    public Object getProductDetail(String productCode) {
        Products product = productDAO.findByProductCode(productCode)
                .orElseThrow(() -> new AppException.NotFoundException("Không tìm thấy sản phẩm"));

        if (CustomerUserDetails.isAdmin()) {
            return mapper.toProductAdminDetailResponse(product);
        }

        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new AppException.NotFoundException("Không tìm thấy sản phẩm");
        }

        return mapper.toProductCustomerDetailResponse(product);
    }
}
