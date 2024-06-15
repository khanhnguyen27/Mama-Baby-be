package com.myweb.mamababy.services;

import com.myweb.mamababy.dtos.UserDTO;
import com.myweb.mamababy.models.Role;
import com.myweb.mamababy.models.User;
import com.myweb.mamababy.repositories.UserRepository;
import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.responses.user.UserResponse;
import com.myweb.mamababy.services.User.UserService;
import org.assertj.core.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

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
        user.setId(57);
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
        //GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        //WHEN
        var response = userService.createUser(userDTO);

        //THEN
        Assertions.assertThat(response.getId()).isEqualTo(user.getId());
        Assertions.assertThat(response.getUsername()).isEqualTo(user.getUsername());
        Assertions.assertThat(response.getFullName()).isEqualTo(user.getFullName());
        Assertions.assertThat(response.getAddress()).isEqualTo(user.getAddress());
        Assertions.assertThat(response.getPhoneNumber()).isEqualTo(user.getPhoneNumber());
        Assertions.assertThat(response.getRole().getName()).isEqualTo(user.getRole().getName());
    }

    @Test
    void createUser_userExisted_fail() throws Exception {
        //GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        //when(userRepository.save(any())).thenReturn(user);

        //WHEN
        var exception = assertThrows(DataIntegrityViolationException.class, () -> userService.createUser(userDTO));

        Assertions.assertThat(exception.getMessage()).containsIgnoringCase("Username already exists");
    }
}
