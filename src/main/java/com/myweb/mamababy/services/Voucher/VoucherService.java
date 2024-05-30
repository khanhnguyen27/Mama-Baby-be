package com.myweb.mamababy.services.Voucher;

import com.myweb.mamababy.dtos.VoucherDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.Order;
import com.myweb.mamababy.models.Product;
import com.myweb.mamababy.models.Store;
import com.myweb.mamababy.models.Voucher;
import com.myweb.mamababy.repositories.StoreRepository;
import com.myweb.mamababy.repositories.VoucherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherService implements IVoucherService{

    private final VoucherRepository voucherRepository;
    private final StoreRepository storeRepository;

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
                .isActive(true)
                .build();

        return voucherRepository.save(newVoucher);
    }

    @Override
    public Voucher getVoucherById(int id) throws Exception {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find voucher with id: " + id));
        return voucher;
    }

    @Override
    public List<Voucher> getVoucherByStoreId(int storeId) throws Exception {

        List<Voucher> vouchers = voucherRepository.findByStoreId(storeId);

        if (vouchers.isEmpty()) {
            throw new DataNotFoundException("Cannot find voucher for store with id: " + storeId);
        }
        return vouchers;
    }

    @Override
    public List<Voucher> getAllVoucher() throws Exception {
        return voucherRepository.findAll();
    }

    @Override
    public Voucher updateVoucher(int id, VoucherDTO voucherDTO) throws Exception {

        Store existingStore = storeRepository
                .findById(voucherDTO.getStoreId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find store with id: "+ voucherDTO.getStoreId()));

        Voucher existingVoucher  = voucherRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Cannot find voucher with id: " + id));

        existingVoucher.setStore(existingStore);
        existingVoucher.setCode(voucherDTO.getCode());
        existingVoucher.setDiscountValue(voucherDTO.getDiscountValue());
        existingVoucher.setDescription(voucherDTO.getDescription());
        existingVoucher.setEndAt(voucherDTO.getEndAt());

        return voucherRepository.save(existingVoucher);
    }

    @Override
    @Transactional
    public Voucher deleteVoucher(int id) throws DataNotFoundException {

        Voucher existingVoucher = voucherRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Cannot find voucher with id: " + id));

        existingVoucher.setActive(false);
        return voucherRepository.save(existingVoucher);
    }

}
