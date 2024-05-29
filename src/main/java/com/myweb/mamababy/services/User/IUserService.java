package com.myweb.mamababy.services.User;
import com.myweb.mamababy.dtos.UserDTO;
import com.myweb.mamababy.models.User;

import java.util.List;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;
    String login(String userName, String password) throws Exception;
    User getUserDetailsFromToken(String token) throws Exception;
    void logout(String token) throws Exception;
    User isActive(UserDTO userDTO) throws Exception;
    User updateAccount(String token, UserDTO userDTO) throws Exception;
    List<User> getAllAccount() throws Exception;
}
