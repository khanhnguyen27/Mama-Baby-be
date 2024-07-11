package com.myweb.mamababy.controllers;


import com.myweb.mamababy.dtos.PackageDTO;
import com.myweb.mamababy.models.Package;
import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.services.Package.IPackageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/package")
@RequiredArgsConstructor
public class PackageController {

    private final IPackageService packageService;

    @PostMapping("")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> createCategory(
            @Valid @RequestBody PackageDTO packageDTO,
            BindingResult result) {
        if(result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        Package aPackage = packageService.createPackage(packageDTO);
        return ResponseEntity.ok(ResponseObject
                .builder()
                .message("Create package successfully !!!")
                .status(HttpStatus.OK)
                .data(aPackage).
                build());
    }

    @GetMapping("")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getAllPackage() {
        List<Package> packages = packageService.getAllPackage();
        return ResponseEntity.ok(ResponseObject
                .builder()
                .message("Get list package successfully !!!")
                .status(HttpStatus.OK)
                .data(packages).
                build());
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getPackageById(
            @PathVariable("id") int packageId
    ) {
        Package aPackage = packageService.getPackageById(packageId);
        return ResponseEntity.ok(ResponseObject
                .builder()
                .message("Get list package successfully !!!")
                .status(HttpStatus.OK)
                .data(aPackage).
                build());
    }

    @PutMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> updateProduct(
            @PathVariable int id,
            @RequestBody PackageDTO packageDTO,
            BindingResult result
    ){
        try{
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Package updatedPackage = packageService.updatePackage(id, packageDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                    .data(updatedPackage)
                    .message("Update package successfully")
                    .status(HttpStatus.OK)
                    .build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
