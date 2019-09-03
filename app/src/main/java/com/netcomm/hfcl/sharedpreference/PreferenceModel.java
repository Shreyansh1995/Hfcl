package com.netcomm.hfcl.sharedpreference;

/**
 * Created by Netcomm on 1/12/2017.
 */

public class PreferenceModel {

    private String AttendanceInput;
    private String userId;
    private String userName;
    private String email;
    private String empCode;
    private String HODId;
    private String HODName;
    private String IsRM;
    private String PlantID;
    private String RMId;
    private String RMName;
    private String UserImage;
    private String Location;

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    private String MobileNo;

    public static final String TokenValues = "abcHkl7900@8Uyhkj";
    public static final String TokenKey = "TokenNo";
    public String getAttendanceInput() {
        return AttendanceInput;
    }

    public void setAttendanceInput(String attendanceInput) {
        AttendanceInput = attendanceInput;
    }


    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getHODId() {
        return HODId;
    }

    public void setHODId(String HODId) {
        this.HODId = HODId;
    }

    public String getHODName() {
        return HODName;
    }

    public void setHODName(String HODName) {
        this.HODName = HODName;
    }

    public String getIsRM() {
        return IsRM;
    }

    public void setIsRM(String isRM) {
        IsRM = isRM;
    }

    public String getPlantID() {
        return PlantID;
    }

    public void setPlantID(String plantID) {
        PlantID = plantID;
    }

    public String getRMId() {
        return RMId;
    }

    public void setRMId(String RMId) {
        this.RMId = RMId;
    }

    public String getRMName() {
        return RMName;
    }

    public void setRMName(String RMName) {
        this.RMName = RMName;
    }

    public String getUserImage() {
        return UserImage;
    }

    public void setUserImage(String userImage) {
        UserImage = userImage;
    }
}
