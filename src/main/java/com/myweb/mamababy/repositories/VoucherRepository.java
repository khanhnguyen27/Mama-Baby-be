package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {

    List<Voucher> findByStoreId(int storeId);

}
