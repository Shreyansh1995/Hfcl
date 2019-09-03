package com.netcomm.hfcl.model;

/**
 * Created by Netcomm on 8/29/2017.
 */

public class LeaveBalanceModel {
    private String balanceLeave;
    private String pendingLeave;
    private String takenLeave;
    private String typeOfLeave;
    private String noOfLeave;

    public String getTypeOfLeave() {
        return typeOfLeave;
    }

    public void setTypeOfLeave(String typeOfLeave) {
        this.typeOfLeave = typeOfLeave;
    }

    public String getNoOfLeave() {
        return noOfLeave;
    }

    public void setNoOfLeave(String noOfLeave) {
        this.noOfLeave = noOfLeave;
    }

    public String getBalanceLeave() {
        return balanceLeave;
    }

    public void setBalanceLeave(String balanceLeave) {
        this.balanceLeave = balanceLeave;
    }

    public String getPendingLeave() {
        return pendingLeave;
    }

    public void setPendingLeave(String pendingLeave) {
        this.pendingLeave = pendingLeave;
    }

    public String getTakenLeave() {
        return takenLeave;
    }

    public void setTakenLeave(String takenLeave) {
        this.takenLeave = takenLeave;
    }



}
