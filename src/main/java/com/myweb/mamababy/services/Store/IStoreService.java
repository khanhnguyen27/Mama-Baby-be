package com.myweb.mamababy.services.Store;

import com.myweb.mamababy.dtos.StoreDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.Store;
import com.myweb.mamababy.responses.store.StoreResponse;

import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

public interface IStoreService {

    Store createStore(StoreDTO storeDTO, MultipartFile file) throws DataNotFoundException, IOException;

    Store getStoreById(int id);

    Store getStoreByUserId(int id) throws DataNotFoundException;

    Page<StoreResponse> getAllStores(String keyword,String status, PageRequest pageRequest);

    Store updateStore(int id, StoreDTO storeDTO, MultipartFile file) throws IOException;

    Store updateStatusStore(int id, StoreDTO storeDTO);

    Store deleteStore(int id);

    List<Store> findByCurrentMonth(int month) throws DataNotFoundException;

    Boolean checkFileImage(MultipartFile file);

    String storeFile(MultipartFile file) throws IOException;

    void deleteFile(String filename) throws IOException;
}
