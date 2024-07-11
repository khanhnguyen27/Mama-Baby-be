package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.Order;
import com.myweb.mamababy.models.Package;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageRepository extends JpaRepository<Package, Integer> {

}
