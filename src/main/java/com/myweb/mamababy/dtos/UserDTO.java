package com.myweb.mamababy.dtos;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    @JsonProperty("username")
    @NotBlank(message = "Username is mandatory")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("fullName")
    @NotBlank(message = "Full Name number is required")
    private String fullName;

    @JsonProperty("address")
    @NotBlank(message = "Address number is required")
    private String address;

    @JsonProperty("phoneNumber")
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @JsonProperty("status")
    private Boolean status;

    @JsonProperty("roleId")
    private int roleId;

}
