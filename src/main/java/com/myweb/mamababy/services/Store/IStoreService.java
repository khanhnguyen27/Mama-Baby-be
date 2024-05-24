package com.myweb.mamababy.services.Store;

import com.myweb.mamababy.dtos.StoreDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IStoreService {
    Store createStore(StoreDTO storeDTO) throws DataNotFoundException;
    Store getStoreById(int id);
    Page<Store> getAllStores(String keyword, PageRequest pageRequest);
    Store updateStore(int store, StoreDTO storeDTO);
    void deleteStore(int id);
}
