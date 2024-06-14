package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.Age;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AgeRepository extends JpaRepository<Age, Integer> {

  @Query("SELECT c FROM Age c WHERE c.isActive = true")
  List<Age> findAllActiveAge();
}
