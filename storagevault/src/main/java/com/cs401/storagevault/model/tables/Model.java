package com.cs401.storagevault.model.tables;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Model {

    @Id
    private int id;
    private int brandId;
    private String modelName;

    public Model() {}

    public Model(int id, int brandId, String modelName) {
        this.id = id;
        this.brandId = brandId;
        this.modelName = modelName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}
