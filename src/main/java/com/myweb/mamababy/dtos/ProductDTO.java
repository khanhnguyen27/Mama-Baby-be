package com.myweb.mamababy.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    @NotBlank(message = "Name is required")
    @JsonProperty("name")
    private String name;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    @Max(value = 10000000, message = "Price must be less than or equal to 10,000,000")
    @JsonProperty("price")
    private float price;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    @Max(value = 1000, message = "Price must be less than or equal to 10,000,000")
    @JsonProperty("point")
    private int point;

    @NotEmpty(message = "Status is required")
    @JsonProperty("status")
    private String status;

    @NotEmpty(message = "Description is required")
    @JsonProperty("description")
    private String description;

    @JsonProperty("image_url")
    private String imageUrl;

    @NotEmpty(message = "Type is required")
    @JsonProperty("type")
    private String type;

    @NotNull(message = "Brand ID is required")
    @JsonProperty("brand_id")
    private int brandId;

    @NotNull(message = "Category ID is required")
    @JsonProperty("category_id")
    private int categoryId;

    @NotNull(message = "Age ID is required")
    @JsonProperty("age_id")
    private int rangeAge;

    @NotNull(message = "Store ID is required")
    @JsonProperty("store_id")
    private int storeId;
}
