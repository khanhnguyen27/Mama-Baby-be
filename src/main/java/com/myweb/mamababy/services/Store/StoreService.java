package com.myweb.mamababy.services.Store;

import com.myweb.mamababy.dtos.StoreDTO;

import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.Store;
import com.myweb.mamababy.models.User;
import com.myweb.mamababy.responses.store.StoreResponse;
import com.myweb.mamababy.repositories.StoreRepository;
import com.myweb.mamababy.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService implements IStoreService{

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Store createStore(StoreDTO storeDTO) throws DataNotFoundException  {

        User existingUser = userRepository
                .findById(storeDTO.getUserId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find user with id: "+storeDTO.getUserId()));

        if(storeRepository.existsByUserId(storeDTO.getUserId()) || !existingUser.getIsActive()) {
            throw new DataIntegrityViolationException("User cannot create new store !!!");
        }

        Store newStore = Store
                .builder()
                .nameStore(storeDTO.getNameStore())
                .address(storeDTO.getAddress())
                .description(storeDTO.getDescription())
                .phone(storeDTO.getPhone())
                .status("PROCESSING")
                .isActive(false)
                .build();
        newStore.setUser(existingUser);

        return storeRepository.save(newStore);
    }

    @Override
    public Store getStoreById(int id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found"));
    }

    @Override
    public Page<StoreResponse> getAllStores(String keyword, String status, PageRequest pageRequest) {
        Page<Store> storesPage = storeRepository.searchStores(keyword, status, pageRequest);
        return storesPage.map(StoreResponse::fromStore);
    }

    @Override
    public Store updateStore(int id, StoreDTO storeDTO) {
        // Tìm sản phẩm tồn tại trong DB
        Store existingStore = getStoreById(id);

        existingStore.setNameStore(storeDTO.getNameStore());
        existingStore.setAddress(storeDTO.getAddress());
        existingStore.setDescription(storeDTO.getDescription());
        existingStore.setPhone(storeDTO.getPhone());

        storeRepository.save(existingStore);
        return existingStore;
    }

    @Override
    public Store updateStatusStore(int id, StoreDTO storeDTO) {
        Store existingStore = getStoreById(id);

        existingStore.setStatus(storeDTO.getStatus());
        existingStore.setActive(storeDTO.isActive());

        return storeRepository.save(existingStore);
    }

    @Override
    public Store deleteStore(int id) {
        Store existingStore = getStoreById(id);
        existingStore.setActive(false);
        storeRepository.save(existingStore);
        return existingStore;
    }
}
