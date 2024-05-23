package com.myweb.mamababy.services.Age;

import com.myweb.mamababy.dtos.AgeDTO;
import com.myweb.mamababy.models.Age;
import com.myweb.mamababy.repositories.AgeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgeService implements IAgeService{

    private final AgeRepository ageRepository;

    @Override
    @Transactional
    public Age createAge(AgeDTO ageDTO)
    {
        Age newAge = Age
                .builder()
                .rangeAge(ageDTO.getRangeAge())
                .build();
        return ageRepository.save(newAge);
    }

    @Override
    public Age getAgeById(int id)
    {
        return ageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Range age not found"));
    }

    @Override
    public List<Age> getAllAges()
    {
        return ageRepository.findAll();
    }

    @Override
    @Transactional
    public Age updateAge(int ageId, AgeDTO ageDTO)
    {
        Age existingAge = getAgeById(ageId);
        existingAge.setRangeAge(ageDTO.getRangeAge());
        ageRepository.save(existingAge);
        return existingAge;
    }

    @Override
    @Transactional
    public void deleteAge(int id)
    {
        ageRepository.deleteById(id);
    }
}