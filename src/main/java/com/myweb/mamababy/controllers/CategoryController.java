package com.myweb.mamababy.controllers;

import com.myweb.mamababy.dtos.CategoryDTO;
import com.myweb.mamababy.models.Category;
import com.myweb.mamababy.services.Category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.FieldError;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("")
    //Nếu tham số truyền vào là 1 object thì sao ? => Data Transfer Object = Request Object
    public ResponseEntity<?> createCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult result) {
        if(result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        Category category  = categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok(category);
    }

    //Hiện tất cả các categories
    @GetMapping("")
    public ResponseEntity<List<Category>> getAllCategories(
            @RequestParam(defaultValue = "0",name = "page")     int page,
            @RequestParam(defaultValue = "12",name = "limit")    int limit
    ) {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable int id,
            @Valid @RequestBody CategoryDTO categoryDTO
    ) {
        Category updateCategory = categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok(updateCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable int id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Delete successfully !!!");
    }
}
