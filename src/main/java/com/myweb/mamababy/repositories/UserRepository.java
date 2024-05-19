package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUsername (String username);
    Optional<User> findByUsername(String username);
    //SELECT * FROM users WHERE phoneNumber=?
}
