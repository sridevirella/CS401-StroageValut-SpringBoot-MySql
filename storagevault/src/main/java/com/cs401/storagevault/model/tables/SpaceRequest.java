package com.cs401.storagevault.model.tables;

import javax.persistence.*;

@Entity
public class SpaceRequest {

    @Id
    private String email;
    private int capacity;
    private String price;
    @Column(name="used_space")
    private int usedSpace;

    public SpaceRequest() {}

    public SpaceRequest(String email, int capacity, String price, int usedSpace) {
        this.email = email;
        this.capacity = capacity;
        this.price = price;
        this.usedSpace = usedSpace;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getUsedSpace() {
        return usedSpace;
    }

    public void setUsedSpace(int usedSpace) {
        this.usedSpace = usedSpace;
    }
}
