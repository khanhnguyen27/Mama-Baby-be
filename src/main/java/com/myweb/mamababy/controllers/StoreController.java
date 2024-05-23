package com.myweb.mamababy.controllers;


import com.myweb.mamababy.dtos.StoreDTO;
import com.myweb.mamababy.models.Store;
import com.myweb.mamababy.reponses.StoreListResponse;
import com.myweb.mamababy.reponses.StoreResponse;
import com.myweb.mamababy.services.Store.IStoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<StoreListResponse> getAllStores(
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
        return ResponseEntity.ok(StoreListResponse.builder()
                .stores(stores)
                .totalPages(totalPages)
                .build());
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
