package com.cs401.storagevault.model.tables;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Pricing {

    @Id
    private int id;
    private char customerType;
    private int  monthlyPrice;
    private int  yearlyPrice;

    public Pricing() {}

    public Pricing(int id, char customerType, int monthlyPrice, int yearlyPrice) {
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

    public int getMonthlyPrice() {
        return monthlyPrice;
    }

    public void setMonthlyPrice(int monthlyPrice) {
        this.monthlyPrice = monthlyPrice;
    }

    public int getYearlyPrice() {
        return yearlyPrice;
    }

    public void setYearlyPrice(int yearlyPrice) {
        this.yearlyPrice = yearlyPrice;
    }
}
