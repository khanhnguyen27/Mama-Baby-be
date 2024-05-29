package com.myweb.mamababy.responses.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.myweb.mamababy.models.Product;
import com.myweb.mamababy.responses.BaseResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse extends BaseResponse {

    private int id;
    private String name;
    private float price;
    private float point;
    private String status;
    private String type;
    private String description;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("category_id")
    private int categoryId;

    @JsonProperty("brand_id")
    private int brandId;

    @JsonProperty("age_id")
    private int age;

    @JsonProperty("store_id")
    private int storeId;

    @JsonProperty("is_active")
    private boolean isActive;

    public static ProductResponse fromProduct(Product product) {
        ProductResponse productResponse = ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .point(product.getPoint())
                .status(product.getStatus())
                .type(product.getType())
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .categoryId(product.getCategory().getId())
                .brandId(product.getBrand().getId())
                .age(product.getAge().getId())
                .storeId(product.getStore().getId())
                .isActive(product.isActive())
                .build();
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        return productResponse;
    }
}
