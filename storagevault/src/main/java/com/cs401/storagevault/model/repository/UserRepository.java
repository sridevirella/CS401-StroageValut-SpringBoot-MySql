package com.cs401.storagevault.model.repository;

import com.cs401.storagevault.model.tables.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT * FROM user WHERE email = :email", nativeQuery = true)
    User findByEmail(String email);

}
