package com.myweb.mamababy.responses.storePackage;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.myweb.mamababy.models.StorePackage;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StorePackageResponse {

    private int id;

    @JsonProperty("package_id")
    private int packageId;

    @JsonProperty("store_id")
    private int storeId;

    private float price;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    @JsonProperty("buy_date")
    private LocalDateTime buyDate;

    private String status;

    public static StorePackageResponse fromStorePackage(StorePackage storePackage) {
        return StorePackageResponse.builder()
                .id(storePackage.getId())
                .packageId(storePackage.getAPackage().getId())
                .storeId(storePackage.getStore().getId())
                .price(storePackage.getPrice())
                .buyDate(storePackage.getBuyDate())
                .status(storePackage.getStatus())
                .build();
    }

}
