package com.myweb.mamababy.services.Payment;

import com.myweb.mamababy.configurations.VnpayConfig;
import com.myweb.mamababy.responses.payment.VnpayResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface IPaymentService {
     VnpayResponse createVnPayPayment(HttpServletRequest request);
}
