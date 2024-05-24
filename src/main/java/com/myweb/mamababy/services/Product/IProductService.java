package com.myweb.mamababy.services.Product;

import com.myweb.mamababy.reponses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import com.myweb.mamababy.dtos.ProductDTO;
import com.myweb.mamababy.models.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws Exception;

    Product getProductById(int id) throws Exception;

    Page<ProductResponse> getAllProducts(String keyword,
                                         int categoryId, int brandId, int age, int storeId, PageRequest pageRequest);

    Product updateProduct(int id, ProductDTO productDTO) throws Exception;

    void deleteProduct(int id);

    boolean existsByName(String name);

    String storeFile(MultipartFile file) throws IOException;

    void deleteFile(String filename) throws IOException;
}
