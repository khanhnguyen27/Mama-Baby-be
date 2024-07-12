package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

  @Query("SELECT c FROM Category c WHERE c.isActive = true")
  List<Category> findAllActiveCategories();

//  @Query("SELECT c FROM Category c")
//  List<Category> findAll(Sort c);
}
