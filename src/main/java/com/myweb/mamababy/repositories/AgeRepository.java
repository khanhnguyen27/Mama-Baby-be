package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.Age;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AgeRepository extends JpaRepository<Age, Integer> {

  @Query("SELECT c FROM Age c WHERE c.isActive = true")
  List<Age> findAllActiveAge();

//  @Query("SELECT c FROM Age c")
//  List<Age> findAll(Sort c);
}
