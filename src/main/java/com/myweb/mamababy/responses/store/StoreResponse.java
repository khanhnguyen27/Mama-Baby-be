package com.myweb.mamababy.responses.store;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.myweb.mamababy.models.Store;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreResponse {

    private int id;

    @JsonProperty("name_store")
    private String nameStore;

    private String address;
    private String description;
    private String phone;

    @JsonProperty("license_url")
    private String licenseUrl ;
    private String status;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    @JsonProperty("request_date")
    private LocalDateTime requestDate;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    @JsonProperty("valid_date")
    private LocalDateTime validDate;

    @JsonProperty("is_active")
    private boolean isActive;

    @JsonProperty("user_id")
    private int userID;

    public static StoreResponse fromStore(Store store){
        return StoreResponse.builder()
                .id(store.getId())
                .nameStore(store.getNameStore())
                .address(store.getAddress())
                .description(store.getDescription())
                .phone(store.getPhone())
                .licenseUrl(store.getLicenseUrl())
                .requestDate(store.getRequestDate())
                .validDate(store.getValidDate())
                .status(store.getStatus())
                .isActive(store.isActive())
                .userID(store.getUser().getId())
                .build();
    }
}
