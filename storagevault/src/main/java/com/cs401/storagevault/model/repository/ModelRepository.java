package com.cs401.storagevault.model.repository;

import com.cs401.storagevault.model.tables.Model;
import com.cs401.storagevault.model.tables.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface ModelRepository extends JpaRepository<Model, Integer> {

    @Query(value = " select m.modelName from brand as b, model as m \n" +
            " where b.id = m.brandId and b.brandName = :brandName", nativeQuery = true)
    List<String> findByBrandName(String brandName);
}
