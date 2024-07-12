package com.myweb.mamababy.responses.user;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;

import java.time.LocalDateTime;

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

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    @JsonProperty("create_at")
    private LocalDateTime createAt;

    @JsonProperty("role_id")
    private com.myweb.mamababy.models.Role role;

    @JsonProperty("isActive")
    private Boolean isActive;

    public static UserResponse fromUser(com.myweb.mamababy.models.User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .full_name(user.getFullName())
                .address(user.getAddress())
                .phone_number(user.getPhoneNumber())
                .accumulated_points(user.getAccumulatedPoints())
                .createAt(user.getCreateAt())
                .isActive(user.getIsActive())
                .role(user.getRole())
                .build();
    }

    public static UserResponse fromUserForAll(com.myweb.mamababy.models.User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .full_name(user.getFullName())
                .phone_number(user.getPhoneNumber())
                .build();
    }
}
