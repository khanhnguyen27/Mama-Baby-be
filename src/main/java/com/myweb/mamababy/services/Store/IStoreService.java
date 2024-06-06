package com.myweb.mamababy.services.Store;

import com.myweb.mamababy.dtos.StoreDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.Store;
import com.myweb.mamababy.responses.store.StoreResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IStoreService {
    Store createStore(StoreDTO storeDTO) throws DataNotFoundException;
    Store getStoreById(int id);
    Store getStoreByUserId(int id) throws DataNotFoundException;
    Page<StoreResponse> getAllStores(String keyword,String status, PageRequest pageRequest);
    Store updateStore(int id, StoreDTO storeDTO);
    Store updateStatusStore(int id, StoreDTO storeDTO);
    Store deleteStore(int id);

}
