package com.myweb.mamababy.controllers;

import com.myweb.mamababy.dtos.ProductDTO;
import com.myweb.mamababy.models.Product;
import com.myweb.mamababy.responses.product.ProductListResponse;
import com.myweb.mamababy.responses.product.ProductResponse;
import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.services.Product.IProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    //POST http://localhost:8080/mamababy/products
    @PostMapping("")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
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


            // Kiểm tra kích thước file và định dạng
            if(file.getSize() > 10 * 1024 * 1024) { // Kích thước > 10MB
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                        .body(ResponseObject.builder()
                                .message("Cannot download images >10MB !!!")
                                .status(HttpStatus.PAYLOAD_TOO_LARGE)
                                .build());
            }
            String contentType = file.getContentType();
            if(contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                        .body(ResponseObject.builder()
                                .message("The file is not suitable !!!")
                                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                                .build());
            }
            // Lưu file và cập nhật thumbnail trong DTO
            String filename = productService.storeFile(file); // Thay thế hàm này với code của bạn để lưu file

            //lưu vào đối tượng product trong DB
            productDTO.setImageUrl(filename);
            Product newProduct = productService.createProduct(productDTO);

            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Add product successfully !!!")
                    .status(HttpStatus.OK)
                    .data(newProduct));
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
}
