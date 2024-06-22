package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.Brand;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BrandRepository extends JpaRepository<Brand, Integer> {

  @Query("SELECT c FROM Brand c WHERE c.isActive = true")
  List<Brand> findAllActiveBrand();

//  @Query("SELECT c FROM Brand c")
//  List<Brand> findAll(Sort c);
}
