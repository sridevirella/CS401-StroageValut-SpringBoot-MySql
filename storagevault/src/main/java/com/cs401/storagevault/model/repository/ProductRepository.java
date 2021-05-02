package com.cs401.storagevault.model.repository;

import com.cs401.storagevault.model.tables.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
