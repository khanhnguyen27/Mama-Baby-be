package com.myweb.mamababy.services.Category;

import com.myweb.mamababy.dtos.CategoryDTO;
import com.myweb.mamababy.models.Category;
import com.myweb.mamababy.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.data.domain.Sort;
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
                .isActive(true)
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
        return categoryRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    @Transactional
    public Category updateCategory(int categoryId, CategoryDTO categoryDTO)
    {
        Category existingCategory = getCategoryById(categoryId);
        existingCategory.setName(categoryDTO.getName());
        existingCategory.setActive(categoryDTO.isActive());
        categoryRepository.save(existingCategory);
        return existingCategory;
    }

    @Override
    @Transactional
    public Category deleteCategory(int id)
    {
        Category existingCategory = getCategoryById(id);
        existingCategory.setActive(false);
        categoryRepository.save(existingCategory);
        return existingCategory;
    }

    @Override
    public List<Category> findByIsActiveTrue() {
        return categoryRepository.findAllActiveCategories();
    }
}
