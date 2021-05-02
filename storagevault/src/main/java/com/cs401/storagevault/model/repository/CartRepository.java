package com.cs401.storagevault.model.repository;

import com.cs401.storagevault.model.tables.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, String> {
}
