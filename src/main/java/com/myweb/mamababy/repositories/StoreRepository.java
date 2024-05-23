package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.Store;
import com.myweb.mamababy.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Integer> {
    boolean existsByUserId (int userId);
    Optional<Store> findByUserId(int userId);
}
