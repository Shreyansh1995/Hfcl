package com.netcomm.hfcl.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Netcomm on 10/18/2016.
 */

public class MySharedPreference {

    private static MySharedPreference object;
    private Context mContext;
    private static final String MyPREFERENCES = "OfficeNetPref";
    private SharedPreferences sharedpreferences;
    private final String userId = "UserId";
    private final String EmpCode = "empcode";
    private final String userName = "userName";
    private final String Email = "email";
    private final String HODId = "HODId";
    private final String HODName = "HODName";
    private final String IsRM = "IsRM";
    private final String PlantID = "PlantID";
    private final String RMId = "RMId";
    private final String RMName = "RMName";
    private final String UserImage = "UserImage";
    private final String AttendanceInput = "AttendanceInput";



    private final String MobileNo = "MobileNo";
    private final String UserLocation = "Location";



    public MySharedPreference(Context mContext) {
        this.mContext = mContext;
        sharedpreferences = mContext.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    public static MySharedPreference getInstance(Context mContext) {
        if (object == null) {
            object = new MySharedPreference(mContext);
        }
        return object;
    }





    public void setAttendanceInput(String attandanceInput) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(AttendanceInput, attandanceInput);
        editor.commit();
    }public void setUserId(String userid) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(userId, userid);
        editor.commit();
    }
    public void setEmpCode(String code) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(EmpCode, code);
        editor.commit();
    }

    public void setUserName(String name) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(userName, name);
        editor.commit();
    }
    public void setEmail(String email) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Email, email);
        editor.commit();
    }

    public void setHODId(String hodid) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(HODId, hodid);
        editor.commit();
    }

    public void setHODName(String hodname) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(HODName, hodname);
        editor.commit();
    }

    public void setIsRM(String isrm) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(IsRM, isrm);
        editor.commit();
    }

    public void setPlantID(String plantid) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PlantID, plantid);
        editor.commit();
    }

    public void setRMId(String rmid) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(RMId, rmid);
        editor.commit();
    }

    public void setRMName(String rmname) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(RMName, rmname);
        editor.commit();
    }

    public void setUserImage(String userimage) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(UserImage, userimage);
        editor.commit();
    }




    public void  setLocation(String location) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(UserLocation, location);
        editor.commit();

    }public void  setMobile(String mob) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(MobileNo, mob);
        editor.commit();

    }
    public void clearAll() {
        sharedpreferences.edit().clear().commit();
    }

    public PreferenceModel getsharedPreferenceData() {
        PreferenceModel data = new PreferenceModel();
        data.setUserId(sharedpreferences.getString(userId, null));
        data.setEmail(sharedpreferences.getString(Email, null));
        data.setUserName(sharedpreferences.getString(userName, null));
        data.setEmpCode(sharedpreferences.getString(EmpCode, null));
        data.setHODId(sharedpreferences.getString(HODId, null));
        data.setHODName(sharedpreferences.getString(HODName, null));
        data.setIsRM(sharedpreferences.getString(IsRM, null));
        data.setPlantID(sharedpreferences.getString(PlantID, null));
        data.setRMId(sharedpreferences.getString(RMId, null));
        data.setRMName(sharedpreferences.getString(RMName, null));
        data.setUserImage(sharedpreferences.getString(UserImage, null));
        data.setAttendanceInput(sharedpreferences.getString(AttendanceInput,null));
        data.setLocation(sharedpreferences.getString(UserLocation,null));
        data.setMobileNo(sharedpreferences.getString(MobileNo,null));
        return data;
    }

}
