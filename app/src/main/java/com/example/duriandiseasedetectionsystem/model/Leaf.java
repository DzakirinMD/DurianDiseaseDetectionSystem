package com.example.duriandiseasedetectionsystem.model;

public class Leaf {

    private String leafID;
    private String leafName;
    private String leafDurian;
    private String leafCharacteristic;

    public Leaf(){

    }

    public Leaf(String leafID, String leafName, String leafDurian, String leafCharacteristic) {
        this.leafID = leafID;
        this.leafName = leafName;
        this.leafDurian = leafDurian;
        this.leafCharacteristic = leafCharacteristic;
    }

    public String getLeafID() {
        return leafID;
    }

    public void setLeafID(String leafID) {
        this.leafID = leafID;
    }

    public String getLeafName() {
        return leafName;
    }

    public void setLeafName(String leafName) {
        this.leafName = leafName;
    }

    public String getLeafDurian() {
        return leafDurian;
    }

    public void setLeafDurian(String leafDurian) {
        this.leafDurian = leafDurian;
    }

    public String getLeafCharacteristic() {
        return leafCharacteristic;
    }

    public void setLeafCharacteristic(String leafCharacteristic) {
        this.leafCharacteristic = leafCharacteristic;
    }
}
