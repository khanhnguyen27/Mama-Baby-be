package com.myweb.mamababy.controllers;


import com.myweb.mamababy.dtos.AgeDTO;
import com.myweb.mamababy.models.Age;
import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.services.Age.IAgeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/age")
@RequiredArgsConstructor
public class AgeController {

    private final IAgeService ageService;

    @PostMapping("")
    @CrossOrigin(origins = "http://localhost:3000")
    //Nếu tham số truyền vào là 1 object thì sao ? => Data Transfer Object = Request Object
    public ResponseEntity<?> createAge(
            @Valid @RequestBody AgeDTO ageDTO,
            BindingResult result) {
        if(result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        Age age  = ageService.createAge(ageDTO);
        return ResponseEntity.ok(age);
    }

    //GET: http://localhost:8080/mamababy/products
    //Hiện tất cả các age is true
    @GetMapping("")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getAllAges(
            @RequestParam(defaultValue = "0",name = "page")     int page,
            @RequestParam(defaultValue = "12",name = "limit")    int limit
    ) {
        List<Age> ages = ageService.findByIsActiveTrue();
        return ResponseEntity.ok(ResponseObject
                .builder()
                .message("Get list ages successfully !!!")
                .status(HttpStatus.OK)
                .data(ages)
                .build());
    }

    //Hiện tất cả các age
    @GetMapping("/admin")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getAllAgesIsTrue(
        @RequestParam(defaultValue = "0",name = "page")     int page,
        @RequestParam(defaultValue = "12",name = "limit")    int limit
    ) {
        List<Age> ages = ageService.getAllAges();
        return ResponseEntity.ok(ResponseObject
            .builder()
            .message("Get list ages successfully !!!")
            .status(HttpStatus.OK)
            .data(ages)
            .build());
    }

    @PutMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Age> updateAge(
            @PathVariable int id,
            @Valid @RequestBody AgeDTO ageDTO
    ) {
        Age updateAge = ageService.updateAge(id, ageDTO);
        return ResponseEntity.ok(updateAge);
    }

    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> deleteAge(@PathVariable int id) {
        Age deleteAge = ageService.deleteAge(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Delete successfully !!!")
                .status(HttpStatus.OK)
                .data(deleteAge)
                .build());
    }

}
