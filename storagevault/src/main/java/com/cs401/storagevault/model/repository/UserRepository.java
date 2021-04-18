package com.cs401.storagevault.model.repository;

import com.cs401.storagevault.model.tables.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    //@Query("SELECT count(*) FROM user WHERE email_id = :email")
    List<User> getByEmail(String email);

}
