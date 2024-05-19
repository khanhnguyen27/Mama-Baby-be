package com.myweb.mamababy.services;
import com.myweb.mamababy.dtos.UserDTO;
import com.myweb.mamababy.models.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;
    String login(String userName, String password) throws Exception;
}
