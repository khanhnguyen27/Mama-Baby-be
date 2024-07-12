package com.myweb.mamababy.controllers;

import com.myweb.mamababy.dtos.StorePackageDTO;
import com.myweb.mamababy.models.StorePackage;
import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.responses.storePackage.StorePackageResponse;
import com.myweb.mamababy.services.StorePackage.IStorePackageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/store_package")
@RequiredArgsConstructor
public class StoreAPackageController {

    private final IStorePackageService storePackageService;

    @PostMapping("")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> createStorePackage(
            @Valid @RequestBody StorePackageDTO storePackageDTO,
            BindingResult result) {
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            StorePackage newStorePackage = storePackageService.createStorePackage(storePackageDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Create store package successfully!!!")
                    .status(HttpStatus.CREATED)
                    .data(StorePackageResponse.fromStorePackage(newStorePackage))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getAllStorePackage() {
        try {
            List<StorePackage> storePackages = storePackageService.getAllPackage();
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Get all store packages successfully!!!")
                    .data(storePackages.stream().map(StorePackageResponse::fromStorePackage).toList())
                    .status(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getStorePackageById(@Valid @PathVariable("id") int storePackageId) {
        try {
            StorePackage existingStorePackage = storePackageService.getStorePackageById(storePackageId);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Store package with id = " + storePackageId + " Found Successfully!!!")
                    .status(HttpStatus.OK)
                    .data(StorePackageResponse.fromStorePackage(existingStorePackage))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/store/{store_id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getStorePackageByStoreId(
            @Valid @PathVariable("store_id") int storeId) {
        try {
            List<StorePackage> storePackages = storePackageService.getStorePackageByStoreId(storeId);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Store packages with store Id = " + storeId + " Found Successfully!!!")
                    .data(storePackages.stream().map(StorePackageResponse::fromStorePackage).toList())
                    .status(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
