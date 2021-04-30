package com.cs401.storagevault.model.repository;

import com.cs401.storagevault.model.tables.Brand;
import com.cs401.storagevault.model.tables.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Integer> {

    @Query(value = "SELECT brandName FROM brand", nativeQuery = true)
    List<String> findAllBrands();
}
