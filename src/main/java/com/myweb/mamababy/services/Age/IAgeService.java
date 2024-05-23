package com.myweb.mamababy.services.Age;

import com.myweb.mamababy.dtos.AgeDTO;
import com.myweb.mamababy.models.Age;

import java.util.List;

public interface IAgeService {
    Age createAge(AgeDTO ageDTO);
    Age getAgeById(int id);
    List<Age> getAllAges();
    Age updateAge(int ageId, AgeDTO ageDTO);
    void deleteAge(int id);
}
