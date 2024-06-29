package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.Order;
import com.myweb.mamababy.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUsername (String username);
    Optional<User> findByUsername(String username);
    //SELECT * FROM users WHERE phoneNumber=?
    boolean existsByPhoneNumber(String phoneNumber);
    User findById(Long id);
    @Query("SELECT u FROM User u WHERE :keyword IS NULL OR :keyword = '' OR u.username LIKE %:keyword% OR u.fullName LIKE %:keyword% OR u.phoneNumber LIKE %:keyword% OR u.address LIKE %:keyword%")
    Page<User> searchUsers(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT u FROM User u WHERE YEAR(u.createAt) = :year")
    List<User> findByUserDateYear(int year);
}
