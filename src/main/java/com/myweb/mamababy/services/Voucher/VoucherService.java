package com.myweb.mamababy.services.Voucher;

import com.myweb.mamababy.dtos.VoucherDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.Actived;
import com.myweb.mamababy.models.Store;
import com.myweb.mamababy.models.Voucher;
import com.myweb.mamababy.repositories.StoreRepository;
import com.myweb.mamababy.repositories.VoucherRepository;
import com.myweb.mamababy.services.Actived.IActivedService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherService implements IVoucherService{

    private final VoucherRepository voucherRepository;
    private final IActivedService activedService;
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

                                "Cannot find store with id: "+voucherDTO.getStoreId()));

        if (!existingStore.isActive() || existingStore.getValidDate().isBefore(LocalDateTime.now().plusHours(7))) {
            throw new DataNotFoundException("Invalid store is inActive");
        }

        Voucher newVoucher = Voucher.builder()

                .code(voucherDTO.getCode())
                .discountValue(voucherDTO.getDiscountValue())
                .description(voucherDTO.getDescription())
                .endAt(voucherDTO.getEndAt())
                .store(existingStore)
                .isActive(voucherDTO.isActive())
                .build();

        return voucherRepository.save(newVoucher);
    }

    @Override
    public Voucher getVoucherById(int id) throws Exception {
      return voucherRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find voucher with id: " + id));
    }

    @Override
    public List<Voucher> getAllVoucher(int storeId, int userId) throws Exception {
        List<Actived> activedList = new ArrayList<>();
        List<Voucher> listAll = voucherRepository.findAll();
        List<Voucher> listVoucherValid = new ArrayList<>();
        if (userId != 0) {
            activedList = activedService.getActivedByUserId(userId);
        }
        if (storeId != 0) {
            listAll = voucherRepository.findByStoreId(storeId);
        }
        for(Voucher voucher : listAll) {
            LocalDateTime nowPlus7Hours = LocalDateTime.now().plusHours(7);
            LocalDate nowPlus7HoursDate = nowPlus7Hours.toLocalDate();
            if (voucher.isActive() && !(nowPlus7HoursDate.isAfter(voucher.getEndAt()) || nowPlus7HoursDate.isEqual(voucher.getEndAt()))) {
                boolean isInActivedList = false;
                for (Actived actived : activedList) {
                    if (actived.getVoucherId() == voucher.getId()) {
                        isInActivedList = true;
                        break;
                    }
                }
                if(!isInActivedList){
                    listVoucherValid.add(voucher);
                }
            }
        }
        return listVoucherValid;
    }

    @Override
    public List<Voucher> getAllVoucherByStoreId(int storeId) throws Exception {
        return voucherRepository.findByStoreId(storeId);
    }

    @Override
    public List<Voucher> getAllVoucherAdmin() throws Exception {
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
        existingVoucher.setActive(voucherDTO.isActive());

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
