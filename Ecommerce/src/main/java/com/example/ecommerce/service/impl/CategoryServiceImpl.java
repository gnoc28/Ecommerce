package com.example.ecommerce.service.impl;

import com.example.ecommerce.DAO.CategoryDAO;
import com.example.ecommerce.DTO.request.CreateCategoryRequest;
import com.example.ecommerce.DTO.request.UpdateCategoryRequest;
import com.example.ecommerce.DTO.response.CategoryAdminResponse;
import com.example.ecommerce.DTO.response.CategoryCustomerResponse;
import com.example.ecommerce.entity.Categories;
import com.example.ecommerce.enums.CategoryStatus;
import com.example.ecommerce.exception.AppException;
import com.example.ecommerce.mapper.CategoryMapper;
import com.example.ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryDAO dao;
    private final CategoryMapper mapper;

    @Override
    public List<CategoryAdminResponse> findAll(){
        return dao.findAll().stream()
                .map(mapper::toCategoryAdminResponse)
                .toList();
    }

    @Override
    public List<CategoryCustomerResponse> findAllActive(){
        return dao.findAll().stream()
                .filter(categories -> categories.getStatus() == CategoryStatus.ACTIVE  )
                .map(mapper ::toCategoryCustomerResponse )
                .toList();
    }

    @Override
    @Transactional
    public CategoryCustomerResponse getCategory(String categoryCode){
        Categories category = dao.findByCategoryCode(categoryCode)
                .orElseThrow(() -> new AppException.NotFoundException("Không tìm thấy danh mục"));
        return mapper.toCategoryCustomerResponse(category);
    }

    @Override
    @Transactional
    public void createCategory(CreateCategoryRequest request){
        String formatCode = request.getCategoryCode().trim().toUpperCase();

        if(dao.existsByCategoryCode(formatCode)){
            throw new AppException.DataExistsException("Mã danh mục" + request.getCategoryCode() + " đã tồn tại");
        }

        if(dao.existsByName(request.getName().trim())){
            throw new AppException.DataExistsException("Tên danh mục '" + request.getName() + "' đã tồn tại");
        }

        Categories category = Categories.builder()
                .categoryCode(formatCode)
                .name(request.getName())
                .status(CategoryStatus.ACTIVE)
                .build();
        dao.save(category);
    }

    @Override
    @Transactional
    public void updateCategory(String categoryCode, UpdateCategoryRequest request){

        Categories category = dao.findByCategoryCode(categoryCode)
                .orElseThrow(() -> new AppException.NotFoundException("Không tìm thấy danh mục"));

        String newName = request.getName().trim();
        if (!category.getName().equalsIgnoreCase(newName)) {
            if (dao.existsByName(newName)) {
                throw new AppException.DataExistsException("Tên danh mục đã tồn tại");
            }
            category.setName(newName);
        }

        category.setStatus(request.getStatus());

        dao.save(category);
    }
}
