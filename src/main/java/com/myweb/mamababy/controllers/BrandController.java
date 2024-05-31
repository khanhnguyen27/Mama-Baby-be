package com.myweb.mamababy.controllers;

import com.myweb.mamababy.dtos.BrandDTO;
import com.myweb.mamababy.models.Brand;
import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.services.Brand.IBrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/brands")
@RequiredArgsConstructor
public class BrandController {

    private final IBrandService brandService;

    @PostMapping("")
    //Nếu tham số truyền vào là 1 object thì sao ? => Data Transfer Object = Request Object
    public ResponseEntity<?> createBrand(
            @Valid @RequestBody BrandDTO brandDTO,
            BindingResult result) {
        if(result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        Brand brand  = brandService.createBrand(brandDTO);
        return ResponseEntity.ok(brand);
    }

    //Hiện tất cả các categories
    @GetMapping("")
    public ResponseEntity<?> getAllBrands(
            @RequestParam(defaultValue = "0",name = "page")     int page,
            @RequestParam(defaultValue = "12",name = "limit")    int limit
    ) {
        List<Brand> brands = brandService.getAllBrands();
        return ResponseEntity.ok(ResponseObject
                .builder()
                .message("Get list brands successfully !!!")
                .status(HttpStatus.OK)
                .data(brands)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Brand> updateBrand(
            @PathVariable int id,
            @Valid @RequestBody BrandDTO brandDTO
    ) {
        Brand updateBrand = brandService.updateBrand(id, brandDTO);
        return ResponseEntity.ok(updateBrand);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable int id) {
        Brand deleteBrand = brandService.deleteBrand(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Delete successfully !!!")
                .status(HttpStatus.OK)
                .data(deleteBrand)
                .build());
    }

}
