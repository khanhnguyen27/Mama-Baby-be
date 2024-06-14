package com.myweb.mamababy.services.Brand;

import com.myweb.mamababy.dtos.BrandDTO;
import com.myweb.mamababy.models.Brand;

import java.util.List;

public interface IBrandService {
    Brand createBrand(BrandDTO brandDTO);
    Brand getBrandById(int id);
    List<Brand> getAllBrands();
    Brand updateBrand(int brandId, BrandDTO brandDTO);
    Brand deleteBrand(int id);
    List<Brand> findByIsActiveTrue();
}
