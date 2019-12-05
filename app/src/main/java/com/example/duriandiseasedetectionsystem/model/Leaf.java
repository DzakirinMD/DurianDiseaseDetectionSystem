package com.example.duriandiseasedetectionsystem.model;

public class Leaf {

    private String leafID;
    private String leafImage;
    private String leafCharacteristic;

    public Leaf(){

    }

    public Leaf(String leafID, String leafImage, String leafCharacteristic) {
        this.leafID = leafID;
        this.leafImage = leafImage;
        this.leafCharacteristic = leafCharacteristic;
    }

    public String getLeafID() {
        return leafID;
    }

    public void setLeafID(String leafID) {
        this.leafID = leafID;
    }

    public String getLeafImage() {
        return leafImage;
    }

    public void setLeafImage(String leafImage) {
        this.leafImage = leafImage;
    }

    public String getLeafCharacteristic() {
        return leafCharacteristic;
    }

    public void setLeafCharacteristic(String leafCharacteristic) {
        this.leafCharacteristic = leafCharacteristic;
    }
}
