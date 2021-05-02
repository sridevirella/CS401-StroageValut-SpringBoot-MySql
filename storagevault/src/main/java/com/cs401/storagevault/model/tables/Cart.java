package com.cs401.storagevault.model.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Cart {

    @Id
    private String email;
    @Column(name = "total_price")
    private double price;

    public Cart() {}

    public Cart(String email, double price) {
        this.email = email;
        this.price = price;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
