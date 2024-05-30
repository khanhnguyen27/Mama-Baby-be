package com.myweb.mamababy.services.Voucher;


import com.myweb.mamababy.dtos.VoucherDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.Voucher;

import java.util.List;

public interface IVoucherService {

    int calculateVoucherValue(String code, int discountValue);

    Voucher createVoucher(VoucherDTO voucherDTO) throws Exception;

    Voucher getVoucherById(int id) throws Exception;

    List<Voucher> getVoucherByStoreId(int storeId) throws Exception;

    List<Voucher> getAllVoucher() throws Exception;

    Voucher updateVoucher(int id, VoucherDTO voucherDTO) throws Exception;

    Voucher deleteVoucher(int id) throws DataNotFoundException;
}
