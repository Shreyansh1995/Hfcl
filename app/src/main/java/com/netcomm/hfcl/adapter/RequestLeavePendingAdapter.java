package com.netcomm.hfcl.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.netcomm.hfcl.R;
import com.netcomm.hfcl.constants.UrlConstants;
import com.netcomm.hfcl.interfaces.ResponseListener;
import com.netcomm.hfcl.model.RequestLeavePendingModel;
import com.netcomm.hfcl.model.RequestLeaveViewModel;
import com.netcomm.hfcl.services.ProjectWebRequest;
import com.netcomm.hfcl.sharedpreference.MySharedPreference;
import com.netcomm.hfcl.sharedpreference.PreferenceModel;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Netcomm on 8/29/2017.
 */

public class RequestLeavePendingAdapter extends BaseAdapter implements ResponseListener {
    private Context mContext;
    private ArrayList<RequestLeavePendingModel> list;
    private ProjectWebRequest request;
    private int selectedPosition;
    private EditText remarks;
    //private String selectApprovalLevel;

    public RequestLeavePendingAdapter(Context mContext, ArrayList<RequestLeavePendingModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public RequestLeavePendingModel getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        RequestLeavePendingAdapter.ViewHolder holder = null;
        if (convertView == null) {
            holder = new RequestLeavePendingAdapter.ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.request_leave_pending_list_item, null);
            holder.tv_emp_name_and_type = (TextView) convertView.findViewById(R.id.tv_emp_name_and_type);
            holder.tv_emp_req_code_and_date = (TextView) convertView.findViewById(R.id.tv_emp_req_code_and_date);
            holder.tv_emp_req_period = (TextView) convertView.findViewById(R.id.tv_emp_req_period);
            //holder.tv_emp_req_resion = (TextView) convertView.findViewById(R.id.tv_emp_req_resion);
            convertView.setTag(holder);
        } else {
            holder = (RequestLeavePendingAdapter.ViewHolder) convertView.getTag();
        }
        final RequestLeavePendingModel data = getItem(position);
        holder.tv_emp_name_and_type.setText(data.getEmpName());
        holder.tv_emp_req_code_and_date.setText(data.getReqNo() + " / " + data.getReqDate());
        holder.tv_emp_req_period.setText("Leave Date " + data.getPeriod());
        //holder.tv_emp_req_resion.setText(data.getResion());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //selectApprovalLevel = data.getApprovalLevel();
                hitApiForViewDetail(data.getReqID(), data.getType());
                selectedPosition = position;
            }
        });
        return convertView;
    }


    class ViewHolder {
        TextView tv_emp_name_and_type;
        TextView tv_emp_req_code_and_date;
        TextView tv_emp_req_period;
        //TextView tv_emp_req_resion;
    }

    void hitApiForViewDetail(String reqId, String type) {
        try {
            request = new ProjectWebRequest(mContext, getParam(reqId, type), UrlConstants.VIEW_MY_REQUESTED_LEAVE, this, UrlConstants.VIEW_MY_REQUESTED_LEAVE_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(JSONObject obj, int Tag) {
        clearRef();
        if (obj.optString("Status").equals("true")) {
            if (Tag == UrlConstants.VIEW_MY_REQUESTED_LEAVE_TAG) {
                RequestLeaveViewModel data =  new RequestLeaveViewModel(obj.optString("ApprovalButtonIsVisable"),
                        obj.optString("ApprovalPanelIsVisable"),
                        obj.optString("Attachment1"),
                        obj.optString("Attachment2"),
                        obj.optString("CancelButtonIsVisable"),
                        obj.optString("CancelDate"),
                        obj.optString("CancelName"),
                        obj.optString("CancelPanelIsVisable"),
                        obj.optString("CancelStatus"),
                        obj.optString("CanceltabName"),
                        obj.optString("CanceltabStatus"),
                        obj.optString("CancleTab"),
                        obj.optString("DeliveryDate"),
                        obj.optString("Department"),
                        obj.optString("Designation"),
                        obj.optString("LeaveType"),
                        obj.optString("Level"),
                        obj.optString("Location"),
                        obj.optString("Message"),
                        obj.optString("RMDate"),
                        obj.optString("RMPanelIsVisable"),
                        obj.optString("RMRemarks"),
                        obj.optString("RMSatus"),
                        obj.optString("RmName"),
                        obj.optString("Saturday"),
                        obj.optString("Status"),
                        obj.optString("address"),
                        obj.optString("contact_no"),
                        obj.optString("empId"),
                        obj.optString("empName"),
                        obj.optString("from_date"),
                        obj.optString("leavereason"),
                        obj.optString("noOfDay"),
                        obj.optString("reqDate"),
                        obj.optString("reqId"),
                        obj.optString("reqNo"),
                        obj.optString("to_date"));


                /**************************************/
                popUpForvehicleList(data);
            } else if (Tag == UrlConstants.APPROVE_DISAPPROVE_REQUEST_LEAVE_TAG) {
                list.remove(selectedPosition);
                notifyDataSetChanged();
                Toast.makeText(mContext, obj.optString("Message"), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, obj.optString("Message"), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFailure(VolleyError error, int Tag) {
        clearRef();
    }

    void clearRef() {
        if (request != null) {
            request = null;
        }
    }

    JSONObject getParam(String reqId, String type) {
        MySharedPreference pref = MySharedPreference.getInstance(mContext);
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(PreferenceModel.TokenKey, PreferenceModel.TokenValues);
            object.put("ReqId", reqId);
            object.put("Type", type);
        } catch (Exception e) {
        }
        return object;
    }

    void popUpForvehicleList(final RequestLeaveViewModel data) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.pop_up_view_request_leave_pending, null);
        TextView tv_submit_ar_approve = (TextView) dialogView.findViewById(R.id.tv_submit_ar_approve);
        TextView tv_submit_ar_dis_approve = (TextView) dialogView.findViewById(R.id.tv_submit_ar_dis_approve);
        TextView tv_header = (TextView) dialogView.findViewById(R.id.tv_header);
        ImageView close_win = (ImageView) dialogView.findViewById(R.id.close_win);
        TextView tv_name = (TextView) dialogView.findViewById(R.id.tv_name);
        TextView tv_req_no = (TextView) dialogView.findViewById(R.id.tv_req_no);
        TextView tv_dept = (TextView) dialogView.findViewById(R.id.tv_dept);
        TextView tv_location = (TextView) dialogView.findViewById(R.id.tv_location);
        TextView tv_submit_date = (TextView) dialogView.findViewById(R.id.tv_submit_date);
        TextView tv_fill_date = (TextView) dialogView.findViewById(R.id.tv_fill_date);
        TextView tv_leave_type = (TextView) dialogView.findViewById(R.id.tv_leave_type);
        /*TextView tv_from_time = (TextView) dialogView.findViewById(R.id.tv_from_time);
        TextView tv_to_time = (TextView) dialogView.findViewById(R.id.tv_to_time);*/
        TextView tv_dates = (TextView) dialogView.findViewById(R.id.tv_dates);
        TextView tv_purpose = (TextView) dialogView.findViewById(R.id.tv_purpose);
        TextView tv_cantact_no = (TextView) dialogView.findViewById(R.id.tv_cantact_no);
        remarks = (EditText) dialogView.findViewById(R.id.remarks);
        LinearLayout ll_approval_header = (LinearLayout) dialogView.findViewById(R.id.approval_header);
        LinearLayout ll_approval_panel = (LinearLayout) dialogView.findViewById(R.id.ll_approval_panel);
        TextView tv_address = (TextView) dialogView.findViewById(R.id.tv_address);
        tv_header.setText("Request No. " + data.getReqNo());
        tv_name.setText(data.getEmpName());
        tv_req_no.setText(data.getReqNo());
        tv_dept.setText(data.getDepartment());
        tv_location.setText(data.getLocation());
        tv_submit_date.setText(data.getReqDate());
        tv_fill_date.setText(data.getNoOfDay());
        tv_address.setText(data.getAddress());
        tv_leave_type.setText(data.getLeaveType());
        tv_dates.setText(data.getFromDate()+ " - "+data.getToDate());
        /*tv_from_time.setText(data.getFromtime());
        tv_to_time.setText(data.getTotime());*/
        tv_purpose.setText(data.getLeavereason());
        tv_cantact_no.setText(data.getContactNo());
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        tv_submit_ar_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitApiForApproveAndDisApprove(data.getReqId(), "1",data.getEmpId(),data.getLeaveType(),data.getFromDate(),data.getToDate(),data.getNoOfDay(),data.getLeavereason(),data.getAddress(),data.getContactNo(),data.getReqNo());
                alertDialog.dismiss();
            }
        });
        tv_submit_ar_dis_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (remarks.getText().toString().trim().length() > 0) {
                    hitApiForApproveAndDisApprove(data.getReqId(), "0",data.getEmpId(),data.getLeaveType(),data.getFromDate(),data.getToDate(),data.getNoOfDay(),data.getLeavereason(),data.getAddress(),data.getContactNo(),data.getReqNo());
                    alertDialog.dismiss();
                } else {
                    remarks.requestFocus();
                    remarks.setHintTextColor(Color.parseColor("#FF0000"));
                    Toast.makeText(mContext, "Please Enter Remark ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialog.getWindow().getAttributes().windowAnimations = R.style.CustomThemeBottomAndUpAnimation;
        alertDialog.show();
        close_win.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.GONE);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
    }

    void hitApiForApproveAndDisApprove(String reqId, String actionType, String empId, String leaveType, String fromDate, String toDate, String noOfDay, String leavereason, String address, String contactNo, String reqNo) {
        try {
            request = new ProjectWebRequest(mContext, getParamForApproveAndDisappove(reqId, actionType,empId,leaveType,fromDate,toDate,noOfDay,leavereason,address,contactNo,reqNo), UrlConstants.APPROVE_DISAPPROVE_REQUEST_LEAVE, this, UrlConstants.APPROVE_DISAPPROVE_REQUEST_LEAVE_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    JSONObject getParamForApproveAndDisappove(String reqId, String actionType, String empId, String leaveType, String fromDate, String toDate, String noOfDay, String leavereason, String address, String contactNo,String reqNo) {
        MySharedPreference pref = MySharedPreference.getInstance(mContext);
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(PreferenceModel.TokenKey, PreferenceModel.TokenValues);
            object.put("ID", reqId);
            //object.put("UserId", pref.getsharedPreferenceData().getUserId());
            //object.put("ApprovalLevel", selectApprovalLevel);
            object.put("ApprovelId", actionType);
            object.put("Remark", remarks.getText().toString());
            //object.put("LeaveType", leaveTypeId);
            //object.put("noOfLeave", noOfDays);
            object.put("Mode", "RM");
            object.put("RMID", pref.getsharedPreferenceData().getRMId());
            object.put("UserID", empId);
            object.put("LeaveTypeText", leaveType);
            object.put("FromDate", fromDate);
            object.put("ToDate", toDate);
            object.put("NoOfDays", noOfDay);
            object.put("Reason", leavereason);
            object.put("Address", address);
            object.put("ContactNo", contactNo);
            object.put("ReqNo", reqNo);
        } catch (Exception e) {
        }
        return object;
    }
}