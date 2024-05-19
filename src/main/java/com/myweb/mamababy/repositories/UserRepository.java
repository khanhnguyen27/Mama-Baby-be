package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsUserName (String phoneNumber);
    Optional<User> findUserName(String phoneNumber);
    //SELECT * FROM users WHERE phoneNumber=?
}
