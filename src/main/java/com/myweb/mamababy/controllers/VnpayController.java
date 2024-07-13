package com.myweb.mamababy.controllers;

import com.myweb.mamababy.dtos.*;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.Package;
import com.myweb.mamababy.models.Store;
import com.myweb.mamababy.repositories.StoreRepository;
import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.responses.payment.VnpayResponse;
import com.myweb.mamababy.services.Package.IPackageService;
import com.myweb.mamababy.services.Payment.PaymentService;
import com.myweb.mamababy.services.StatusOrder.IStatusOrderService;
import com.myweb.mamababy.services.Store.IStoreService;
import com.myweb.mamababy.services.StorePackage.IStorePackageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;

@RestController
@RequestMapping("${api.prefix}/payment")
@RequiredArgsConstructor
public class VnpayController {

    private final PaymentService paymentService;
    private final IStatusOrderService statusOrderService;
    private final IStoreService storeService;
    private final IPackageService packageService;
    private final IStorePackageService storePackageService;
    private final StoreRepository storeRepository;

    @PostMapping("/vn-pay")
    public ResponseEntity<?> pay(
            HttpServletRequest request,
            @Valid @RequestBody PaymentDTO paymentDTO,
            BindingResult result
    ) throws DataNotFoundException {

        VnpayResponse vnpayResponse = paymentService.createVnPayPayment(request, paymentDTO);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Payment page")
                .status(HttpStatus.OK)
                .data(vnpayResponse)
                .build());
    }
    @GetMapping("/vn-pay-callback")
    public RedirectView payCallbackHandler(HttpServletRequest request) {
        try{
            String status = request.getParameter("vnp_ResponseCode");
            String vnpOrderInfo = request.getParameter("vnp_OrderInfo");
            String[] parts = vnpOrderInfo.split("\\|");
            int orderId = Integer.parseInt(parts[0]);
            int storeId = Integer.parseInt(parts[1]);
            int packageId = Integer.parseInt(parts[2]);

            RedirectView redirectView = new RedirectView();

            if (status.equals("00")) {
                if(orderId != 0 && packageId == 0){
                    statusOrderService.createStatusOrder(new StatusOrderDTO(orderId, "PENDING"));

                    redirectView = new RedirectView("http://localhost:3000/successPayment");
                    redirectView.addStaticAttribute("orderId", orderId);
                    redirectView.addStaticAttribute("storeId", storeId);
                }else if (orderId == 0 && packageId != 0){
                    Store existingStore = storeService.getStoreById(storeId);
                    Package exisitngPackage = packageService.getPackageById(packageId);
                    LocalDateTime validDate;
                    if(existingStore.getValidDate().isBefore(LocalDateTime.now().plusHours(7))){
                        validDate = LocalDateTime.now().plusHours(7).plusMonths(exisitngPackage.getMonth());
                    }else{
                        validDate = existingStore.getValidDate().plusMonths(exisitngPackage.getMonth());
                    }
                    existingStore.setValidDate(validDate);
                    storeRepository.save(existingStore);

                    StorePackageDTO newStorePackageDTO = StorePackageDTO.builder()
                            .packageId(packageId)
                            .storeId(storeId)
                            .price(exisitngPackage.getPrice())
                            .build();
                    storePackageService.createStorePackage(newStorePackageDTO);

                    redirectView = new RedirectView("http://localhost:3000/staff/successPackagePayment");
                    redirectView.addStaticAttribute("packageId", packageId);
                    redirectView.addStaticAttribute("storeId", storeId);
                }

            } else {
                if(orderId != 0 && packageId == 0){
                    redirectView = new RedirectView("http://localhost:3000/failedPayment");
                    redirectView.addStaticAttribute("orderId", orderId);
                    redirectView.addStaticAttribute("storeId", storeId);
                }else if (orderId == 0 && packageId != 0) {
                    redirectView = new RedirectView("http://localhost:3000/staff/failedPackagePayment");
                    redirectView.addStaticAttribute("packageId", packageId);
                    redirectView.addStaticAttribute("storeId", storeId);
                }

            }
            return redirectView;
        }catch (Exception e){
            return  new RedirectView("/error.html");
        }
    }
}
