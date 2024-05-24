package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.Age;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgeRepository extends JpaRepository<Age, Integer> {
}
