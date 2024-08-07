package com.myweb.mamababy.services.Payment;

import com.myweb.mamababy.components.VnpayUtil;
import com.myweb.mamababy.configurations.VnpayConfig;
import com.myweb.mamababy.dtos.PaymentDTO;
import com.myweb.mamababy.dtos.StatusOrderDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.repositories.StoreRepository;
import com.myweb.mamababy.responses.payment.VnpayResponse;
import com.myweb.mamababy.services.StatusOrder.IStatusOrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService{

    private final VnpayConfig vnPayConfig;

    @Override
    public VnpayResponse createVnPayPayment(HttpServletRequest request, PaymentDTO paymentDTO) throws DataNotFoundException {

        long amount = (int)paymentDTO.getFinalAmount() * 100L;
        String bankCode = paymentDTO.getBankCode();
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        vnpParamsMap.put("vnp_OrderInfo", paymentDTO.getOrderId() + "|" + paymentDTO.getStoreId() + "|" + paymentDTO.getStorePackageId());
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", VnpayUtil.getIpAddress(request));
        //build query url
        String queryUrl = VnpayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VnpayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VnpayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
        return VnpayResponse.builder()
                .code("ok")
                .message("success")
                .paymentUrl(paymentUrl)
                .build();
    }
}
