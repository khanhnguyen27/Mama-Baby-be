package com.myweb.mamababy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StorePackageDTO {

    @JsonProperty("package_id")
    private int packageId;

    @JsonProperty("store_id")
    private int storeId;

    @JsonProperty("price")
    private float price;
}
