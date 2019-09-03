package com.netcomm.hfcl.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.netcomm.hfcl.R;
import com.netcomm.hfcl.constants.UrlConstants;
import com.netcomm.hfcl.interfaces.ResponseListener;
import com.netcomm.hfcl.model.RequestLeaveArchiveListModel;
import com.netcomm.hfcl.model.RequestLeaveViewModel;
import com.netcomm.hfcl.services.ProjectWebRequest;
import com.netcomm.hfcl.sharedpreference.MySharedPreference;
import com.netcomm.hfcl.sharedpreference.PreferenceModel;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Netcomm on 8/29/2017.
 */

public class RequestLeaveArchiveAdapter extends BaseAdapter implements ResponseListener {
    private Context mContext;
    private ArrayList<RequestLeaveArchiveListModel> list;
    private int selectedPosition;
    private ProjectWebRequest request;

    public RequestLeaveArchiveAdapter(Context mContext, ArrayList<RequestLeaveArchiveListModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public RequestLeaveArchiveListModel getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        RequestLeaveArchiveAdapter.ViewHolder holder = null;

        if (convertView == null) {
            holder = new RequestLeaveArchiveAdapter.ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.request_leave_archive_list_item, null);
            holder.tv_ar_emp_name = (TextView) convertView.findViewById(R.id.tv_ar_emp_name);
            holder.tv_ar_req_no = (TextView) convertView.findViewById(R.id.tv_ar_req_no);
            holder.tv_ar_req_date = (TextView) convertView.findViewById(R.id.tv_ar_req_date);
            holder.tv_ar_period = (TextView) convertView.findViewById(R.id.tv_ar_period);
            convertView.setTag(holder);
        } else {
            holder = (RequestLeaveArchiveAdapter.ViewHolder) convertView.getTag();
        }
        holder.tv_ar_emp_name.setText(list.get(position).getEmpName());
        holder.tv_ar_req_no.setText(list.get(position).getReqNo());
        holder.tv_ar_req_date.setText(list.get(position).getReqDate());
        holder.tv_ar_period.setText(list.get(position).getPeriod());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitApiForViewDetail(list.get(position).getReqID(), list.get(position).getType());
                selectedPosition = position;
            }
        });
        return convertView;
    }


    class ViewHolder {

        TextView tv_ar_emp_name;
        TextView tv_ar_req_no;
        TextView tv_ar_req_date;
        TextView tv_ar_period;


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
                RequestLeaveViewModel data = new RequestLeaveViewModel(obj.optString("ApprovalButtonIsVisable"),
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
            }  else {
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.popup_view_request_leave_detail, null);
        TextView tv_header = (TextView) dialogView.findViewById(R.id.tv_header);
        ImageView close_win = (ImageView) dialogView.findViewById(R.id.close_win);

        TextView tv_name = (TextView) dialogView.findViewById(R.id.tv_name);
        TextView tv_req_no = (TextView) dialogView.findViewById(R.id.tv_req_no);
        TextView tv_dept = (TextView) dialogView.findViewById(R.id.tv_dept);
        TextView tv_location = (TextView) dialogView.findViewById(R.id.tv_location);
        TextView tv_submit_date = (TextView) dialogView.findViewById(R.id.tv_submit_date);
        TextView tv_fill_date = (TextView) dialogView.findViewById(R.id.tv_fill_date);
        TextView tv_leave_type = (TextView) dialogView.findViewById(R.id.tv_leave_type);
        TextView tv_dates = (TextView) dialogView.findViewById(R.id.tv_dates);
        /*TextView tv_from_time = (TextView) dialogView.findViewById(R.id.tv_from_time);
        TextView tv_to_time = (TextView) dialogView.findViewById(R.id.tv_to_time);*/
        TextView tv_purpose = (TextView) dialogView.findViewById(R.id.tv_purpose);
        TextView tv_cantact_no = (TextView) dialogView.findViewById(R.id.tv_cantact_no);


        LinearLayout ll_approval_header = (LinearLayout) dialogView.findViewById(R.id.approval_header);
        LinearLayout ll_approval_panel = (LinearLayout) dialogView.findViewById(R.id.ll_approval_panel);
        TextView tv_cancel_request = (TextView) dialogView.findViewById(R.id.tv_cancel_request);
        TextView tv_address = (TextView) dialogView.findViewById(R.id.tv_address);
        tv_cancel_request.setVisibility(View.GONE);
        tv_header.setText("Request No. " + data.getReqNo());
        tv_name.setText(data.getEmpName());
        tv_req_no.setText(data.getReqNo());
        tv_dept.setText(data.getDepartment());
        tv_location.setText(data.getLocation());
        tv_submit_date.setText(data.getReqDate());
        tv_dates.setText(data.getFromDate()+" - "+data.getToDate());
        /*tv_from_time.setText(data.getFromtime());
        tv_to_time.setText(data.getTotime());*/
        tv_purpose.setText(data.getLeavereason());
        tv_cantact_no.setText(data.getContactNo());
        tv_address.setText(data.getAddress());
        tv_fill_date.setText(data.getNoOfDay());
        tv_leave_type.setText(data.getLeaveType());

        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        /****************************************************/

        if (data.getrMPanelIsVisable().equals("false")){
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
        }

        /****************************************************/
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
}