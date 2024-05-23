package com.myweb.mamababy.responses.user;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.myweb.mamababy.models.Role;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    @JsonProperty("id")
    private int id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("full_name")
    private String full_name;

    @JsonProperty("address")
    private String address;

    @JsonProperty("phone_number")
    private String phone_number;

    @JsonProperty("accumulated_points")
    private int accumulated_points;

    @JsonProperty("role_id")
    private com.myweb.mamababy.models.Role role;

    public static UserResponse fromUser(com.myweb.mamababy.models.User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .full_name(user.getFullName())
                .address(user.getAddress())
                .phone_number(user.getPhoneNumber())
                .accumulated_points(user.getAccumulatedPoints())
                .role(user.getRole())
                .build();
    }
}
