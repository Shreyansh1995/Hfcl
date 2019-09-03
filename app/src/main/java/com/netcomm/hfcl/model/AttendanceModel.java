package com.netcomm.hfcl.model;

/**
 * Created by Netcomm on 1/27/2017.
 */

public class AttendanceModel {
    private String color;
    private String dateOffice;
    private String inTime;
    private String outTime;
    private String status;
/*
    public AttendanceModel( String dateOffice, String inTime, String outTime, String status) {

        this.dateOffice = dateOffice;
        this.inTime = inTime;
        this.outTime = outTime;
        this.status = status;
    }*/

    public AttendanceModel(String color,String date, String sInTime, String sOutTime,String status) {
        this.color=color;
        this.dateOffice = date;
        this.inTime = sInTime;
        this.outTime = sOutTime;
        this.status = status;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDateOffice() {
        return dateOffice;
    }

    public void setDateOffice(String dateOffice) {
        this.dateOffice = dateOffice;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
