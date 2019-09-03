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
import com.netcomm.hfcl.model.AttendancePendingDetailModel;
import com.netcomm.hfcl.model.AttendanceRequestViewModel;
import com.netcomm.hfcl.services.ProjectWebRequest;
import com.netcomm.hfcl.sharedpreference.MySharedPreference;
import com.netcomm.hfcl.sharedpreference.PreferenceModel;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Netcomm on 1/18/2017.
 */
public class AttendancePendingRequestAdapter extends BaseAdapter implements ResponseListener {
    private Context mContext;
    private ArrayList<AttendancePendingDetailModel> list;
    private ProjectWebRequest request;
    private int selectedPosition;
    //private String selectApprovalLevel;
    EditText remarks;

    public AttendancePendingRequestAdapter(Context mContext, ArrayList<AttendancePendingDetailModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public AttendancePendingDetailModel getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AttendancePendingRequestAdapter.ViewHolder holder = null;
        if (convertView == null) {
            holder = new AttendancePendingRequestAdapter.ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.attendance_panding_request_item, null);
            holder.tv_emp_name_and_type = (TextView) convertView.findViewById(R.id.tv_emp_name_and_type);
            holder.tv_emp_req_code_and_date = (TextView) convertView.findViewById(R.id.tv_emp_req_code_and_date);
            holder.tv_emp_req_period = (TextView) convertView.findViewById(R.id.tv_emp_req_period);
            //holder.tv_emp_req_resion = (TextView) convertView.findViewById(R.id.tv_emp_req_resion);
            convertView.setTag(holder);
        } else {
            holder = (AttendancePendingRequestAdapter.ViewHolder) convertView.getTag();
        }
        final AttendancePendingDetailModel data = getItem(position);
        holder.tv_emp_name_and_type.setText(data.getEmpName() );
        holder.tv_emp_req_code_and_date.setText("Req No.: "+data.getReqNo() + " | Req Date : " + data.getReqDate());
        if(data.getRegularisationType().equals("Mis-Punch")){
            String parts[]=data.getPeriod().split("-");
        String  fdate=parts[0];
        holder.tv_emp_req_period.setText("Reg Date: " + fdate);
        }else{
            holder.tv_emp_req_period.setText("Reg Date: " + data.getPeriod());
        }



        //holder.tv_emp_req_resion.setText(data.getReason());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitApiForViewDetail(data.getReqID());
                //selectApprovalLevel = data.getApprovalLevel();
                selectedPosition = position;
            }
        });
        return convertView;
    }


    class ViewHolder {
        TextView tv_emp_name_and_type;
        TextView tv_emp_req_code_and_date;
        TextView tv_emp_req_period;
        TextView tv_status;
    }

    void hitApiForViewDetail(String reqId) {
        try {
            request = new ProjectWebRequest(mContext, getParam(reqId), UrlConstants.VIEW_ATTENDANCE_REQUEST, this, UrlConstants.VIEW_ATTENDANCE_REQUEST_TAG);
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
            if (Tag == UrlConstants.VIEW_ATTENDANCE_REQUEST_TAG) {
                AttendanceRequestViewModel data = new AttendanceRequestViewModel(obj.optString("CancelDate"),
                        obj.optString("CancelPanelIsVisable"),
                        obj.optString("CancelStatus"),
                        obj.optString("Department"),
                        obj.optString("Designation"),
                        obj.optString("Location"),
                        obj.optString("Message"),
                        obj.optString("RMDate"),
                        obj.optString("RMPanelIsVisable"),
                        obj.optString("RMRemarks"),
                        obj.optString("RMSatus"),
                        obj.optString("RegularizationType"),
                        obj.optString("Status"),
                        obj.optString("contact_no"),
                        obj.optString("empId"),
                        obj.optString("empName"),
                        obj.optString("from_date"),
                        obj.optString("fromtime"),
                        obj.optString("reqDate"),
                        obj.optString("reqId"),
                        obj.optString("reqNo"),
                        obj.optString("resion"),
                        obj.optString("to_date"),
                        obj.optString("totime"),
                        obj.optString("type"));
                popUpForvehicleList(data);
            } else if (Tag == UrlConstants.APPROVE_DISAPPROVE_ATTENDANCE_REQUEST_TAG) {
                list.remove(selectedPosition);
                notifyDataSetChanged();
                Toast.makeText(mContext, obj.optString("message"), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, obj.optString("message"), Toast.LENGTH_SHORT).show();
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

    JSONObject getParam(String reqId) {
        MySharedPreference pref = MySharedPreference.getInstance(mContext);
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(PreferenceModel.TokenKey, PreferenceModel.TokenValues);
            object.put("ReqId", reqId);
        } catch (Exception e) {
        }
        return object;
    }

    void popUpForvehicleList(final AttendanceRequestViewModel data) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.popup_view_pending_request, null);
        TextView tv_submit_ar_approve = (TextView) dialogView.findViewById(R.id.tv_submit_ar_approve);
        TextView tv_submit_ar_dis_approve = (TextView) dialogView.findViewById(R.id.tv_submit_ar_dis_approve);
        TextView tv_header = (TextView) dialogView.findViewById(R.id.tv_header);
        ImageView close_win = (ImageView) dialogView.findViewById(R.id.close_win);
        TextView tv_name = (TextView) dialogView.findViewById(R.id.tv_name);
        TextView tv_req_no = (TextView) dialogView.findViewById(R.id.tv_req_no);
        /*TextView tv_dept = (TextView) dialogView.findViewById(R.id.tv_dept);
        TextView tv_location = (TextView) dialogView.findViewById(R.id.tv_location);*/
        TextView tv_submit_date = (TextView) dialogView.findViewById(R.id.tv_submit_date);
        TextView tv_from_date = (TextView) dialogView.findViewById(R.id.tv_from_date);
       // TextView tv_to_date = (TextView) dialogView.findViewById(R.id.tv_to_date);
        TextView tv_from_time = (TextView) dialogView.findViewById(R.id.tv_from_time);
        TextView tv_to_time = (TextView) dialogView.findViewById(R.id.tv_to_time);
        TextView tv_purpose = (TextView) dialogView.findViewById(R.id.tv_purpose);
        TextView tv_regularization_type = (TextView) dialogView.findViewById(R.id.tv_regularization_type);
        TextView tv_cantact_no = (TextView) dialogView.findViewById(R.id.tv_cantact_no);
        View view_mispunch_onduty = (View) dialogView.findViewById(R.id.view_mispunch_onduty);
        TextView tv_from_time_mispunch = (TextView) dialogView.findViewById(R.id.tv_from_time_mispunch);
        remarks = (EditText) dialogView.findViewById(R.id.remarks);
        LinearLayout ll_time_mispunch = (LinearLayout) dialogView.findViewById(R.id.ll_time_mispunch);
        LinearLayout ll_on_duty_time = (LinearLayout) dialogView.findViewById(R.id.ll_on_duty_time);

        /*LinearLayout ll_approval_header = (LinearLayout) dialogView.findViewById(R.id.approval_header);
        LinearLayout ll_approval_panel = (LinearLayout) dialogView.findViewById(R.id.ll_approval_panel);*/
        tv_header.setText("Request No. " + data.getReqNo());
        tv_name.setText(data.getEmpName());
        tv_req_no.setText(data.getReqNo());
        /*tv_dept.setText(data.getDepartment());
        tv_location.setText(data.getLocation());*/
        tv_submit_date.setText(data.getReqDate());

        if(data.getRegularizationType().equals("Mis-Punch")){
            tv_from_date.setText(data.getFromDate());
            tv_from_time_mispunch.setText(data.getFromtime());
            ll_time_mispunch.setVisibility(View.VISIBLE);
            ll_on_duty_time.setVisibility(View.GONE);
            view_mispunch_onduty.setVisibility(View.GONE);
            //tv_to_time.setText(data.getTotime());
        }else{
            tv_from_date.setText(data.getFromDate()+" - "+data.getToDate());
            tv_from_time.setText(data.getFromtime());
            tv_to_time.setText(data.getTotime());
            ll_time_mispunch.setVisibility(View.GONE);
            view_mispunch_onduty.setVisibility(View.GONE);
            ll_on_duty_time.setVisibility(View.VISIBLE);
        }
     //   tv_from_date.setText(data.getFromDate()+" - "+data.getToDate());
       // tv_to_date.setText(data.getToDate());

        tv_purpose.setText(data.getResion());
        tv_regularization_type.setText(data.getRegularizationType());
        tv_cantact_no.setText(data.getContactNo());
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();

        tv_submit_ar_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitApiForApproveAndDisApprove(data.getReqId(), "1",data.getReqNo(),data.getEmpId(),data.getFromDate(),data.getRegularizationType(),data.getFromtime(),data.getTotime(),data.getResion());
                alertDialog.dismiss();
            }
        });
        tv_submit_ar_dis_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (remarks.getText().toString().trim().length() > 0) {
                    hitApiForApproveAndDisApprove(data.getReqId(), "0",data.getReqNo(),data.getEmpId(),data.getFromDate(),data.getRegularizationType(),data.getFromtime(),data.getTotime(),data.getResion());
                    alertDialog.dismiss();
                } else {
                    remarks.requestFocus();
                    remarks.setHintTextColor(Color.parseColor("#FF0000"));
                    Toast.makeText(mContext, "Please Enter Remark ", Toast.LENGTH_SHORT).show();
                }

            }
        });

        /*if (data.getrMPanelIsVisable().equals("false")){
            ll_approval_header.setVisibility(View.VISIBLE);
            View v = ((Activity) mContext).getLayoutInflater().inflate(R.layout.approval_level_layout, null);
            TextView tv_approval_label = (TextView) v.findViewById(R.id.tv_approval_label);
            TextView tv_approval_remark_label = (TextView) v.findViewById(R.id.tv_approval_remark_label);
            TextView tv_approval_status = (TextView) v.findViewById(R.id.tv_approval_status);
            TextView tv_approval_remark = (TextView) v.findViewById(R.id.tv_approval_remark);
            TextView tv_rm_date = (TextView) v.findViewById(R.id.tv_rm_date);

            tv_approval_label.setText("Status");
            tv_approval_remark_label.setText("Remark");
            tv_approval_status.setText(data.getrMSatus());
            tv_approval_remark.setText(data.getrMRemarks());
            tv_rm_date.setText(data.getrMDate());
            ll_approval_panel.addView(v);
        }
        else {
            ll_approval_header.setVisibility(View.GONE);
        }*/

        /*if (data.getMApprovalLevel() != 0) {
            if (data.getMApprovalLevel() == 1) {
                if (data.getFstApprovalStatusType().equals("True")) {
                    ll_approval_header.setVisibility(View.VISIBLE);
                    View v = ((Activity) mContext).getLayoutInflater().inflate(R.layout.approval_level_layout, null);
                    TextView tv_approval_label = (TextView) v.findViewById(R.id.tv_approval_label);
                    TextView tv_approval_remark_label = (TextView) v.findViewById(R.id.tv_approval_remark_label);
                    TextView tv_approval_status = (TextView) v.findViewById(R.id.tv_approval_status);
                    TextView tv_approval_remark = (TextView) v.findViewById(R.id.tv_approval_remark);
                    TextView tv_rm_date = (TextView) v.findViewById(R.id.tv_rm_date);

                    tv_approval_label.setText(data.getFstApprovalNm() + " Status");
                    tv_approval_remark_label.setText(data.getFstApprovalNm() + " Remark");
                    tv_approval_status.setText(data.getFstApprovalStatus());
                    tv_approval_remark.setText(data.getFstApprovalRemarks());
                    tv_rm_date.setText(data.getFstApprovalDate());
                    ll_approval_panel.addView(v);
                }
            } else if (data.getMApprovalLevel() == 2) {
                if (data.getFstApprovalStatusType().equals("True")) {
                    View v = ((Activity) mContext).getLayoutInflater().inflate(R.layout.approval_level_layout, null);
                    TextView tv_approval_label = (TextView) v.findViewById(R.id.tv_approval_label);
                    TextView tv_approval_remark_label = (TextView) v.findViewById(R.id.tv_approval_remark_label);
                    TextView tv_approval_status = (TextView) v.findViewById(R.id.tv_approval_status);
                    TextView tv_approval_remark = (TextView) v.findViewById(R.id.tv_approval_remark);
                    TextView tv_rm_date = (TextView) v.findViewById(R.id.tv_rm_date);

                    tv_approval_label.setText(data.getFstApprovalNm() + " Status");
                    tv_approval_remark_label.setText(data.getFstApprovalNm() + " Remark");
                    tv_approval_status.setText(data.getFstApprovalStatus());
                    tv_approval_remark.setText(data.getFstApprovalRemarks());
                    tv_rm_date.setText(data.getFstApprovalDate());
                    ll_approval_panel.addView(v);
                }
                if (data.getScndApprovalStatusType().equals("True")) {
                    ll_approval_header.setVisibility(View.VISIBLE);
                    View v = ((Activity) mContext).getLayoutInflater().inflate(R.layout.approval_level_layout, null);
                    TextView tv_approval_label = (TextView) v.findViewById(R.id.tv_approval_label);
                    TextView tv_approval_remark_label = (TextView) v.findViewById(R.id.tv_approval_remark_label);
                    TextView tv_approval_status = (TextView) v.findViewById(R.id.tv_approval_status);
                    TextView tv_approval_remark = (TextView) v.findViewById(R.id.tv_approval_remark);
                    TextView tv_rm_date = (TextView) v.findViewById(R.id.tv_rm_date);

                    tv_approval_label.setText(data.getScdApprovalNm() + " Status");
                    tv_approval_remark_label.setText(data.getScdApprovalNm() + " Remark");
                    tv_approval_status.setText(data.getScdApprovalStatus());
                    tv_approval_remark.setText(data.getScdApprovalRemarks());
                    tv_rm_date.setText(data.getScdApprovalDate());
                    ll_approval_panel.addView(v);
                }
            }
        } else {
            ll_approval_header.setVisibility(View.GONE);
        }*/

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

    void hitApiForApproveAndDisApprove(String reqId, String actionType, String reqNo, String empId, String fromDate, String regularizationType, String fromtime, String totime, String resion) {
        try {
            request = new ProjectWebRequest(mContext, getParamForApproveAndDisappove(reqId, actionType,reqNo,empId,fromDate,regularizationType,fromtime,totime,resion), UrlConstants.APPROVE_DISAPPROVE_ATTENDANCE_REQUEST, this, UrlConstants.APPROVE_DISAPPROVE_ATTENDANCE_REQUEST_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    JSONObject getParamForApproveAndDisappove(String reqId, String actionType, String reqNo, String empId, String fromDate, String regularizationType, String fromtime, String totime, String resion) {
        MySharedPreference pref = MySharedPreference.getInstance(mContext);
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(PreferenceModel.TokenKey, PreferenceModel.TokenValues);
            object.put("ReqId", reqId);
            object.put("UserId", pref.getsharedPreferenceData().getUserId());
            object.put("Status", actionType);
            object.put("Remarks", remarks.getText().toString());
            object.put("ReqNo", reqNo);
            object.put("EmpID", empId);
            object.put("FromDate", fromDate);
            object.put("RegularizationTypeText", regularizationType);
            object.put("FromTime", fromtime);
            object.put("ToTime", totime);
            object.put("Purpose", resion);
        } catch (Exception e) {
        }
        return object;
    }
}
