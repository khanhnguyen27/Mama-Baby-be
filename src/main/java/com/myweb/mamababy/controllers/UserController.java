package com.myweb.mamababy.controllers;


import com.myweb.mamababy.dtos.UserDTO;
import com.myweb.mamababy.dtos.UserLoginDTO;
import com.myweb.mamababy.models.User;
import com.myweb.mamababy.services.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.FieldError;

import java.util.List;
@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
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
            return ResponseEntity.ok(user);
        }  catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    //http://localhost:8088/mamababy/users/login
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO) {
        // Kiểm tra thông tin đăng nhập và sinh token
        try {
            String token = userService.login(userLoginDTO.getUsername(), userLoginDTO.getPassword());
            // Trả về token trong response
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
