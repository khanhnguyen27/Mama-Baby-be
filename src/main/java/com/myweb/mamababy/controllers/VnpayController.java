package com.myweb.mamababy.controllers;

import com.myweb.mamababy.dtos.*;
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

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/payment")
@RequiredArgsConstructor
public class VnpayController {

    private final PaymentService paymentService;
    private final IStatusOrderService statusOrderService;
    private List<PaymentOrderDTO>  paymentOrderDTOList;

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
    ) {

        paymentOrderDTOList = null;

        paymentOrderDTOList = paymentDTO.getOrders();

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
            if (status.equals("00")) {
                for(PaymentOrderDTO p : paymentOrderDTOList){
                    statusOrderService.createStatusOrder(new StatusOrderDTO(p.getId(), "PENDING"));
                }
                return new RedirectView("/payment-success.html");
            } else {
                return new RedirectView("/payment-fail.html");
            }
        }catch (Exception e){
            return  new RedirectView("/error.html");
        }
    }
}
