package com.myweb.mamababy.services.Payment;

import com.myweb.mamababy.dtos.PaymentDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.responses.payment.VnpayResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface IPaymentService {
     VnpayResponse createVnPayPayment(HttpServletRequest request, PaymentDTO paymentDTO) throws DataNotFoundException;
}
