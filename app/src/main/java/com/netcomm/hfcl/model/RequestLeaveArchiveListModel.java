package com.netcomm.hfcl.model;

/**
 * Created by Netcomm on 8/29/2017.
 */

public class RequestLeaveArchiveListModel {
    private String empName;
    private String noOfLeave;
    private String period;
    private String rMStatus;
    private String regularisationType;
    private String reqDate;
    private String reqID;
    private String reqNo;
    private String type;

    public RequestLeaveArchiveListModel(String empName, String noOfLeave, String period, String rMStatus, String regularisationType, String reqDate, String reqID, String reqNo, String type) {
        this.empName = empName;
        this.noOfLeave = noOfLeave;
        this.period = period;
        this.rMStatus = rMStatus;
        this.regularisationType = regularisationType;
        this.reqDate = reqDate;
        this.reqID = reqID;
        this.reqNo = reqNo;
        this.type = type;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getNoOfLeave() {
        return noOfLeave;
    }

    public void setNoOfLeave(String noOfLeave) {
        this.noOfLeave = noOfLeave;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getrMStatus() {
        return rMStatus;
    }

    public void setrMStatus(String rMStatus) {
        this.rMStatus = rMStatus;
    }

    public String getRegularisationType() {
        return regularisationType;
    }

    public void setRegularisationType(String regularisationType) {
        this.regularisationType = regularisationType;
    }

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
    }

    public String getReqID() {
        return reqID;
    }

    public void setReqID(String reqID) {
        this.reqID = reqID;
    }

    public String getReqNo() {
        return reqNo;
    }

    public void setReqNo(String reqNo) {
        this.reqNo = reqNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
