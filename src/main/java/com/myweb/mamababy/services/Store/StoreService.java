package com.myweb.mamababy.services.Store;

import com.myweb.mamababy.dtos.StoreDTO;

import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.Package;
import com.myweb.mamababy.models.Store;
import com.myweb.mamababy.models.StorePackage;
import com.myweb.mamababy.models.User;
import com.myweb.mamababy.repositories.PackageRepository;
import com.myweb.mamababy.repositories.StoreAPackageRepository;
import com.myweb.mamababy.responses.store.StoreResponse;
import com.myweb.mamababy.repositories.StoreRepository;
import com.myweb.mamababy.repositories.UserRepository;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreService implements IStoreService{

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final StoreAPackageRepository storeAPackageRepository;
    private final PackageRepository packageRepository;
    private static final String UPLOADS_FOLDER = "uploads";


    @Override
    @Transactional
    public Store createStore(StoreDTO storeDTO, MultipartFile file) throws DataNotFoundException, IOException {

        User existingUser = userRepository
                .findById(storeDTO.getUserId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find user with id: "+storeDTO.getUserId()));

        if(storeRepository.existsByUserId(storeDTO.getUserId()) || !existingUser.getIsActive()) {
            throw new DataIntegrityViolationException("User cannot create new store !!!");
        }

        String fileName = storeFile(file);

        Store newStore = Store
                .builder()
                .nameStore(storeDTO.getNameStore())
                .address(storeDTO.getAddress())
                .description(storeDTO.getDescription())
                .phone(storeDTO.getPhone())
                .licenseUrl(fileName)
                .status("PROCESSING")
                .isActive(false)
                .requestDate(LocalDateTime.now().plusHours(7))
                .validDate(LocalDateTime.now().plusHours(7))
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
    public Store getStoreByUserId(int id) throws DataNotFoundException {

        Optional<Store> store = storeRepository.findByUserId(id);

        if (store.isPresent()) {
            return store.get();
        }
        throw new DataNotFoundException("Cannot find store with user id =" + id);
    }

    @Override
    public Page<StoreResponse> getAllStores(String keyword, String status, PageRequest pageRequest) {
        Page<Store> storesPage = storeRepository.searchStores(keyword, status, pageRequest);
        return storesPage.map(StoreResponse::fromStore);
    }

    @Override
    public Page<StoreResponse> getAllStoresAdmin(String keyword, String status, PageRequest pageRequest) {
        Page<Store> storesPage = storeRepository.searchStoresAdmin(keyword, status, pageRequest);
        return storesPage.map(StoreResponse::fromStore);
    }

    @Override
    public Store updateStore(int id, StoreDTO storeDTO) throws IOException {
        // Tìm cửa hàng tồn tại trong DB
        Store existingStore = getStoreById(id);

        if(storeDTO.getNameStore() != null && !storeDTO.getNameStore().isEmpty()) {
            existingStore.setNameStore(storeDTO.getNameStore());
        }
        if(storeDTO.getAddress() != null && !storeDTO.getAddress().isEmpty()) {
            existingStore.setAddress(storeDTO.getAddress());
        }
        if(storeDTO.getDescription() != null && !storeDTO.getDescription().isEmpty()) {
            existingStore.setDescription(storeDTO.getDescription());
        }
        if(storeDTO.getPhone() != null && !storeDTO.getPhone().isEmpty()) {
            existingStore.setPhone(storeDTO.getPhone());
        }

        storeRepository.save(existingStore);
        return existingStore;
    }

    @Override
    public Store updateStatusStore(int id, StoreDTO storeDTO) {
        Store existingStore = getStoreById(id);

        existingStore.setStatus(storeDTO.getStatus());
        existingStore.setActive(storeDTO.isActive());
        existingStore.setValidDate(LocalDateTime.now().plusHours(7).plusMonths(2));

        return storeRepository.save(existingStore);
    }

    @Override
    public Store deleteStore(int id) {
        Store existingStore = getStoreById(id);
        storeRepository.delete(existingStore);
        return existingStore;
    }

    @Override
    public List<Store> findByCurrentMonth(int month) throws DataNotFoundException {
        List<Store> stores = storeRepository.findByDateMonthAndYear(month, LocalDate.now().getYear());
        if (stores.isEmpty()) {
            throw new DataNotFoundException("No stores found for the current month.");
        }
        return stores;
    }

    @Override
    public Boolean checkFileImage(MultipartFile file) {
        Boolean result = false;
        // Kiểm tra kích thước file và định dạng
        if(file.getSize() > 10 * 1024 * 1024 || file.getOriginalFilename() == null) { // Kích thước > 10MB
            return result;
        }
        String contentType = file.getContentType();
        if(contentType == null || !contentType.startsWith("image/")) {
            return result;
        }

        result =true;
        return result;
    }

    @Override
    public String storeFile(MultipartFile file) throws IOException {
        if (!checkFileImage(file)) {
            throw new IOException("Invalid image format");
        }
        //Xu li file name
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String uniqueFilename = filename;

        if (!uniqueFilename.startsWith("License_")) {
            // Thêm gio hen tai vào trước tên file để đảm bảo tên file là duy nhất
            uniqueFilename = "License_" + UUID.randomUUID().toString() + "_" + filename;
        }

        // Đường dẫn đến thư mục mà bạn muốn lưu file
        Path uploadDir = Paths.get(UPLOADS_FOLDER);
        // Kiểm tra và tạo thư mục nếu nó không tồn tại
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        // Đường dẫn đầy đủ đến file
        Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        // Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    @Override
    public void deleteFile(String filename) throws IOException {
        // Đường dẫn đến thư mục chứa file
        Path uploadDir = Paths.get(UPLOADS_FOLDER);
        // Đường dẫn đầy đủ đến file cần xóa
        Path filePath = uploadDir.resolve(filename);

        // Kiểm tra xem file tồn tại hay không
        if (Files.exists(filePath)) {
            // Xóa file
            Files.delete(filePath);
        }
    }

    @Transactional
    @Override
    public Store buyPackageSuccess(int storePackageId) throws Exception {
        Optional<StorePackage> optionalStorePackage = storeAPackageRepository.findById(storePackageId);
        if (optionalStorePackage.isPresent()) {
            StorePackage storePackage = optionalStorePackage.get();
            Optional<Package> optionalPackage = packageRepository.findById(storePackage.getAPackage().getId());
            if (optionalPackage.isPresent()) {
                Package aPackage  = optionalPackage.get();
                Store existingStore = storeRepository
                        .findById(storePackage.getStore().getId())
                        .orElseThrow(() ->
                                new DataNotFoundException(
                                        "Cannot find store with id: "+ storePackage.getStore().getId()));
                LocalDateTime validDate;
                if(existingStore.getValidDate().isBefore(LocalDateTime.now().plusHours(7))){
                    validDate = LocalDateTime.now().plusHours(7).plusMonths(aPackage.getMonth());
                }else{
                    validDate = existingStore.getValidDate().plusMonths(aPackage.getMonth());
                }
                existingStore.setValidDate(validDate);
                return storeRepository.save(existingStore);
            }
            throw new DataNotFoundException("Cannot find Package with id =" + storePackageId);
        }
        throw new DataNotFoundException("Cannot find StorePackage with id =" + storePackageId);
    }
}
