package com.myweb.mamababy.services.StorePackage;

import com.myweb.mamababy.dtos.StorePackageDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.Package;
import com.myweb.mamababy.models.Store;
import com.myweb.mamababy.models.StorePackage;
import com.myweb.mamababy.repositories.PackageRepository;
import com.myweb.mamababy.repositories.StoreAPackageRepository;
import com.myweb.mamababy.repositories.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StorePackageService implements IStorePackageService{

    private final StoreAPackageRepository storeAPackageRepository;
    private final PackageRepository packageRepository;
    private final StoreRepository storeRepository;

    @Override
    public StorePackage createStorePackage(StorePackageDTO storePackageDTO) throws Exception{
        Package existingPackage = packageRepository.findById(storePackageDTO.getPackageId())
                .orElseThrow(
                        () -> new DataNotFoundException("Cannot find package with id: " + storePackageDTO.getPackageId()));
        Store existingStore = storeRepository.findById(storePackageDTO.getStoreId())
                .orElseThrow(
                        () -> new DataNotFoundException("Cannot find store with id: " + storePackageDTO.getStoreId()));

        StorePackage newStorePackage = StorePackage.builder()
                .aPackage(existingPackage)
                .store(existingStore)
                .price(existingPackage.getPrice())
                .buyDate(LocalDateTime.now().plusHours(7))
                .validDate(existingStore.getValidDate())
                .build();
        return storeAPackageRepository.save(newStorePackage);
    }

    @Override
    public StorePackage getStorePackageById(int id) throws Exception{
        Optional<StorePackage> optionalStorePackage = storeAPackageRepository.findById(id);
        if (optionalStorePackage.isPresent()) {
            return optionalStorePackage.get();
        }
        throw new DataNotFoundException("Cannot find product with id =" + id);
    }

    @Override
    public List<StorePackage> getStorePackageByStoreId(int id) {
        return storeAPackageRepository.findByStoreId(id);
    }

    @Override
    public List<StorePackage> getAllPackage() {
        return storeAPackageRepository.findAll();
    }
}
