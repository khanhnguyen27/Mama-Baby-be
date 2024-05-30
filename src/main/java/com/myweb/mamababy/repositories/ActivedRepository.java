package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.Actived;
import com.myweb.mamababy.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ActivedRepository extends JpaRepository<Actived, Integer> {
    List<Actived> findByUserId(int id);
}
