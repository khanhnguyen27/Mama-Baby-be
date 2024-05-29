package com.myweb.mamababy.services.Voucher;

import com.myweb.mamababy.components.JwtTokenUtil;
import com.myweb.mamababy.dtos.VoucherDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.exceptions.ExpiredTokenException;
import com.myweb.mamababy.models.*;
import com.myweb.mamababy.repositories.StoreRepository;
import com.myweb.mamababy.repositories.UserRepository;
import com.myweb.mamababy.repositories.VoucherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoucherService implements IVoucherService{

    private final VoucherRepository voucherRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public int calculateVoucherValue(String code, int discountValue) {
        return 0;
    }

    @Override
    public Voucher createVoucher(VoucherDTO voucherDTO) throws Exception {

        Store existingStore = storeRepository
                .findById(voucherDTO.getStoreId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find store with id: "+ voucherDTO.getStoreId()));


        Voucher newVoucher = Voucher.builder()

                .store(existingStore)
                .code(voucherDTO.getCode())
                .discountValue(voucherDTO.getDiscountValue())
                .description(voucherDTO.getDescription())
                .endAt(voucherDTO.getEndAt())
                .is_active(true)
                .build();

        return voucherRepository.save(newVoucher);
    }

    @Override
    public List<Voucher> getVoucherById(int id, String token) throws Exception {
        if (jwtTokenUtil.isTokenExpired(token)) {
            throw new ExpiredTokenException("Token is expired");
        }
        String subject = jwtTokenUtil.extractUserName(token);
        Optional<User> user;
        user = userRepository.findByUsername(subject);
        if (user.isPresent()) {
            User retrievedUser = user.get();
            Store existingStore = storeRepository
                    .findByUserId(retrievedUser.getId())
                    .orElseThrow(() ->
                            new DataNotFoundException(
                                    "Cannot find store with id: "+ retrievedUser.getId()));
            if (existingStore.getId() != id) {
                throw new Exception("Store does not match");
            } else {
                return voucherRepository.findByStoreId(existingStore.getId());
            }
        }

        throw new Exception("User not found");
    }

    @Override
    public List<Voucher> getAllVoucher() throws Exception {
        return voucherRepository.findAll();
    }

    @Override
    public Voucher updateVoucher(int voucherId, VoucherDTO voucherDTO, String token) throws Exception {

        if (jwtTokenUtil.isTokenExpired(token)) {
            throw new ExpiredTokenException("Token is expired");
        }
        String subject = jwtTokenUtil.extractUserName(token);
        Optional<User> user;
        user = userRepository.findByUsername(subject);

        if (user.isPresent()) {
            User retrievedUser = user.get();
            Store existingStore = storeRepository
                    .findByUserId(retrievedUser.getId())
                    .orElseThrow(() ->
                            new DataNotFoundException(
                                    "Cannot find store with id: "+ retrievedUser.getId()));
            if (existingStore.getId() != voucherDTO.getStoreId()) {
                throw new Exception("Store does not match");
            } else {
                Voucher existingVoucher  = voucherRepository.findById(voucherId).orElseThrow(() ->
                        new DataNotFoundException("Cannot find voucher with id: " + voucherId));

                existingVoucher.setCode(voucherDTO.getCode());
                existingVoucher.setDiscountValue(voucherDTO.getDiscountValue());
                existingVoucher.setDescription(voucherDTO.getDescription());
                existingVoucher.setEndAt(voucherDTO.getEndAt());
                existingVoucher.setIs_active(voucherDTO.getIs_active());

                return voucherRepository.save(existingVoucher);
            }
        }

        throw new Exception("User not found");
    }

    @Override
    @Transactional
    public Voucher deleteVoucher(int id) throws DataNotFoundException {

        Voucher existingVoucher = voucherRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Cannot find voucher with id: " + id));

        voucherRepository.deleteById(id);

        return existingVoucher;
    }
}
