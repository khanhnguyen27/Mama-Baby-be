package com.myweb.mamababy.controllers;


import com.myweb.mamababy.dtos.UpdateUserDTO;
import com.myweb.mamababy.dtos.UserDTO;
import com.myweb.mamababy.dtos.UserLoginDTO;
import com.myweb.mamababy.models.Order;
import com.myweb.mamababy.models.User;
import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.responses.order.OrderResponse;
import com.myweb.mamababy.responses.user.UserListResponse;
import com.myweb.mamababy.responses.user.UserResponse;
import com.myweb.mamababy.services.User.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.FieldError;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    //http://localhost:8088/mamababy/users/register
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/register")
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
            User user = userService.createUser(userDTO);
            //return ResponseEntity.ok("Register successfully");
            return ResponseEntity.ok().body(
                    ResponseObject.builder()
                            .message("Create account successfully")
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
        try {
            String token = userService.login(userLoginDTO.getUsername(), userLoginDTO.getPassword());
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
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/details")
    public ResponseEntity<?> getUserDetails(
            @RequestHeader("Authorization") String token
    ) throws Exception {
        String extractedToken = token.substring(7);
        User user = userService.getUserDetailsFromToken(extractedToken);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Get user's detail successfully")
                        .data(UserResponse.fromUser(user))
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/admin/all")
    public ResponseEntity<?> getAllUser() throws Exception {
        List<User> user = userService.getAllAccount();
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Get user's detail successfully")
                        .data(user.stream()
                                .map(UserResponse::fromUser)
                                .collect(Collectors.toList()))
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/all")
    public ResponseEntity<?> getAllUserForAd() throws Exception {
        List<User> user = userService.getAllAccount();
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Get user's detail successfully")
                        .data(user.stream()
                                .map(UserResponse::fromUserForAll)
                                .collect(Collectors.toList()))
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    //http://localhost:8088/mamababy/users
    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("")
    public ResponseEntity<?> updateUser(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody UpdateUserDTO updateUserDTO
    ) throws Exception {
        String extractedToken = token.substring(7);
        User user = userService.updateAccount(extractedToken, updateUserDTO);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Update User successfully")
                        .data(UserResponse.fromUser(user))
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    //http://localhost:8088/mamababy/users/admin
    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/admin")
    public ResponseEntity<?> isActiveUser(
            @Valid @RequestBody UserDTO userDTO
    ) throws Exception {
        User user = userService.isActive(userDTO);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Update User successfully")
                        .data(UserResponse.fromUser(user))
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    //{{API_MM}}/users/get-users-by-keyword?page=0&size=10&keyword=NguyenVanD
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/get-users-by-keyword")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> searchUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<User> userPage = userService.findUserByKeyword(keyword, pageable);
            int totalPages = userPage.getTotalPages();
            List<UserResponse> userResponses = userPage.getContent().stream()
                    .map(UserResponse::fromUser)
                    .toList();
            UserListResponse userListResponse = UserListResponse.builder()
                    .users(userResponses)
                    .totalPages(totalPages)
                    .build();

            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Get users successfully")
                    .data(userListResponse)
                    .status(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error searching users", e);
        }
    }

    // Get By Year
    @GetMapping("/findByYear")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getUsersByYear(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) int year) {
        try {
            List<User> users = userService.findByYear(year);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Orders for year " + year + " found successfully!!!")
                    .data(users.stream()
                            .map(UserResponse::fromUser)
                            .collect(Collectors.toList()))
                    .status(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //http://localhost:8088/mamababy/users/details
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(
            @PathVariable int id
    ) throws Exception {
        User user = userService.getUserById(id);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Get user's detail successfully")
                        .data(UserResponse.fromUser(user))
                        .status(HttpStatus.OK)
                        .build()
        );
    }

}
