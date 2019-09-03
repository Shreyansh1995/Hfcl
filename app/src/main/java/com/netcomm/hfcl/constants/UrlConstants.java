package com.netcomm.hfcl.constants;

/**
 * Created by Netcomm on 10/28/2016.
 */

public class UrlConstants {

    private static final String BASE_URL = "https://hfclhub.hfcl.com/OffAttApp/OfficenetServices.svc/";
//    private static final String BASE_URL = "http://192.168.0.18:8054/MobileAPI/OfficenetServices.svc/";
    public static final String LOGIN = BASE_URL + "OfficeNet_UsersLogin";
    public static final String PUNCHIN_OUT = BASE_URL + "OfficeNet_SavePunchInOutDetails";
    public static final String SUBMIT_ATTENDANCE_REGULARIZATION_TYPE = BASE_URL + "OD_SubmitRequest";
    public static final String VIEW_ATTENDANCE_REGULARIZATION = BASE_URL + "Attendance_UserList";
    public static final String VIEW_ATTENDANCE_REQUEST = BASE_URL + "Attendance_View";
    public static final String CANCEL_ATTENDANCE_ATTENDANCE_REQUEST = BASE_URL + "Attendance_Cancel";
    public static final String ARCHIVE_ATTENDANCE_REQUEST = BASE_URL + "Attendance_ArchiveList";
    public static final String PENDING_ATTENDANCE_REQUEST = BASE_URL + "Attendance_UserPendingList";
    public static final String APPROVE_DISAPPROVE_ATTENDANCE_REQUEST = BASE_URL + "Attendance_ApproveDisapprove";
    public static final String GET_PUNCH_OUT_STATUS = BASE_URL + "GetStatusOfPunchInOut";
    public static final String GET_USER_LOGIN_DETAILS = BASE_URL + "GetUsersLoginDetails";

    public static final String LEAVE_CALC_DAYS = BASE_URL + "GetLeaveCalDaysRes";
    public static final String GET_LEAVE_TYPE = BASE_URL + "GetleaveBalance";
    public static final String SUBMIT_LEAVE_REQUEST = BASE_URL + "Leave_SubmitRequest";
    public static final String GET_MY_REQUESTED_LEAVE = BASE_URL + "Leave_UserLeaveList";
    public static final String VIEW_MY_REQUESTED_LEAVE = BASE_URL + "leaveView";
    public static final String CANCEL_REQUEST_LEAVE = BASE_URL + "leaveCancel";
    public static final String GET_REQUEST_LEAVE_PENDING = BASE_URL + "Leave_PendingLeaveList";
    public static final String APPROVE_DISAPPROVE_REQUEST_LEAVE = BASE_URL + "UserLeaveapprove";
    public static final String GET_REQUEST_LEAVE_ARCHIVE = BASE_URL + "Leave_ArchiveLeaveList";

    public static final String GET_ATTENDANCE = BASE_URL + "Attendance_ShowRequest";


    public static final int LOGIN_TAG=1000;
    public static final int PUNCHIN_OUT_TAG=1001;
    public static final int SUBMIT_ATTENDANCE_REGULARIZATION_TYPE_TAG = 1014;
    public static final int VIEW_ATTENDANCE_REGULARIZATION_TAG = 1015;
    public static final int VIEW_ATTENDANCE_REQUEST_TAG = 1018;
    public static final int CANCEL_ATTENDANCE_ATTENDANCE_REQUEST_TAG = 1020;
    public static final int ARCHIVE_ATTENDANCE_REQUEST_TAG = 1016;
    public static final int PENDING_ATTENDANCE_REQUEST_TAG = 1017;
    public static final int APPROVE_DISAPPROVE_ATTENDANCE_REQUEST_TAG = 1021;

    public static final int LEAVE_CALC_DAYS_TAG = 1060;
    public static final int GET_LEAVE_TYPE_TAG = 1059;
    public static final int SUBMIT_LEAVE_REQUEST_TAG = 1061;
    public static final int GET_MY_REQUESTED_LEAVE_TAG = 1062;
    public static final int VIEW_MY_REQUESTED_LEAVE_TAG = 1063;
    public static final int CANCEL_REQUEST_LEAVE_TAG = 1066;
    public static final int GET_REQUEST_LEAVE_PENDING_TAG = 1065;
    public static final int APPROVE_DISAPPROVE_REQUEST_LEAVE_TAG = 1067;
    public static final int GET_REQUEST_LEAVE_ARCHIVE_TAG = 1064;


    public static final int GET_PUNCH_OUT_STATUS_TAG = 2000;
    public static final int GET_USER_LOGIN_DETAILS_TAG = 2001;
    public static final int GET_ATTENDANCE_TAG = 2002;

}
