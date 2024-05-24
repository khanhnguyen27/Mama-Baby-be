package com.myweb.mamababy.controllers;


import com.myweb.mamababy.dtos.UserDTO;
import com.myweb.mamababy.dtos.UserLoginDTO;
import com.myweb.mamababy.models.User;
import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.responses.user.UserResponse;
import com.myweb.mamababy.services.User.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.FieldError;

import java.util.List;
@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    //http://localhost:8088/mamababy/users/register
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/register")
    //can we register an "admin" user ?
    public ResponseEntity<?> createUser(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult result
    ) {
        try{
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
                return ResponseEntity.badRequest().body("Password does not match");
            }
            User user = userService.createUser(userDTO);
            //return ResponseEntity.ok("Register successfully");
            return ResponseEntity.ok().body(
                    ResponseObject.builder()
                            .message("Get user's detail successfully")
                            .data(UserResponse.fromUser(user))
                            .status(HttpStatus.OK)
                            .build()
            );
        }  catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    //http://localhost:8088/mamababy/users/login
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO) {
        // Kiểm tra thông tin đăng nhập và sinh token
        try {
            String token = userService.login(userLoginDTO.getUsername(), userLoginDTO.getPassword());
            // Trả về token trong response
            //return ResponseEntity.ok(token);
            return ResponseEntity.ok().body(
                    ResponseObject.builder()
                            .message("Login successfully")
                            .data(token)
                            .status(HttpStatus.OK)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //http://localhost:8088/mamababy/users/details
    @GetMapping("/details")
    public ResponseEntity<ResponseObject> getUserDetails(
            @RequestHeader("Authorization") String token
    ) throws Exception {
        String extractedToken = token.substring(7); // Loại bỏ "Bearer " từ chuỗi token
        User user = userService.getUserDetailsFromToken(extractedToken);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Get user's detail successfully")
                        .data(UserResponse.fromUser(user))
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    //http://localhost:8088/mamababy/users/logout
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestHeader("Authorization") String tokenHeader) throws Exception {
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            String token = tokenHeader.substring(7);
            userService.logout(token);
            return ResponseEntity.ok("User logged out successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid token");
        }
    }

}
