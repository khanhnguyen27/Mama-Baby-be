package com.myweb.mamababy.responses.store;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.myweb.mamababy.models.Store;
import java.time.LocalDate;
import lombok.*;

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
    private String status;
    private LocalDate requestDate;

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
                .requestDate(store.getRequestDate())
                .status(store.getStatus())
                .isActive(store.isActive())
                .userID(store.getUser().getId())
                .build();
    }
}
