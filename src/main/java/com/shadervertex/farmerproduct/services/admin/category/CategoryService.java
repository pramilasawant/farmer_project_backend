package com.shadervertex.farmerproduct.services.admin.category;

import java.util.List;

import com.shadervertex.farmerproduct.dto.CategoryDto;
import com.shadervertex.farmerproduct.model.Category;

public interface CategoryService {
    Category createCategory(CategoryDto categoryDto);
    List<Category> getAllCategory();
    Category updateCategory(Long id, CategoryDto categoryDto);
    void deleteCategory(Long id);
}