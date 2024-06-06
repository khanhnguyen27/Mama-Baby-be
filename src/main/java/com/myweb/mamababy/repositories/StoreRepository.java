package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.Store;
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
            "(:keyword IS NULL OR :keyword = '' OR s.nameStore LIKE %:keyword%) " +
            "AND (:status IS NULL OR :status = '' OR s.status LIKE %:status%) ")
    Page<Store> searchStores
            (@Param("keyword") String keyword,
             @Param("status") String status,
             Pageable pageable);
}
