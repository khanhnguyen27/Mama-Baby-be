package com.myweb.mamababy.services.Product;

import com.myweb.mamababy.responses.product.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import com.myweb.mamababy.dtos.ProductDTO;
import com.myweb.mamababy.models.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IProductService {
    Product createProduct(ProductDTO productDTO, MultipartFile file) throws Exception;

    Product getProductById(int id) throws Exception;

    Page<ProductResponse> getProductByStoreId(String keyword,
                                              int categoryId, int brandId, int age, int storeId, PageRequest pageRequest) throws Exception;

    Page<ProductResponse> getAllProducts(String keyword, String type,
                                         int categoryId, int brandId, int age, int storeId, PageRequest pageRequest);
    List<ProductResponse> getAllProductsCH(String type);

    Product updateProduct(int id, ProductDTO productDTO, MultipartFile file) throws Exception;

    Product deleteProduct(int id) throws Exception;

    boolean existsByName(String name);

    Boolean checkFileImage(MultipartFile file);

    String storeFile(MultipartFile file) throws IOException;

    void deleteFile(String filename) throws IOException;
}
