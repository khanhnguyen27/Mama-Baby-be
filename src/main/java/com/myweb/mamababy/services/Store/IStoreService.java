package com.myweb.mamababy.services.Store;

import com.myweb.mamababy.dtos.CategoryDTO;
import com.myweb.mamababy.dtos.StoreDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.Category;
import com.myweb.mamababy.models.Store;

import java.util.List;

public interface IStoreService {
    Store createStore(StoreDTO storeDTO) throws DataNotFoundException;
    Store getStoreById(int id);
    List<Store> getAllStores();
    Store updateStore(int store, StoreDTO storeDTO);
    void deleteStore(int id);
}
