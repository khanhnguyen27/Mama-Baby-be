package com.myweb.mamababy.controllers;

import com.myweb.mamababy.dtos.BrandDTO;
import com.myweb.mamababy.dtos.ExchangeDTO;
import com.myweb.mamababy.models.Exchange;
import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.responses.exchange.ExchangeListResponse;
import com.myweb.mamababy.responses.exchange.ExchangeResponse;
import com.myweb.mamababy.services.Exchange.IExchangeService;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/exchanges")
@RequiredArgsConstructor
public class ExchangeController {

    private final IExchangeService exchangeService;

    //POST http://localhost:8080/mamababy/exchanges
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(value="", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createExchange(
            @Valid @ModelAttribute ExchangeDTO exchangeDTO,
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
            Exchange newExchange = exchangeService.createExchange(exchangeDTO, file);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Create new exchange request successfully")
                    .status(HttpStatus.CREATED)
                    .data(ExchangeResponse.fromExchange(newExchange))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //GET http://localhost:8080/mamababy/products
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("")
    public ResponseEntity<?> getExchanges(
            @RequestParam(defaultValue = "") String status,
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
        Page<ExchangeResponse> exchangePage = exchangeService
                .getAllExchange(status, pageRequest);
        // Lấy tổng số trang
        totalPages = exchangePage.getTotalPages();
        List<ExchangeResponse> exchanges = exchangePage.getContent();

        ExchangeListResponse exchangeListResponse = ExchangeListResponse
                .builder()
                .exchanges(exchanges)
                .totalPages(totalPages)
                .build();
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Get exchange request successfully")
                .data(exchangeListResponse)
                .status(HttpStatus.OK)
                .build());
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{id}")
    public ResponseEntity<?> getExchangeById(
            @PathVariable("id") int exchangeId
    ){
        try{
            Exchange existingExchange = exchangeService.getExchangeById(exchangeId);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Get detail product successfully")
                    .status(HttpStatus.OK)
                    .data(ExchangeResponse.fromExchange(existingExchange))
                    .build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExchange(@PathVariable int id) {
        try {
            exchangeService.deleteExchange(id);
            return ResponseEntity.ok(ResponseObject.builder()
                    .data(null)
                    .message(String.format("Exchange request with id = %d deleted successfully", id))
                    .status(HttpStatus.OK)
                    .build());

        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateExchange(
            @PathVariable int id,
            @Valid @RequestBody ExchangeDTO exchangeDTO
    ) {
        try {
            Exchange updatedExchange = exchangeService.updateExchange(id, exchangeDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                    .data(ExchangeResponse.fromExchange(updatedExchange))
                    .message("Update product successfully")
                    .status(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
