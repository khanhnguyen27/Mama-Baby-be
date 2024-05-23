package com.myweb.mamababy.services.Category;

import com.myweb.mamababy.dtos.CategoryDTO;
import com.myweb.mamababy.models.Category;
import com.myweb.mamababy.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Category createCategory(CategoryDTO categoryDTO)
    {
        Category newCategory = Category
                .builder()
                .name(categoryDTO.getName())
                .build();
        return categoryRepository.save(newCategory);
    }

    @Override
    public Category getCategoryById(int id)
    {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public List<Category> getAllCategories()
    {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public Category updateCategory(int categoryId, CategoryDTO categoryDTO)
    {
        Category existingCategory = getCategoryById(categoryId);
        existingCategory.setName(categoryDTO.getName());
        categoryRepository.save(existingCategory);
        return existingCategory;
    }

    @Override
    @Transactional
    public void deleteCategory(int id)
    {
        categoryRepository.deleteById(id);
    }
}
