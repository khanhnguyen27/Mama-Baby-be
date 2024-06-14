package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

  @Query("SELECT c FROM Category c WHERE c.isActive = true")
  List<Category> findAllActiveCategories();
}
