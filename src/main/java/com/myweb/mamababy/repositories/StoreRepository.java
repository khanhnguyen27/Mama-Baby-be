package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.Store;
import com.myweb.mamababy.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Integer> {
    boolean existsByUserId (int userId);
    Optional<Store> findByUserId(int userId);

    @Query("SELECT s FROM Store s WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR s.nameStore LIKE %:keyword%) ")
    Page<Store> searchStores
            (@Param("keyword") String keyword,
             Pageable pageable);
}
