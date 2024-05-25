package com.myweb.mamababy.services.Product;

import com.myweb.mamababy.dtos.ProductDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.*;
import com.myweb.mamababy.repositories.*;
import com.myweb.mamababy.responses.product.ProductResponse;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final AgeRepository ageRepository;
    private final StoreRepository storeRepository;
    private static final String UPLOADS_FOLDER = "uploads";

    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
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
                .findById(productDTO.getStoreID())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find store with id: "+productDTO.getStoreID()));

        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .point(productDTO.getPoint())
                .status(productDTO.getStatus())
                .description(productDTO.getDescription())
                .type(productDTO.getType())
                .imageUrl(productDTO.getImageUrl())
                .brand(existingBrand)
                .category(existingCategory)
                .age(existingAge)
                .store(existingStore)
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
    public Page<ProductResponse> getAllProducts(String keyword, int categoryId, int brandId, int age, int storeId, PageRequest pageRequest) {
        Page<Product> productsPage= productRepository.searchProducts(keyword, categoryId, brandId, age, pageRequest);
        return productsPage.map(ProductResponse::fromProduct);
    }


    @Override
    public Product updateProduct(int id, ProductDTO productDTO) throws Exception {
        return null;
    }

    @Override
    public void deleteProduct(int id) {

    }

    @Override
    public boolean existsByName(String name) {
        return false;
    }

    @Override
    public String storeFile(MultipartFile file) throws IOException {
        //Xu li file name
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        // Thêm UUID vào trước tên file để đảm bảo tên file là duy nhất
        //String uniqueFilename = UUID.randomUUID().toString() + "_" + filename; //old code, not good
        Date createAt =new Date();
        String uniqueFilename = createAt.getTime() + "_" + filename; // Convert nanoseconds to microseconds
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

    }
}
