package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Integer> {

  @Query("SELECT c FROM Brand c WHERE c.isActive = true")
  List<Brand> findAllActiveBrand();

//  @Query("SELECT c FROM Brand c")
//  List<Brand> findAll(Sort c);
}
