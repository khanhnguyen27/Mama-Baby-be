package com.myweb.mamababy.services.Brand;

import com.myweb.mamababy.dtos.BrandDTO;
import com.myweb.mamababy.models.Brand;
import com.myweb.mamababy.repositories.BrandRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService implements IBrandService{

    private final BrandRepository brandRepository;

    @Override
    @Transactional
    public Brand createBrand(BrandDTO brandDTO)
    {
        Brand newBrand = Brand
                .builder()
                .name(brandDTO.getName())
                .isActive(true)
                .build();
        return brandRepository.save(newBrand);
    }

    @Override
    public Brand getBrandById(int id)
    {
        return brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found"));
    }

    @Override
    public List<Brand> getAllBrands()
    {
        return brandRepository.findAll();
    }

    @Override
    @Transactional
    public Brand updateBrand(int brandId, BrandDTO brandDTO)
    {
        Brand existingBrand = getBrandById(brandId);
        existingBrand.setName(brandDTO.getName());
        existingBrand.setActive(brandDTO.isActive());
        brandRepository.save(existingBrand);
        return existingBrand;
    }

    @Override
    @Transactional
    public Brand deleteBrand(int id)
    {
        Brand existingBrand = getBrandById(id);
        existingBrand.setActive(false);
        brandRepository.save(existingBrand);
        return existingBrand;
    }
}
