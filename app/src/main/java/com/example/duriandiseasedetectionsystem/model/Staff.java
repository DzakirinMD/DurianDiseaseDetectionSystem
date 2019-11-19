package com.example.duriandiseasedetectionsystem.model;

public class Staff {

    private String staffID;
    private String staffName;
    private String staffAddress;
    private String staffNoTel;
    private String staffEmail;
    private String staffPassword;

    public Staff(){

    }

    public Staff(String staffID, String staffName, String staffAddress, String staffNoTel, String staffEmail, String staffPassword) {
        this.staffID = staffID;
        this.staffName = staffName;
        this.staffAddress = staffAddress;
        this.staffNoTel = staffNoTel;
        this.staffEmail = staffEmail;
        this.staffPassword = staffPassword;
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffAddress() {
        return staffAddress;
    }

    public void setStaffAddress(String staffAddress) {
        this.staffAddress = staffAddress;
    }

    public String getStaffNoTel() {
        return staffNoTel;
    }

    public void setStaffNoTel(String staffNoTel) {
        this.staffNoTel = staffNoTel;
    }

    public String getStaffEmail() {
        return staffEmail;
    }

    public void setStaffEmail(String staffEmail) {
        this.staffEmail = staffEmail;
    }

    public String getStaffPassword() {
        return staffPassword;
    }

    public void setStaffPassword(String staffPassword) {
        this.staffPassword = staffPassword;
    }
}
