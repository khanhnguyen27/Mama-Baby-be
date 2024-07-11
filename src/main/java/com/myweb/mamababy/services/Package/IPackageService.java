package com.myweb.mamababy.services.Package;

import com.myweb.mamababy.dtos.PackageDTO;
import com.myweb.mamababy.dtos.ProductDTO;
import com.myweb.mamababy.models.Package;
import com.myweb.mamababy.models.Product;
import com.myweb.mamababy.responses.product.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IPackageService {
    Package createPackage(PackageDTO packageDTO);

    Package getPackageById(int id);

    List<Package> getAllPackage();

    Package updatePackage(int id, PackageDTO packageDTO);
}
