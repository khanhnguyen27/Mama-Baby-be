package com.myweb.mamababy.controllers;


import com.myweb.mamababy.dtos.StoreDTO;
import com.myweb.mamababy.models.Store;
import com.myweb.mamababy.responses.product.ProductListResponse;
import com.myweb.mamababy.responses.store.StoreListResponse;
import com.myweb.mamababy.responses.store.StoreResponse;
import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.responses.user.UserResponse;
import com.myweb.mamababy.services.Store.IStoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/stores")
@RequiredArgsConstructor
public class StoreController {

    private final IStoreService storeService;

    //POST: http://localhost:8080/mamababy/stores
    @PostMapping("")
    //Nếu tham số truyền vào là 1 object thì sao ? => Data Transfer Object = Request Object
    public ResponseEntity<?> createStore(
            @Valid @RequestBody StoreDTO storeDTO,
            BindingResult result){

        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Store store  = storeService.createStore(storeDTO);
            return ResponseEntity.ok(store);
        }catch (Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    //Hiện tất cả các categories
    @GetMapping("")
    public ResponseEntity<?> getAllStores(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0",name = "page")     int page,
            @RequestParam(defaultValue = "12",name = "limit")    int limit
    ) {
        int totalPages = 0;

        PageRequest pageRequest = PageRequest.of(
                page, limit,
                //Sort.by("createdAt").descending()
                Sort.by("id").ascending()
        );

        Page<StoreResponse> storePage = storeService.getAllStores(keyword, pageRequest);
        totalPages = storePage.getTotalPages();
        List<StoreResponse> stores = storePage.getContent();

        StoreListResponse storeListResponse = StoreListResponse
                .builder()
                .stores(stores)
                .totalPages(totalPages)
                .build();
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Get store' details successfully")
                        .data(storeListResponse)
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Store> updateStore(
            @PathVariable int id,
            @Valid @RequestBody StoreDTO storeDTO
    ) {
        Store updateStore = storeService.updateStore(id, storeDTO);
        return ResponseEntity.ok(updateStore);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStore(@PathVariable int id) {
        storeService.deleteStore(id);
        return ResponseEntity.ok("Delete successfully !!!");
    }

}
