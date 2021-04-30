package com.cs401.storagevault.model.tables;

import javax.persistence.*;
import java.io.File;

@Entity
public class DeviceRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rno;
    private String email;
    private String brand;
    @Column(name="brand_model")
    private String brandModel;
    private int capacity;
    private int duration;
    @Column(name="holder_name")
    private String name;
    @Column(name="account_number")
    private int accountNumber;
    @Column(name="routing_number")
    private int routingNumber;
    @Column(name="config_file")
    private File configFile;

    public DeviceRegistration() {}

    public DeviceRegistration(int rno, String email, String brand, String brandModel, int capacity, int duration, String name, int accountNumber, int routingNumber, File configFile) {
        this.rno = rno;
        this.email = email;
        this.brand = brand;
        this.brandModel = brandModel;
        this.capacity = capacity;
        this.duration = duration;
        this.name = name;
        this.accountNumber = accountNumber;
        this.routingNumber = routingNumber;
        this.configFile = configFile;
    }

    public int getRno() {
        return rno;
    }

    public String getEmail() {
        return email;
    }

    public String getBrand() {
        return brand;
    }

    public String getBrandModel() {
        return brandModel;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getDuration() {
        return duration;
    }

    public String getName() {
        return name;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public int getRoutingNumber() {
        return routingNumber;
    }

    public File getConfigFile() {
        return configFile;
    }

    public void setRno(int rno) {
        this.rno = rno;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setBrandModel(String brandModel) {
        this.brandModel = brandModel;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setRoutingNumber(int routingNumber) {
        this.routingNumber = routingNumber;
    }

    public void setConfigFile(File configFile) {
        this.configFile = configFile;
    }

    @Override
    public String toString() {
        return "DeviceRegistration{" +
                "rno=" + rno +
                ", email='" + email + '\'' +
                ", brand='" + brand + '\'' +
                ", brandModel='" + brandModel + '\'' +
                ", capacity=" + capacity +
                ", duration=" + duration +
                ", name='" + name + '\'' +
                ", accountNumber=" + accountNumber +
                ", routingNumber=" + routingNumber +
                ", configFile=" + configFile +
                '}';
    }
}
