package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
