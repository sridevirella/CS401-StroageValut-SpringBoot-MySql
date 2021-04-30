package com.cs401.storagevault.model.repository;

import com.cs401.storagevault.model.tables.DeviceRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRegistrationRepo extends JpaRepository<DeviceRegistration, Integer> {
}
