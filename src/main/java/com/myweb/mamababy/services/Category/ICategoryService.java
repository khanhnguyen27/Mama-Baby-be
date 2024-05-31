package com.myweb.mamababy.services.Category;

import com.myweb.mamababy.dtos.CategoryDTO;
import com.myweb.mamababy.models.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO);
    Category getCategoryById(int id);
    List<Category> getAllCategories();
    Category updateCategory(int categoryId, CategoryDTO categoryDTO);
    Category deleteCategory(int id);
}
