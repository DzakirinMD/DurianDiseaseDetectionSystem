package com.example.duriandiseasedetectionsystem.model;

public class Farmer {

    private String farmerID;
    private String farmerName;
    private String farmerAddress;
    private String farmerNoTel;
    private String farmerEmail;
    private String farmerRole;
    private String farmerImage;
    private String farmerPassword;

    public Farmer () {

    }

    public Farmer(String farmerID, String farmerName, String farmerAddress, String farmerNoTel, String farmerEmail, String farmerRole, String farmerImage, String farmerPassword) {
        this.farmerID = farmerID;
        this.farmerName = farmerName;
        this.farmerAddress = farmerAddress;
        this.farmerNoTel = farmerNoTel;
        this.farmerEmail = farmerEmail;
        this.farmerRole = farmerRole;
        this.farmerImage = farmerImage;
        this.farmerPassword = farmerPassword;
    }

    public String getFarmerID() {
        return farmerID;
    }

    public void setFarmerID(String farmerID) {
        this.farmerID = farmerID;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getFarmerAddress() {
        return farmerAddress;
    }

    public void setFarmerAddress(String farmerAddress) {
        this.farmerAddress = farmerAddress;
    }

    public String getFarmerNoTel() {
        return farmerNoTel;
    }

    public void setFarmerNoTel(String farmerNoTel) {
        this.farmerNoTel = farmerNoTel;
    }

    public String getFarmerEmail() {
        return farmerEmail;
    }

    public void setFarmerEmail(String farmerEmail) {
        this.farmerEmail = farmerEmail;
    }

    public String getFarmerRole() {
        return farmerRole;
    }

    public void setFarmerRole(String farmerRole) {
        this.farmerRole = farmerRole;
    }

    public String getFarmerImage() {
        return farmerImage;
    }

    public void setFarmerImage(String farmerImage) {
        this.farmerImage = farmerImage;
    }

    public String getFarmerPassword() {
        return farmerPassword;
    }

    public void setFarmerPassword(String farmerPassword) {
        this.farmerPassword = farmerPassword;
    }
}
