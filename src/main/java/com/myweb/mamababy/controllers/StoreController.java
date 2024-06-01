package com.myweb.mamababy.controllers;


import com.myweb.mamababy.dtos.StoreDTO;
import com.myweb.mamababy.models.Product;
import com.myweb.mamababy.models.Store;
import com.myweb.mamababy.models.User;
import com.myweb.mamababy.responses.product.ProductListResponse;
import com.myweb.mamababy.responses.product.ProductResponse;
import com.myweb.mamababy.responses.store.StoreListResponse;
import com.myweb.mamababy.responses.store.StoreResponse;
import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.responses.user.UserResponse;
import com.myweb.mamababy.services.Store.IStoreService;
import com.myweb.mamababy.services.User.IUserService;
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
    private final IUserService userService;

    //Tạo mới một cửa hàng
    //POST: http://localhost:8080/mamababy/stores
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("")
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
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Create new store successfully !!!")
                    .status(HttpStatus.OK)
                    .data(StoreResponse.fromStore(store))
                    .build());
        }catch (Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    //Hiện tất cả các store
    //GET: http://localhost:8080/mamababy/stores
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("")
    public ResponseEntity<?> getAllStores(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "0",name = "page")     int page,
            @RequestParam(defaultValue = "12",name = "limit")    int limit
    ) {
        int totalPages = 0;

        PageRequest pageRequest = PageRequest.of(
                page, limit,
                //Sort.by("createdAt").descending()
                Sort.by("id").ascending()
        );

        Page<StoreResponse> storePage = storeService.getAllStores(keyword, status, pageRequest);
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
    //Lấy ra một sản phẩm theo ID
    //GET: http://localhost:8080/mamababy/stores/{id}
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{id}")
    public ResponseEntity<?> getStoreById(
            @PathVariable("id") int storeId
    ){
        try{
            Store existingStore = storeService.getStoreById(storeId);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Get detail product successfully")
                    .status(HttpStatus.OK)
                    .data(StoreResponse.fromStore(existingStore))
                    .build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Cập nhật lại một sản phẩm theo ID
    //PUT: http://localhost:8080/mamababy/stores/{id}
    @PutMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> updateStore(
            @PathVariable int id,
            @Valid @RequestBody StoreDTO storeDTO
    ) {
        try{
            Store updateStore = storeService.updateStore(id, storeDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Create new store successfully !!!")
                    .status(HttpStatus.OK)
                    .data(StoreResponse.fromStore(updateStore))
                    .build());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/admin/update_status/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> updateStatusStore(
            @PathVariable int id,
            @Valid @RequestBody StoreDTO storeDTO
    ) {
        try{
            Store updateStore = storeService.updateStatusStore(id, storeDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Create new store successfully !!!")
                    .status(HttpStatus.OK)
                    .data(StoreResponse.fromStore(updateStore))
                    .build());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Xóa một sản phẩm theo ID
    //DELETE: http://localhost:8080/mamababy/stores/{id}
    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> deleteStore(@PathVariable int id) {
        try {
            Store deleteStore = storeService.deleteStore(id);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Delete successfully !!!")
                    .status(HttpStatus.OK)
                    .data(StoreResponse.fromStore(deleteStore))
                    .build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
