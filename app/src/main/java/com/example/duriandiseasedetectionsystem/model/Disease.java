package com.example.duriandiseasedetectionsystem.model;

public class Disease {

    private String diseaseID;
    private String diseaseName;
    private int diseaseSeverity;
    private String diseaseSymptoms;

    public Disease(){

    }

    public Disease(String diseaseID, String diseaseName, int diseaseSeverity, String diseaseSymptoms) {
        this.diseaseID = diseaseID;
        this.diseaseName = diseaseName;
        this.diseaseSeverity = diseaseSeverity;
        this.diseaseSymptoms = diseaseSymptoms;
    }

    public String getDiseaseID() {
        return diseaseID;
    }

    public void setDiseaseID(String diseaseID) {
        this.diseaseID = diseaseID;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public int getDiseaseSeverity() {
        return diseaseSeverity;
    }

    public void setDiseaseSeverity(int diseaseSeverity) {
        this.diseaseSeverity = diseaseSeverity;
    }

    public String getDiseaseSymptoms() {
        return diseaseSymptoms;
    }

    public void setDiseaseSymptoms(String diseaseSymptoms) {
        this.diseaseSymptoms = diseaseSymptoms;
    }
}