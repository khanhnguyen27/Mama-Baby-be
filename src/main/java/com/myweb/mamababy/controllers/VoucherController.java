package com.myweb.mamababy.controllers;


import com.myweb.mamababy.dtos.VoucherDTO;
import com.myweb.mamababy.models.Voucher;
import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.responses.voucher.VoucherResponse;
import com.myweb.mamababy.services.Voucher.IVoucherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("${api.prefix}/vouchers")
@RequiredArgsConstructor

public class VoucherController {

    private final IVoucherService voucherService;

    // Create Voucher
    @PostMapping("")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> createVoucher(
            @Valid @RequestBody VoucherDTO voucherDTO,
            BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Voucher newVoucher = voucherService.createVoucher(voucherDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Create New Voucher Successfully!!!")
                    .status(HttpStatus.CREATED)
                    .data(VoucherResponse.fromVoucher(newVoucher))
                    .build());

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Find Voucher By Id
    @GetMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getVoucher(@PathVariable("id") int voucherId) {
        try {
            Voucher exitingVoucher = voucherService.getVoucherById(voucherId);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Voucher With VoucherId = " + voucherId + " Found Successfully!!!")
                    .status(HttpStatus.OK)
                    .data(VoucherResponse.fromVoucher(exitingVoucher))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Get All Voucher
    @GetMapping("")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getAllVoucher(
            @RequestParam(defaultValue = "0", name = "user_id") int userId,
            @RequestParam(defaultValue = "0", name = "store_id") int storeId
    ) throws Exception {
        List<Voucher> vouchers = voucherService.getAllVoucher(storeId, userId);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Get all voucher Successfully!!!")
                        .data(vouchers.stream()
                                .map(VoucherResponse::fromVoucher)
                                .collect(Collectors.toList()))
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    //Get All Voucher by store ID
    @GetMapping("store/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getAllVoucherByStoreId(
            @PathVariable("id") int storeId
    ) throws Exception {
        List<Voucher> vouchers = voucherService.getAllVoucherByStoreId(storeId);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Get all voucher Successfully!!!")
                        .data(vouchers.stream()
                                .sorted(Comparator.comparing(Voucher::getEndAt).reversed())
                                .map(VoucherResponse::fromVoucher)
                                .collect(Collectors.toList()))
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @GetMapping("/admin")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getAllVoucherAdmin() throws Exception {
        List<Voucher> vouchers = voucherService.getAllVoucherAdmin();
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Get all voucher Successfully!!!")
                        .data(vouchers.stream()
                                .map(VoucherResponse::fromVoucher)
                                .collect(Collectors.toList()))
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    // Update Voucher
    @PutMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> updateOrder(
            @Valid @PathVariable int id,
            @Valid @RequestBody VoucherDTO voucherDTO) {

        try {
            Voucher voucher = voucherService.updateVoucher(id, voucherDTO);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Voucher With VoucherId = " + id + " Updated Successfully!!!")
                    .status(HttpStatus.OK)
                    .data(VoucherResponse.fromVoucher(voucher))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Delete Voucher By Soft Deleted
    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public  ResponseEntity<?> deleteVoucher(@Valid @PathVariable int id){
        try {

            Voucher deleteVoucher  = voucherService.deleteVoucher(id);
            return ResponseEntity.ok(ResponseObject.builder()
                    .data(VoucherResponse.fromVoucher(deleteVoucher))
                    .message(String.format("Voucher With Id = %d Deleted Successfully!!!", id))
                    .status(HttpStatus.OK)
                    .build());

        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
