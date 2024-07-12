package com.myweb.mamababy.services.Package;

import com.myweb.mamababy.dtos.PackageDTO;
import com.myweb.mamababy.models.Package;

import java.util.List;

public interface IPackageService {
    Package createPackage(PackageDTO packageDTO);

    Package getPackageById(int id);

    List<Package> getAllPackage();

    Package updatePackage(int id, PackageDTO packageDTO);
}
