package com.myweb.mamababy.services.Voucher;

import com.myweb.mamababy.dtos.VoucherDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.Voucher;
import com.myweb.mamababy.repositories.VoucherRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoucherService implements IVoucherService{

    private final VoucherRepository voucherRepository;

    @Override
    public int calculateVoucherValue(String code, int discountValue) {
        return 0;
    }

    @Override
    public Voucher createVoucher(VoucherDTO voucherDTO) throws Exception {

        Voucher newVoucher = Voucher.builder()

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
      return voucherRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find voucher with id: " + id));
    }

  @Override
    public List<Voucher> getAllVoucher() throws Exception {
        return voucherRepository.findAll();
    }

    @Override
    public Voucher updateVoucher(int id, VoucherDTO voucherDTO) throws Exception {

        Voucher existingVoucher  = voucherRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Cannot find voucher with id: " + id));

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

      if (!existingVoucher.isActive()) {
        throw new DataNotFoundException("Voucher with id " + id + " was deleted before!!!");
      }

        existingVoucher.setActive(false);
        return voucherRepository.save(existingVoucher);
    }
}
