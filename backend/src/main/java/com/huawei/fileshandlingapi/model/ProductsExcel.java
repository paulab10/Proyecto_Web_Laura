package com.huawei.fileshandlingapi.model;

import java.time.LocalDate;

public class ProductsExcel {
    private String poNumber;

    private String duNumber;

    private LocalDate actualDate;

    private LocalDate planDate;

    private double quantityDV;

    private String description;

    private String region;

    private double billedPercent;

    public double getBilledPercent() {
        return billedPercent;
    }

    public void setBilledPercent(double billedPercent) {
        this.billedPercent = billedPercent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addQuantity(double quantity) {
        this.quantityDV += quantity;
    }

    public double getQuantityDV() {
        return quantityDV;
    }

    public void setQuantityDV(double quantityDV) {
        this.quantityDV = quantityDV;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getDuNumber() {
        return duNumber;
    }

    public void setDuNumber(String duNumber) {
        this.duNumber = duNumber;
    }

    public LocalDate getActualDate() {
        return actualDate;
    }

    public void setActualDate(LocalDate actualDate) {
        this.actualDate = actualDate;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public LocalDate getPlanDate() {
        return planDate;
    }

    public void setPlanDate(LocalDate planDate) {
        this.planDate = planDate;
    }
}
