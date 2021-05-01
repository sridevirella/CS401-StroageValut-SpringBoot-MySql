package com.cs401.storagevault.model.repository;

import com.cs401.storagevault.model.tables.SpaceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SpaceRequestRepository extends JpaRepository<SpaceRequest, String> {

    @Query(value = "SELECT price FROM space_request WHERE email = :email", nativeQuery = true)
    String getPrice(String email);
}
