package com.myweb.mamababy.controllers;

import com.myweb.mamababy.dtos.CategoryDTO;
import com.myweb.mamababy.dtos.ProductDTO;
import com.myweb.mamababy.models.Category;
import com.myweb.mamababy.models.Product;
import com.myweb.mamababy.responses.product.ProductListResponse;
import com.myweb.mamababy.responses.product.ProductResponse;
import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.services.Product.IProductService;
import com.myweb.mamababy.services.Store.IStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.UrlResource;
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

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final IProductService productService;
    private final IStoreService storeService;

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
            @RequestParam(defaultValue = "") String type,
            @RequestParam(defaultValue = "", name = "sort_price") String sort,
            @RequestParam(defaultValue = "0", name = "category_id") int categoryId,
            @RequestParam(defaultValue = "0", name = "brand_id") int brandId,
            @RequestParam(defaultValue = "0", name = "age_id") int rangeAge,
            @RequestParam(defaultValue = "0", name = "store_id") int storeId,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "12", name = "limit") int limit
            ){
        int totalPages = 0;
        PageRequest pageRequest = null;
        // Tạo Pageable từ thông tin trang và giới hạn
        if(sort.equals("DESC")){
            pageRequest = PageRequest.of(
                    page, limit,
                    Sort.by("price").descending()
            );
        }else if(sort.equals("ASC")){
            pageRequest = PageRequest.of(
                    page, limit,
                    Sort.by("price").ascending()
            );
        }else {
            pageRequest = PageRequest.of(
                    page, limit,
                    Sort.by("createdAt").descending()
            );
        }

        //Lay tat ca cac product theo yeu cau
        Page<ProductResponse> productPage = productService
                .getAllProducts(keyword, type, categoryId, brandId, rangeAge, storeId, pageRequest);
        // Lấy tổng số trang
        totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();

        List<ProductResponse> productResponsesValid = new ArrayList<>();
        for(ProductResponse productResponse : products){
            if(storeService.getStoreById(productResponse.getStoreId()).isActive()){
                productResponsesValid.add(productResponse);
            }
        }

        ProductListResponse productListResponse = ProductListResponse
                .builder()
                .products(productResponsesValid)
                .totalPages(totalPages)
                .build();
        return ResponseEntity.ok(ResponseObject.builder()
                        .message("Get products details successfully")
                        .data(productListResponse)
                        .status(HttpStatus.OK)
                        .build());
    }

    //GET http://localhost:8080/mamababy/products
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/comment_history")
    public ResponseEntity<?> getAllProducts(
            @RequestParam(defaultValue = "") String type
    ){
        //Lay tat ca cac product theo yeu cau
        List<ProductResponse> products = productService
                .getAllProductsCH(type);

        List<ProductResponse> productResponsesValid = new ArrayList<>();
        for(ProductResponse productResponse : products){
            if(storeService.getStoreById(productResponse.getStoreId()).isActive()){
                productResponsesValid.add(productResponse);
            }
        }

        ProductListResponse productListResponse = ProductListResponse
                .builder()
                .products(productResponsesValid)
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
    @GetMapping("/store")
    public ResponseEntity<?> getProductByStoreId(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "", name = "sort_price") String sort,
            @RequestParam(defaultValue = "0", name = "category_id") int categoryId,
            @RequestParam(defaultValue = "0", name = "brand_id") int brandId,
            @RequestParam(defaultValue = "0", name = "age_id") int rangeAge,
            @RequestParam(defaultValue = "0", name = "store_id") int storeId,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "12", name = "limit") int limit
    ) throws Exception {
        int totalPages = 0;
        PageRequest pageRequest = null;
        // Tạo Pageable từ thông tin trang và giới hạn
        if(sort.equals("DESC")){
            pageRequest = PageRequest.of(
                    page, limit,
                    Sort.by("price").descending()
            );
        }else if(sort.equals("ASC")){
            pageRequest = PageRequest.of(
                    page, limit,
                    Sort.by("price").ascending()
            );
        }else {
            pageRequest = PageRequest.of(
                    page, limit,
                    Sort.by("createdAt").descending()
            );
        }
        //Lay tat ca cac product theo yeu cau
        Page<ProductResponse> productPage = productService
                .getProductByStoreId(keyword, categoryId, brandId, rangeAge, storeId, pageRequest);
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

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName) {
        try {
            java.nio.file.Path imagePath = Paths.get("uploads/"+imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                logger.info(imageName + " not found");
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("uploads/notfound.jpeg").toUri()));
                //return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error occurred while retrieving image: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

}
