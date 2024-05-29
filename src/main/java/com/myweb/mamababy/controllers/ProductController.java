package com.myweb.mamababy.controllers;

import com.myweb.mamababy.dtos.CategoryDTO;
import com.myweb.mamababy.dtos.ProductDTO;
import com.myweb.mamababy.models.Category;
import com.myweb.mamababy.models.Product;
import com.myweb.mamababy.responses.product.ProductListResponse;
import com.myweb.mamababy.responses.product.ProductResponse;
import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.services.Product.IProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    //POST http://localhost:8080/mamababy/products
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(value="", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @Valid @ModelAttribute ProductDTO productDTO,
            BindingResult result,
            @RequestParam("image") MultipartFile file
            ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }

            Product newProduct = productService.createProduct(productDTO, file);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Create new product successfully")
                    .status(HttpStatus.CREATED)
                    .data(ProductResponse.fromProduct(newProduct))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //GET http://localhost:8080/mamababy/products
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("")
    public ResponseEntity<?> getProducts(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0", name = "category_id") int categoryId,
            @RequestParam(defaultValue = "0", name = "brand_id") int brandId,
            @RequestParam(defaultValue = "0", name = "age_id") int rangeAge,
            @RequestParam(defaultValue = "0", name = "store_id") int storeId,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "12", name = "limit") int limit
            ){
        int totalPages = 0;
        // Tạo Pageable từ thông tin trang và giới hạn
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                //Sort.by("createdAt").descending()
                Sort.by("id").ascending()
        );
        //Lay tat ca cac product theo yeu cau
        Page<ProductResponse> productPage = productService
                .getAllProducts(keyword, categoryId, brandId, rangeAge, storeId, pageRequest);
        // Lấy tổng số trang
        totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();

        ProductListResponse productListResponse = ProductListResponse
                .builder()
                .products(products)
                .totalPages(totalPages)
                .build();
        return ResponseEntity.ok(ResponseObject.builder()
                        .message("Get products details successfully")
                        .data(productListResponse)
                        .status(HttpStatus.OK)
                        .build());
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(
            @PathVariable("id") int productId
        ){
        try{
            Product existingProduct = productService.getProductById(productId);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Get detail product successfully")
                    .status(HttpStatus.OK)
                    .data(ProductResponse.fromProduct(existingProduct))
                    .build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id) {
        try {
            Product deleteProduct = productService.deleteProduct(id);
            return ResponseEntity.ok(ResponseObject.builder()
                    .data(ProductResponse.fromProduct(deleteProduct))
                    .message(String.format("Product with id = %d deleted successfully", id))
                    .status(HttpStatus.OK)
                    .build());

        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(
            @PathVariable int id,
            @ModelAttribute ProductDTO productDTO,
            @RequestParam("image") MultipartFile file
            ){
        try{
            Product updatedProduct = productService.updateProduct(id, productDTO, file);
            return ResponseEntity.ok(ResponseObject.builder()
                    .data(ProductResponse.fromProduct(updatedProduct))
                    .message("Update product successfully")
                    .status(HttpStatus.OK)
                    .build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}
