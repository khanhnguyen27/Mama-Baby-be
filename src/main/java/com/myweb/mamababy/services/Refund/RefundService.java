package com.myweb.mamababy.services.Refund;

import com.myweb.mamababy.dtos.CartItemRefundDTO;
import com.myweb.mamababy.dtos.RefundDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.*;
import com.myweb.mamababy.repositories.*;
import com.myweb.mamababy.responses.refunds.RefundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RefundService implements IRefundService {

    private final RefundRepository refundRepository;
    private final RefundDetailRepository refundDetailRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;


    @Override
    public List<Refund> findByYear(int year) {
        List<Refund> refunds = refundRepository.findByRefundDateYear(year);
        return refunds.stream().filter(refund -> refund.getCreateDate().getYear() == year).collect(
                Collectors.toList());
    }

    @Override
    public Refund createRefund(RefundDTO refundDTO) throws Exception {

        User existingUser = userRepository
                .findById(refundDTO.getUserId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find user with id: " + refundDTO.getUserId()));
        Store existingStore = storeRepository
                .findById(refundDTO.getStoreId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find store with id: " + refundDTO.getStoreId()));
        Order existingOrder = orderRepository
                .findById(refundDTO.getOrderId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find store with id: " + refundDTO.getStoreId()));

        if (!existingUser.getIsActive() || !existingStore.isActive()) {
            throw new DataNotFoundException("Invalid");
        }
        Refund newRefund = Refund.builder()
                .description(refundDTO.getDescription())
                .status("PROCESSING")
                .amount(refundDTO.getAmount())
                .createDate(LocalDateTime.now().plusHours(7))
                .user(existingUser)
                .store(existingStore)
                .order(existingOrder)
                .build();

        List<RefundDetail> refundDetails = new ArrayList<>();

        for (CartItemRefundDTO cartItemRefundDTO : refundDTO.getCartItemRefund()) {

            // Tạo một đối tượng RefundDetail từ CartItemDTO
            RefundDetail refundDetail = new RefundDetail();
            refundDetail.setRefund(newRefund);

            int productId = cartItemRefundDTO.getProductId();
            int quantity = cartItemRefundDTO.getQuantity();
            float unitPrice = cartItemRefundDTO.getUnitPrice();
            float amount = cartItemRefundDTO.getAmount();

            Product existingProduct = productRepository
                    .findById(productId)
                    .orElseThrow(() ->
                            new DataNotFoundException(
                                    "Cannot find product with id: " + productId));

            refundDetail.setProduct(existingProduct);
            refundDetail.setQuantity(quantity);
            refundDetail.setUnitPrice(unitPrice);
            refundDetail.setAmount(amount);

            refundDetails.add(refundDetail);
        }

        newRefund.setRefundDetails(refundDetails);
        refundRepository.save(newRefund);
        refundDetailRepository.saveAll(refundDetails);
        return newRefund;
    }

    @Override
    public Refund getRefundById(int id) throws DataNotFoundException {
        Optional<Refund> optionalRefund = refundRepository.findById(id);
        if (optionalRefund.isPresent()) {
            return optionalRefund.get();
        }
        throw new DataNotFoundException("Cannot find refund with id =" + id);
    }

    @Override
    public Page<RefundResponse> getAllRefund(String status, PageRequest pageRequest) {
        Page<Refund> refundPage = refundRepository.searchExchange(status, pageRequest);
        return refundPage.map(RefundResponse::fromRefund);
    }

    @Override
    public Refund updateRefund(int id, RefundDTO refundDTO) throws DataNotFoundException {
        Refund existingExchange = getRefundById(id);
        if (existingExchange != null) {

            if (refundDTO.getStatus() != null &&
                    !refundDTO.getStatus().isEmpty()) {
                if (refundDTO.getStatus().equals("ACCEPT")) {
                    for (CartItemRefundDTO cart : refundDTO.getCartItemRefund()) {
                        Optional<Product> existingProduct = productRepository.findById(cart.getProductId());
                        if (existingProduct.isPresent()) {
                            Product product = existingProduct.get();
                            product.setRemain(product.getRemain() + cart.getQuantity());
                            productRepository.save(product);
                        }
                    }
                }
                existingExchange.setStatus(refundDTO.getStatus());
            }

            return refundRepository.save(existingExchange);
        }
        return null;
    }

    @Override
    public List<Refund> findByUserId(int userId) throws DataNotFoundException {

//        if (refunds.isEmpty()) {
//            throw new DataNotFoundException("Cannot find exchange for user with id: " + userId);
//        }
        return refundRepository.findByUserId(userId);
    }


    @Override
    public List<Refund> findByStoreId(int storeId) throws DataNotFoundException {

//        if (refunds.isEmpty()) {
//            throw new DataNotFoundException("Cannot find exchange for store with id: " + storeId);
//        }

        return refundRepository.findByStoreId(storeId);
    }
}
