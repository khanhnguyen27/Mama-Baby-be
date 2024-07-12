package com.myweb.mamababy.services.StorePackage;

import com.myweb.mamababy.dtos.StorePackageDTO;
import com.myweb.mamababy.models.StorePackage;

import java.util.List;

public interface IStorePackageService {

    StorePackage createStorePackage(StorePackageDTO storePackageDTO) throws Exception;

    StorePackage getStorePackageById(int id) throws Exception;

    List<StorePackage> getStorePackageByStoreId(int id);

    List<StorePackage> getAllPackage();
}
