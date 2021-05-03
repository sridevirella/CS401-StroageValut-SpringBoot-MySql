package com.cs401.storagevault.model.tables;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Pricing {

    @Id
    private int id;
    private char customerType;
    private double  monthlyPrice;
    private double  yearlyPrice;

    public Pricing() {}

    public Pricing(int id, char customerType, double monthlyPrice, double yearlyPrice) {
        this.id = id;
        this.customerType = customerType;
        this.monthlyPrice = monthlyPrice;
        this.yearlyPrice = yearlyPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public char getCustomerType() {
        return customerType;
    }

    public void setCustomerType(char customerType) {
        this.customerType = customerType;
    }

    public double getMonthlyPrice() {
        return monthlyPrice;
    }

    public void setMonthlyPrice(double monthlyPrice) {
        this.monthlyPrice = monthlyPrice;
    }

    public double getYearlyPrice() {
        return yearlyPrice;
    }

    public void setYearlyPrice(double yearlyPrice) {
        this.yearlyPrice = yearlyPrice;
    }
}
