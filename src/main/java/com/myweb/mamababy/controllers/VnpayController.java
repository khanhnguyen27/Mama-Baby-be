package com.myweb.mamababy.controllers;

import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.responses.payment.VnpayResponse;
import com.myweb.mamababy.services.Payment.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/payment")
@RequiredArgsConstructor
public class VnpayController {

    private final PaymentService paymentService;

    @GetMapping("/vn-pay")
    public ResponseEntity<?> pay(HttpServletRequest request) {
        VnpayResponse vnpayResponse = paymentService.createVnPayPayment(request);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Payment page")
                .status(HttpStatus.OK)
                .data(vnpayResponse)
                .build());
    }
    @GetMapping("/vn-pay-callback")
    public ResponseEntity<?> payCallbackHandler(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        if (status.equals("00")) {

            VnpayResponse vnpayResponse = new VnpayResponse("00", "Payment success page", "https://");
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Payment success")
                    .status(HttpStatus.OK)
                    .data(vnpayResponse)
                    .build());

        } else {
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Payment fail")
                    .status(HttpStatus.OK)
                    .data(null)
                    .build());
        }
    }
}
