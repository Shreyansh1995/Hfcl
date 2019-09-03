package com.netcomm.hfcl.model;

/**
 * Created by Netcomm on 2/4/2017.
 */

public class AttendanceRequestViewModel {

    private String ApprovalButtonIsVisable;


    private String ApprovalPanelIsVisable;
    private String Attachment1;
    private String Attachment2;
    private String CancelButtonIsVisable;
    private String cancelDate;



    private String CancelStatus;
    private String cancelPanelIsVisable;
    private String department;
    private String designation;
    private String location;
    private String message;
    private String rMDate;
    private String rMPanelIsVisable;
    private String rMRemarks;
    private String rMSatus;
    private String regularizationType;
    private String status;
    private String contactNo;
    private String empId;
    private String empName;
    private String fromDate;
    private String fromtime;
    private String reqDate;
    private String reqId;
    private String reqNo;
    private String resion;
    private String toDate;
    private String totime;
    private String type;

   /* public AttendanceRequestViewModel( String ApprovalButtonIsVisable,String ApprovalPanelIsVisable,String Attachment1,String Attachment2, String cancelDate, String cancelPanelIsVisable,String CancelButtonIsVisable, String department, String designation, String location, String message, String rMDate, String rMPanelIsVisable, String rMRemarks, String rMSatus, String regularizationType, String status, String contactNo, String empId, String empName, String fromDate, String fromtime, String reqDate, String reqId, String reqNo, String resion, String toDate, String totime, String type) {

       this.ApprovalButtonIsVisable=ApprovalButtonIsVisable;
       this.ApprovalPanelIsVisable=ApprovalPanelIsVisable;
       this.Attachment1=Attachment1;
       this.Attachment2=Attachment2;
       this.CancelButtonIsVisable=CancelButtonIsVisable;
        this.cancelDate = cancelDate;
        this.cancelPanelIsVisable = cancelPanelIsVisable;
        this.department = department;
        this.designation = designation;
        this.location = location;
        this.message = message;
        this.rMDate = rMDate;
        this.rMPanelIsVisable = rMPanelIsVisable;
        this.rMRemarks = rMRemarks;
        this.rMSatus = rMSatus;
        this.regularizationType = regularizationType;
        this.status = status;
        this.contactNo = contactNo;
        this.empId = empId;
        this.empName = empName;
        this.fromDate = fromDate;
        this.fromtime = fromtime;
        this.reqDate = reqDate;
        this.reqId = reqId;
        this.reqNo = reqNo;
        this.resion = resion;
        this.toDate = toDate;
        this.totime = totime;
        this.type = type;
    }
*/
    public AttendanceRequestViewModel(String cancelDate,String cancelPanelIsVisable, String CancelStatus,String department, String designation, String location, String message, String rMDate, String rMPanelIsVisable, String rMRemarks, String rMSatus, String regularizationType, String status, String contactNo, String empId, String empName, String fromDate, String fromtime, String reqDate, String reqId, String reqNo, String resion, String toDate, String totime, String type) {
        this.cancelDate = cancelDate;
        this.cancelPanelIsVisable = cancelPanelIsVisable;
        this.CancelStatus = CancelStatus;
        this.department = department;
        this.designation = designation;
        this.location = location;
        this.message = message;
        this.rMDate = rMDate;
        this.rMPanelIsVisable = rMPanelIsVisable;
        this.rMRemarks = rMRemarks;
        this.rMSatus = rMSatus;
        this.regularizationType = regularizationType;
        this.status = status;
        this.contactNo = contactNo;
        this.empId = empId;
        this.empName = empName;
        this.fromDate = fromDate;
        this.fromtime = fromtime;
        this.reqDate = reqDate;
        this.reqId = reqId;
        this.reqNo = reqNo;
        this.resion = resion;
        this.toDate = toDate;
        this.totime = totime;
        this.type = type;

    }

    public String getCancelStatus() {
        return CancelStatus;
    }

    public void setCancelStatus(String cancelStatus) {
        CancelStatus = cancelStatus;
    }
    public String getApprovalButtonIsVisable() {
        return ApprovalButtonIsVisable;
    }

    public void setApprovalButtonIsVisable(String approvalButtonIsVisable) {
        ApprovalButtonIsVisable = approvalButtonIsVisable;
    }

    public String getApprovalPanelIsVisable() {
        return ApprovalPanelIsVisable;
    }

    public void setApprovalPanelIsVisable(String approvalPanelIsVisable) {
        ApprovalPanelIsVisable = approvalPanelIsVisable;
    }

    public String getAttachment1() {
        return Attachment1;
    }

    public void setAttachment1(String attachment1) {
        Attachment1 = attachment1;
    }

    public String getAttachment2() {
        return Attachment2;
    }

    public void setAttachment2(String attachment2) {
        Attachment2 = attachment2;
    }

    public String getCancelButtonIsVisable() {
        return CancelButtonIsVisable;
    }

    public void setCancelButtonIsVisable(String cancelButtonIsVisable) {
        CancelButtonIsVisable = cancelButtonIsVisable;
    }

    public String getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(String cancelDate) {
        this.cancelDate = cancelDate;
    }

    public String getCancelPanelIsVisable() {
        return cancelPanelIsVisable;
    }

    public void setCancelPanelIsVisable(String cancelPanelIsVisable) {
        this.cancelPanelIsVisable = cancelPanelIsVisable;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getrMDate() {
        return rMDate;
    }

    public void setrMDate(String rMDate) {
        this.rMDate = rMDate;
    }

    public String getrMPanelIsVisable() {
        return rMPanelIsVisable;
    }

    public void setrMPanelIsVisable(String rMPanelIsVisable) {
        this.rMPanelIsVisable = rMPanelIsVisable;
    }

    public String getrMRemarks() {
        return rMRemarks;
    }

    public void setrMRemarks(String rMRemarks) {
        this.rMRemarks = rMRemarks;
    }

    public String getrMSatus() {
        return rMSatus;
    }

    public void setrMSatus(String rMSatus) {
        this.rMSatus = rMSatus;
    }

    public String getRegularizationType() {
        return regularizationType;
    }

    public void setRegularizationType(String regularizationType) {
        this.regularizationType = regularizationType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getFromtime() {
        return fromtime;
    }

    public void setFromtime(String fromtime) {
        this.fromtime = fromtime;
    }

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getReqNo() {
        return reqNo;
    }

    public void setReqNo(String reqNo) {
        this.reqNo = reqNo;
    }

    public String getResion() {
        return resion;
    }

    public void setResion(String resion) {
        this.resion = resion;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getTotime() {
        return totime;
    }

    public void setTotime(String totime) {
        this.totime = totime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
