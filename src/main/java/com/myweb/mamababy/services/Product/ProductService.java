package com.myweb.mamababy.services.Product;

import com.myweb.mamababy.dtos.ProductDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.Category;
import com.myweb.mamababy.models.Product;
import com.myweb.mamababy.repositories.AgeRepository;
import com.myweb.mamababy.repositories.BrandRepository;
import com.myweb.mamababy.repositories.CategoryRepository;
import com.myweb.mamababy.responses.product.ProductResponse;
import com.myweb.mamababy.repositories.ProductRepository;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final AgeRepository ageRepository;
    private final BrandRepository brandRepository;

    private static final String UPLOADS_FOLDER = "images";

    @Override
    public Product createProduct(ProductDTO productDTO) throws Exception {
//        Category existingCategory = categoryRepository
//                .findById(productDTO.())
//                .orElseThrow(() ->
//                        new DataNotFoundException(
//                                "Cannot find category with id: "+productDTO.getCategoryId()));
//
//        Product newProduct = Product.builder()
//                .name(productDTO.getName())
//                .price(productDTO.getPrice())
//                .thumbnail(productDTO.getThumbnail())
//                .description(productDTO.getDescription())
//                .category(existingCategory)
//                .build();
//        return productRepository.save(newProduct);
        return null;
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
        //Xử lý tên file
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        //đảm bảo tên file là duy nhất
        Date createAt =new Date();
        String uniqueFilename = createAt.getTime() + "_" +filename;
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
