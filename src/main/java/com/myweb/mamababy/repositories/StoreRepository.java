package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.Store;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Integer> {
    boolean existsByUserId (int userId);

    Optional<Store> findByUserId(int userId);

    @Query("SELECT s FROM Store s " +
        "WHERE MONTH(s.requestDate) = :month AND YEAR(s.requestDate) = :year")
    List<Store> findByDateMonthAndYear(
        @Param("month") int month,
        @Param("year") int year
    );

    @Query("SELECT s FROM Store s WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR s.nameStore LIKE %:keyword%) " +
            "AND (:status IS NULL OR :status = '' OR s.status LIKE %:status%) " +
            "AND s.isActive = true")
    Page<Store> searchStores
            (@Param("keyword") String keyword,
             @Param("status") String status,
             Pageable pageable);
}
