package com.example.duriandiseasedetectionsystem.model;

public class ControlMeasures {

    private String cmID;
    private String cmName;
    private String cmDisease;
    private int cmSeverity;
    private String cmImage;
    private String cmInstruction;

    public ControlMeasures(){

    }

    public ControlMeasures(String cmID, String cmName, String cmDisease, int cmSeverity, String cmImage, String cmInstruction) {
        this.cmID = cmID;
        this.cmName = cmName;
        this.cmDisease = cmDisease;
        this.cmSeverity = cmSeverity;
        this.cmImage = cmImage;
        this.cmInstruction = cmInstruction;
    }

    public String getCmID() {
        return cmID;
    }

    public void setCmID(String cmID) {
        this.cmID = cmID;
    }

    public String getCmName() {
        return cmName;
    }

    public void setCmName(String cmName) {
        this.cmName = cmName;
    }

    public String getCmDisease() {
        return cmDisease;
    }

    public void setCmDisease(String cmDisease) {
        this.cmDisease = cmDisease;
    }

    public int getCmSeverity() {
        return cmSeverity;
    }

    public void setCmSeverity(int cmSeverity) {
        this.cmSeverity = cmSeverity;
    }

    public String getCmImage() {
        return cmImage;
    }

    public void setCmImage(String cmImage) {
        this.cmImage = cmImage;
    }

    public String getCmInstruction() {
        return cmInstruction;
    }

    public void setCmInstruction(String cmInstruction) {
        this.cmInstruction = cmInstruction;
    }
}
