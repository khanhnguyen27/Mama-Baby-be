package com.myweb.mamababy.controllers;

import com.myweb.mamababy.dtos.*;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.responses.payment.VnpayResponse;
import com.myweb.mamababy.services.Payment.PaymentService;
import com.myweb.mamababy.services.StatusOrder.IStatusOrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("${api.prefix}/payment")
@RequiredArgsConstructor
public class VnpayController {

    private final PaymentService paymentService;
    private final IStatusOrderService statusOrderService;

//    @PostMapping("/vn-pay")
//    public ResponseEntity<?> pay(
//            HttpServletRequest request
//    ) {
//        VnpayResponse vnpayResponse = paymentService.createVnPayPayment(request);
//        return ResponseEntity.ok(ResponseObject.builder()
//                .message("Payment page")
//                .status(HttpStatus.OK)
//                .data(vnpayResponse)
//                .build());
//    }

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

            RedirectView redirectView;

            if (status.equals("00")) {
                statusOrderService.createStatusOrder(new StatusOrderDTO(orderId, "PENDING"));
                redirectView = new RedirectView(" http://localhost:3000/successPayment");
                redirectView.addStaticAttribute("orderId", orderId);
                redirectView.addStaticAttribute("storeId;", storeId);
            } else {
                redirectView = new RedirectView(" http://localhost:3000/failedPayment");
                redirectView.addStaticAttribute("orderId", orderId);
                redirectView.addStaticAttribute("storeId;", storeId);
            }
            return redirectView;
        }catch (Exception e){
            return  new RedirectView("/error.html");
        }
    }
}
