package com.myweb.mamababy.controllers;


import com.myweb.mamababy.dtos.VoucherDTO;
import com.myweb.mamababy.models.Voucher;
import com.myweb.mamababy.responses.Article.ArticleResponse;
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


import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("${api.prefix}/vouchers")
@RequiredArgsConstructor

public class VoucherController {

    private final IVoucherService voucherService;

    @PostMapping("")
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

        //Find By store id, staff can manage their voucher shop
        @GetMapping("/store/{id}")
        public ResponseEntity<?> getVoucher(@Valid @PathVariable("id") int shopId,
                                            @RequestHeader("Authorization") String token) {
            try {
                String extractedToken = token.substring(7);
                List<Voucher> exitingVoucher = voucherService.getVoucherById(shopId, extractedToken);
                return ResponseEntity.ok(ResponseObject.builder()
                        .message("Get voucher shop Successfully!!!")
                        .status(HttpStatus.OK)
                        .data(exitingVoucher.stream()
                                .map(VoucherResponse::fromVoucher)
                                .collect(Collectors.toList()))
                        .build());
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }

    //get All
    @GetMapping("")
    public ResponseEntity<?> getAllVoucher() throws Exception {
        List<Voucher> vouchers = voucherService.getAllVoucher();
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Get All Voucher Successfully!!!")
                        .data(vouchers.stream()
                                .map(VoucherResponse::fromVoucher)
                                .collect(Collectors.toList()))
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(
            @Valid @PathVariable int voucherId,
            @Valid @RequestBody VoucherDTO voucherDTO,
            @RequestHeader("Authorization") String token) {

        try {
            String extractedToken = token.substring(7);
            Voucher voucher = voucherService.updateVoucher(voucherId, voucherDTO, extractedToken);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Voucher With VoucherId = " + voucherId + " Updated Successfully!!!")
                    .status(HttpStatus.OK)
                    .data(VoucherResponse.fromVoucher(voucher))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Delete, không xóa cứng, cật nhật is_active
//    @DeleteMapping("/{id}")
//    public  ResponseEntity<?> deleteVoucher(@Valid @PathVariable int id){
//        try {
//
//            voucherService.deleteVoucher(id);
//            return ResponseEntity.ok(ResponseObject.builder()
//                    .data(null)
//                    .message(String.format("Voucher With Id = %d Deleted Successfully!!!", id))
//                    .status(HttpStatus.OK)
//                    .build());
//
//        }catch (Exception e){
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
}
