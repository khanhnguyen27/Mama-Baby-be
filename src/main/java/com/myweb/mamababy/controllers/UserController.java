package com.myweb.mamababy.controllers;


import com.myweb.mamababy.dtos.UpdateUserDTO;
import com.myweb.mamababy.dtos.UserDTO;
import com.myweb.mamababy.dtos.UserLoginDTO;
import com.myweb.mamababy.models.User;
import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.responses.user.UserListResponse;
import com.myweb.mamababy.responses.user.UserResponse;
import com.myweb.mamababy.services.User.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    //Lấy thông tin chi tiết khi user đã đăng nhập
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/details")
    public ResponseEntity<?> getUserDetails(
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

    //Chỉ admin mới được sử dụng
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/all")
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

    //http://localhost:8088/mamababy/users/logout
    @CrossOrigin(origins = "http://localhost:3000")
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

    //User chỉ được chỉnh các thông số cơ bản
    //http://localhost:8088/mamababy/users
    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("")
    public ResponseEntity<?> updateUser(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody UpdateUserDTO updateUserDTO
    ) throws Exception {
        String extractedToken = token.substring(7); // Loại bỏ "Bearer " từ chuỗi token
        User user = userService.updateAccount(extractedToken, updateUserDTO);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .message("Update User successfully")
                        .data(UserResponse.fromUser(user))
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    //Admin chỉ được phép block/open account customer, staff và chỉnh role
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
    //tìm kiếm user để dễ quản lý
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

}
