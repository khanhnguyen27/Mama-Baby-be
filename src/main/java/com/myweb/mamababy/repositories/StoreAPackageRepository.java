package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.StorePackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreAPackageRepository extends JpaRepository<StorePackage, Integer> {

    List<StorePackage> findByStoreId(int storeId);
}
