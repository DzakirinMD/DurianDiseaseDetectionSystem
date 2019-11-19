package com.example.duriandiseasedetectionsystem.model;

public class Durian {

    private String dId;
    private String dName;
    private String dSpecies;
    private String dCharacteristic;

    public Durian () {

    }

    public Durian(String dId, String dName, String dSpecies, String dCharacteristic) {
        this.dId = dId;
        this.dName = dName;
        this.dSpecies = dSpecies;
        this.dCharacteristic = dCharacteristic;
    }

    public String getdId() {
        return dId;
    }

    public void setdId(String dId) {
        this.dId = dId;
    }

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public String getdSpecies() {
        return dSpecies;
    }

    public void setdSpecies(String dSpecies) {
        this.dSpecies = dSpecies;
    }

    public String getdCharacteristic() {
        return dCharacteristic;
    }

    public void setdCharacteristic(String dCharacteristic) {
        this.dCharacteristic = dCharacteristic;
    }
}
