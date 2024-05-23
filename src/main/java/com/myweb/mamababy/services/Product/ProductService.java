package com.myweb.mamababy.services.Product;

import com.myweb.mamababy.dtos.ProductDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.Product;
import com.myweb.mamababy.reponses.ProductResponse;
import com.myweb.mamababy.repositories.ProductRepository;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private static final String UPLOADS_FOLDER = "uploads";

    @Override
    public Product createProduct(ProductDTO productDTO) throws Exception {
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
        return "";
    }

    @Override
    public void deleteFile(String filename) throws IOException {

    }
}
