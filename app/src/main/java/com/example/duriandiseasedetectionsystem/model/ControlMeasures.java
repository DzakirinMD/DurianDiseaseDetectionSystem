package com.example.duriandiseasedetectionsystem.model;

public class ControlMeasures {

    private String cmID;
    private String cmName;
    private String cmInstruction;

    public ControlMeasures(){

    }

    public ControlMeasures(String cmID, String cmName, String cmInstruction) {
        this.cmID = cmID;
        this.cmName = cmName;
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

    public String getCmInstruction() {
        return cmInstruction;
    }

    public void setCmInstruction(String cmInstruction) {
        this.cmInstruction = cmInstruction;
    }
}
