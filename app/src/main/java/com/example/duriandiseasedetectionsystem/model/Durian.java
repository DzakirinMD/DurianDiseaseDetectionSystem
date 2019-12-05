package com.example.duriandiseasedetectionsystem.model;

public class Durian {

    private String durianID;
    private String durianName;
    private String durianSpecies;
    private String durianImage;
    private String durianCharacteristic;
    private String farmerId;

    public Durian () {

    }

    public Durian(String durianID, String durianName, String durianSpecies, String durianImage, String durianCharacteristic, String farmerId) {
        this.durianID = durianID;
        this.durianName = durianName;
        this.durianSpecies = durianSpecies;
        this.durianImage = durianImage;
        this.durianCharacteristic = durianCharacteristic;
        this.farmerId = farmerId;
    }

    public String getdurianID() {
        return durianID;
    }

    public void setdurianID(String durianID) {
        this.durianID = durianID;
    }

    public String getdurianName() {
        return durianName;
    }

    public void setdurianName(String durianName) {
        this.durianName = durianName;
    }

    public String getdurianSpecies() {
        return durianSpecies;
    }

    public void setdurianSpecies(String durianSpecies) {
        this.durianSpecies = durianSpecies;
    }

    public String getdurianCharacteristic() {
        return durianCharacteristic;
    }

    public void setdurianCharacteristic(String durianCharacteristic) {
        this.durianCharacteristic = durianCharacteristic;
    }

    public String getdurianImage() {
        return durianImage;
    }

    public void setdurianImage(String durianImage) {
        this.durianImage = durianImage;
    }

    public String getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(String farmerId) {
        this.farmerId = farmerId;
    }
}