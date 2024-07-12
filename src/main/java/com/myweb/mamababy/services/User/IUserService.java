package com.myweb.mamababy.services.User;
import com.myweb.mamababy.dtos.UpdateUserDTO;
import com.myweb.mamababy.dtos.UserDTO;
import com.myweb.mamababy.models.User;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;
    String login(String userName, String password) throws Exception;
    User getUserDetailsFromToken(String token) throws Exception;
    User isActive(UserDTO userDTO) throws Exception;
    User updateAccount(String token, UpdateUserDTO updateUserDTO) throws Exception;
    User getUserById(int id);
    List<User> getAllAccount() throws Exception;
    Page<User> findUserByKeyword(String keyword, Pageable pageable) throws Exception;
    List<User> findByYear(int year);

}
