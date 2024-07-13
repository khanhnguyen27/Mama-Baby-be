package com.myweb.mamababy.services.Product;

import com.myweb.mamababy.dtos.ProductDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.*;
import com.myweb.mamababy.repositories.*;
import com.myweb.mamababy.responses.product.ProductResponse;
import com.myweb.mamababy.services.Store.IStoreService;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final AgeRepository ageRepository;
    private final StoreRepository storeRepository;
    private final IStoreService storeService;
    private static final String UPLOADS_FOLDER = "uploads";

    @Override
    @Transactional
    public Product createProduct(ProductDTO productDTO, MultipartFile file) throws DataNotFoundException, IOException {
        Category existingCategory = categoryRepository
                .findById(productDTO.getCategoryId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find category with id: "+productDTO.getCategoryId()));
        Brand existingBrand = brandRepository
                .findById(productDTO.getBrandId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find brand with id: "+productDTO.getBrandId()));
        Age existingAge = ageRepository
                .findById(productDTO.getRangeAge())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find range age with id: "+productDTO.getRangeAge()));
        Store existingStore = storeRepository
                .findById(productDTO.getStoreId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find store with id: "+productDTO.getStoreId()));

        if(!existingCategory.isActive() ||!existingBrand.isActive() || !existingAge.isActive() || !existingStore.isActive() || existingStore.getValidDate().isBefore(LocalDateTime.now().plusHours(7))){
            throw new DataIntegrityViolationException("Cannot create new product with not active value!!!");
        }

        String fileName = storeFile(file);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate expiryDate = LocalDate.parse(productDTO.getExpiryDate(), formatter);


        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .point(productDTO.getPoint())
                .remain(productDTO.getRemain())
                .status(productDTO.getStatus())
                .description(productDTO.getDescription())
                .expiry_date(expiryDate)
                .type(productDTO.getType())
                .imageUrl(fileName)
                .brand(existingBrand)
                .category(existingCategory)
                .age(existingAge)
                .store(existingStore)
                .isActive(true)
                .comments(null)
                .build();
        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(int id) throws Exception {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        }
        throw new DataNotFoundException("Cannot find product with id =" + id);
    }

    @Override
    public Page<ProductResponse> getProductByStoreId(String keyword, int categoryId, int brandId, int age, int storeId, PageRequest pageRequest)throws Exception {
        Page<Product> productsPage= productRepository.searchProductsByStore(keyword, categoryId, brandId, age, storeId, pageRequest);
        return productsPage.map(ProductResponse::fromProduct);
    }

    @Override
    public Page<ProductResponse> getAllProducts(String keyword, String type, int categoryId, int brandId, int age, int storeId, PageRequest pageRequest) {
        Page<Product> productsPage= productRepository.searchProducts(keyword, type, categoryId, brandId, age, storeId, pageRequest);
        return productsPage.map(ProductResponse::fromProduct);
    }

    @Override
    public List<ProductResponse> getAllProductsCH(String type) {
        List<Product> products = productRepository.searchProductsCH(type);
        List<ProductResponse> productResponses = products.stream()
                .map(ProductResponse::fromProduct) // Map Product to ProductResponse
                .collect(Collectors.toList());    // Collect into a List

        return productResponses;
    }


    @Override
    @Transactional
    public Product updateProduct(int id, ProductDTO productDTO, MultipartFile file) throws Exception {
        Product existingProduct = getProductById(id);
        if(existingProduct != null) {
            Category existingCategory = categoryRepository
                    .findById(productDTO.getCategoryId())
                    .orElseThrow(() ->
                            new DataNotFoundException(
                                    "Cannot find category with id: "+productDTO.getCategoryId()));
            Brand existingBrand = brandRepository
                    .findById(productDTO.getBrandId())
                    .orElseThrow(() ->
                            new DataNotFoundException(
                                    "Cannot find brand with id: "+productDTO.getBrandId()));
            Age existingAge = ageRepository
                    .findById(productDTO.getRangeAge())
                    .orElseThrow(() ->
                            new DataNotFoundException(
                                    "Cannot find range age with id: "+productDTO.getRangeAge()));
            Store existingStore = storeRepository
                    .findById(productDTO.getStoreId())
                    .orElseThrow(() ->
                            new DataNotFoundException(
                                    "Cannot find store with id: "+productDTO.getStoreId()));

            if(!existingCategory.isActive() ||!existingBrand.isActive() || !existingAge.isActive() || !existingStore.isActive() || existingStore.getValidDate().isBefore(LocalDateTime.now().plusHours(7))){
                throw new DataIntegrityViolationException("Cannot create new product with not active value!!!");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate expiryDate = LocalDate.parse(productDTO.getExpiryDate(), formatter);


            existingProduct.setCategory(existingCategory);
            existingProduct.setBrand(existingBrand);
            existingProduct.setAge(existingAge);
            existingProduct.setIsActive(productDTO.getIsActive());
            existingProduct.setUpdatedAt(LocalDateTime.now().plusHours(7));
            existingProduct.setExpiry_date(expiryDate);
            //existingProduct.setStore(existingStore);

            if(productDTO.getName() != null && !productDTO.getName().isEmpty()) {
                existingProduct.setName(productDTO.getName());
            }

            if(productDTO.getPrice() >= 0) {
                existingProduct.setPrice(productDTO.getPrice());
            }
            if(productDTO.getPoint() >= 0) {
                existingProduct.setPoint(productDTO.getPoint());
            }
            if(productDTO.getRemain() >= 0) {
                existingProduct.setRemain(productDTO.getRemain());
            }
            if(productDTO.getStatus() != null &&
                    !productDTO.getStatus().isEmpty()) {
                existingProduct.setStatus(productDTO.getStatus());
            }
            if(productDTO.getType() != null &&
                    !productDTO.getType().isEmpty()) {
                existingProduct.setType(productDTO.getType());
            }
            if(productDTO.getDescription() != null &&
                    !productDTO.getDescription().isEmpty()) {
                existingProduct.setDescription(productDTO.getDescription());
            }

            if(file != null && !file.isEmpty()) {
                deleteFile(existingProduct.getImageUrl());
                String fileName = storeFile(file);
                existingProduct.setImageUrl(fileName);
            }


            return productRepository.save(existingProduct);
        }
        return null;
    }

    @Override
    @Transactional
    public Product deleteProduct(int id) throws Exception {
        Product existingProduct = getProductById(id);
        existingProduct.setIsActive(false);
        return productRepository.save(existingProduct);

    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public Boolean checkFileImage(MultipartFile file) {
        Boolean result = false;

        if(file.getSize() > 10 * 1024 * 1024 || file.getOriginalFilename() == null) {
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

        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String uniqueFilename = "Product_" + UUID.randomUUID().toString() + "_" + filename;

        Path uploadDir = Paths.get(UPLOADS_FOLDER);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;

    }

    @Override
    public void deleteFile(String filename) throws IOException {
        Path uploadDir = Paths.get(UPLOADS_FOLDER);
        Path filePath = uploadDir.resolve(filename);

        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }

    @Override
    public Page<ProductResponse> getProductsByType(String keyword, int categoryId, int brandId, int rangeAge, int storeId, String type, Pageable pageable) {
        Page<Product> products = productRepository.searchProductsByType(keyword, categoryId, brandId, rangeAge, storeId, type, pageable);
        return products.map(ProductResponse::fromProduct);
    }
}
