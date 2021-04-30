package com.cs401.storagevault.model.repository;

import com.cs401.storagevault.model.tables.DeviceRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeviceRegistrationRepo extends JpaRepository<DeviceRegistration, Integer> {

    @Query(value = "SELECT * FROM device_registration where email= :email", nativeQuery = true)
    List<DeviceRegistration> findByUserEmail(String email);
}
