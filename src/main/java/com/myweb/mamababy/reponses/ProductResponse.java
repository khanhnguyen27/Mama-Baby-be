package com.myweb.mamababy.reponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.myweb.mamababy.models.Product;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse extends BaseResponse{

    private int id;
    private String name;
    private float price;
    private float point;
    private String status;
    private String description;
    private String imageUrl;

    @JsonProperty("category_id")
    private int categoryId;

    @JsonProperty("brand_id")
    private int brandId;

    @JsonProperty("age_id")
    private int age;

    @JsonProperty("store_id")
    private int storeId;

    public static ProductResponse fromProduct(Product product) {
        ProductResponse productResponse = ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .point(product.getPoint())
                .status(product.getStatus())
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .categoryId(product.getCategory().getId())
                .brandId(product.getBrand().getId())
                .age(product.getAge().getId())
                .storeId(product.getStore().getId())
                .build();
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        return productResponse;
    }
}
