package com.shadervertex.farmerproduct.services.admin.category;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.shadervertex.farmerproduct.dto.CategoryDto;
import com.shadervertex.farmerproduct.model.Category;
import com.shadervertex.farmerproduct.repository.CategoryRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(CategoryDto categoryDto) {
        return categoryRepository.save(
            Category.builder()
                .name(categoryDto.getName())
                .description(categoryDto.getDescription())
                .build()
        );
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(Long id, CategoryDto categoryDto) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);

        if (categoryOpt.isPresent()) {
            Category category = categoryOpt.get();
            category.setName(categoryDto.getName());
            category.setDescription(categoryDto.getDescription());
            return categoryRepository.save(category);
        }
        return null; 
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}