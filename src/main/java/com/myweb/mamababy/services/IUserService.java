package com.myweb.mamababy.services;
import com.myweb.mamababy.dtos.UserDTO;
import com.myweb.mamababy.model.User;
import com.myweb.mamababy.exceptions.DataNotFoundException;
public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;
    String login(String userName, String password) throws Exception;
}
