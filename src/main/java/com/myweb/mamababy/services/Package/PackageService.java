package com.myweb.mamababy.services.Package;

import com.myweb.mamababy.dtos.PackageDTO;
import com.myweb.mamababy.models.Package;
import com.myweb.mamababy.repositories.PackageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PackageService implements IPackageService{

    private final PackageRepository packageRepository;

    @Override
    @Transactional
    public Package createPackage(PackageDTO packageDTO) {
        Package newPackage = Package
                .builder()
                .packageName(packageDTO.getPackageName())
                .description(packageDTO.getDescription())
                .price(packageDTO.getPrice())
                .month(packageDTO.getMonth())
                .build();
        return packageRepository.save(newPackage);
    }

    @Override
    public Package getPackageById(int id) {
        return packageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found"));
    }

    @Override
    public List<Package> getAllPackage() {
        return packageRepository.findAll(Sort.by(Sort.Direction.ASC, "month"));
    }

    @Override
    @Transactional
    public Package updatePackage(int id, PackageDTO packageDTO) {
        Package existingPackage = getPackageById(id);
        existingPackage.setPackageName(packageDTO.getPackageName());
        existingPackage.setDescription(packageDTO.getDescription());
        existingPackage.setPrice(packageDTO.getPrice());
        existingPackage.setMonth(packageDTO.getMonth());
        return packageRepository.save(existingPackage);
    }
}
