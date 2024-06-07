package com.myweb.mamababy.controllers;

import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.responses.payment.VnpayResponse;
import com.myweb.mamababy.services.Payment.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("${api.prefix}/payment")
@RequiredArgsConstructor
public class VnpayController {

    private final PaymentService paymentService;

    @PostMapping("/vn-pay")
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
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("https://example.com/payment-success"));
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
        } else {
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Payment fail")
                    .status(HttpStatus.OK)
                    .data(null)
                    .build());
        }
    }
}
