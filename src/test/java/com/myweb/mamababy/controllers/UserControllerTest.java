package com.myweb.mamababy.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myweb.mamababy.dtos.UserDTO;
import com.myweb.mamababy.models.Role;
import com.myweb.mamababy.models.User;
import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.responses.user.UserResponse;
import com.myweb.mamababy.services.User.IUserService;
import com.myweb.mamababy.services.User.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserDTO userDTO;
    private User user;
    private ResponseObject responseObject;

    @BeforeEach
    public void initData(){
        // Setup UserDTO
        userDTO = UserDTO.builder()
                .username("HooHellp01234")
                .password("123456789")
                .fullName("hk")
                .address("duong ao 10")
                .phoneNumber("03759357393")
                .build();

        // Setup Role
        Role role = new Role();
        role.setId(1);
        role.setName("CUSTOMER");

        // Setup User
        user = new User();
        user.setUsername("HooHellp01234");
        user.setFullName("hk");
        user.setAddress("duong ao 10");
        user.setPhoneNumber("03759357393");
        user.setAccumulatedPoints(0);
        user.setRole(role);
        user.setIsActive(true);

        // Create UserResponse
        UserResponse userResponse = UserResponse.fromUser(user);

        // Create ResponseObject
        responseObject = ResponseObject.builder()
                .message("Create account successfully")
                .data(userResponse)
                .status(HttpStatus.OK)
                .build();
    }

    @Test
    void createUser_validRequest_success() throws Exception {
        // Given
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(userDTO);

        // Mocking userService.createUser to return user
        Mockito.when(userService.createUser(ArgumentMatchers.any(UserDTO.class)))
                .thenReturn(user);

        // When and Then
        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8088/mamababy/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.message").value("Create account successfully"))
                .andExpect(jsonPath("$.data.username").value("HooHellp01234"))
                .andExpect(jsonPath("$.data.full_name").value("hk"));
    }

    @Test
    void createUser_usernameInvalid_fail() throws Exception {
        // Given
        userDTO.setUsername("HooHe");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(userDTO);

        // Mocking userService.createUser to return user
//        Mockito.when(userService.createUser(ArgumentMatchers.any(UserDTO.class)))
//                .thenReturn(user);

        // When and Then
        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8088/mamababy/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$[0]").value("Username must be at least 6 characters"));
    }
}
