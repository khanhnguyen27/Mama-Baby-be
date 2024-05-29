package com.myweb.mamababy.responses.store;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.myweb.mamababy.models.Store;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreResponse {

    private int id;
    private String nameStore;
    private String address;
    private String description;
    private String phone;
    private String status;

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
                .status(store.getStatus())
                .isActive(store.isActive())
                .userID(store.getUser().getId())
                .build();
    }
}
