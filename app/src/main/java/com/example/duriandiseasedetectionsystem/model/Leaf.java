package com.example.duriandiseasedetectionsystem.model;

public class Leaf {

    private String leafID;
    private String leafImage;
    private String leafCharacteristics;

    public Leaf(){

    }

    public Leaf(String leafID, String leafImage, String leafCharacteristics) {
        this.leafID = leafID;
        this.leafImage = leafImage;
        this.leafCharacteristics = leafCharacteristics;
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

    public String getleafCharacteristics() {
        return leafCharacteristics;
    }

    public void setleafCharacteristics(String leafCharacteristics) {
        this.leafCharacteristics = leafCharacteristics;
    }
}
