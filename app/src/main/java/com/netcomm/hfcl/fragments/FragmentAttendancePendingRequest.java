package com.netcomm.hfcl.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.netcomm.hfcl.MainActivity;
import com.netcomm.hfcl.R;
import com.netcomm.hfcl.adapter.AttendancePendingRequestAdapter;
import com.netcomm.hfcl.constants.UrlConstants;
import com.netcomm.hfcl.interfaces.ResponseListener;
import com.netcomm.hfcl.model.AttendancePendingDetailModel;
import com.netcomm.hfcl.services.ProjectWebRequest;
import com.netcomm.hfcl.sharedpreference.MySharedPreference;
import com.netcomm.hfcl.sharedpreference.PreferenceModel;
import com.netcomm.hfcl.utils.DateManagerUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAttendancePendingRequest extends Fragment  implements View.OnClickListener, ResponseListener {
    private View viewMain;
    private MainActivity mainActivity;
    private ListView lv_pending_request;
    private MySharedPreference pref;
    private ProjectWebRequest request;
    private LinearLayout ll_pending_from_date;
    private LinearLayout ll_pending_to_date;
    private TextView tv_pending_from_date;
    private TextView tv_pending_to_date;
    private LinearLayout ll_submit;
    private ArrayList<AttendancePendingDetailModel> pendingList;
    private TextView error_text;
    private TextView no_data_found;
    private DateManagerUtility utility;


    public FragmentAttendancePendingRequest() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity){
            mainActivity=(MainActivity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mainActivity = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewMain = inflater.inflate(R.layout.fragment_fragment_attendance_pending_request, container, false);
        initView();
        return viewMain;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_submit:
                hitApiForPendingAttendanceDetail();
                break;
            case R.id.ll_pending_from_date:
                utility.FromDatePicker(tv_pending_from_date);
                break;
            case R.id.ll_pending_to_date:
                utility.ToDatePicker(tv_pending_to_date);
                break;

        }
    }

    void initView() {
        pref = MySharedPreference.getInstance(getActivity());
        utility = new DateManagerUtility();
        lv_pending_request = (ListView) viewMain.findViewById(R.id.lv_pending_request);
        ll_pending_from_date = (LinearLayout) viewMain.findViewById(R.id.ll_pending_from_date);
        ll_pending_to_date = (LinearLayout) viewMain.findViewById(R.id.ll_pending_to_date);
        tv_pending_from_date = (TextView) viewMain.findViewById(R.id.tv_pending_from_date);
        tv_pending_to_date = (TextView) viewMain.findViewById(R.id.tv_pending_to_date);
        no_data_found = (TextView) viewMain.findViewById(R.id.no_data_found);
        error_text = (TextView) viewMain.findViewById(R.id.error_text);
        ll_submit = (LinearLayout) viewMain.findViewById(R.id.ll_submit);
        ll_pending_from_date.setOnClickListener(this);
        ll_pending_to_date.setOnClickListener(this);
        ll_submit.setOnClickListener(this);
        utility.setCurrentDate(mainActivity, tv_pending_from_date, tv_pending_to_date);
        //hitApiForPendingAttendanceDetail();
    }

    JSONObject getParamPendingAttendanceList() {
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(PreferenceModel.TokenKey, PreferenceModel.TokenValues);
            object.put("UserId", pref.getsharedPreferenceData().getUserId());
            object.put("PlantID", pref.getsharedPreferenceData().getPlantID());
        } catch (Exception e) {
        }
        return object;
    }

    void hitApiForPendingAttendanceDetail() {
       /* if (MyCalenderUtil.validDate(mainActivity, tv_pending_from_date.getText().toString(), tv_pending_to_date.getText().toString(), error_text)) {
            error_text.setVisibility(View.GONE);*/
        error_text.setVisibility(View.GONE);
            try {
                request = new ProjectWebRequest(getActivity(), getParamPendingAttendanceList(), UrlConstants.PENDING_ATTENDANCE_REQUEST, this, UrlConstants.PENDING_ATTENDANCE_REQUEST_TAG);
                request.execute();
            } catch (Exception e) {
                clearRef();
                e.printStackTrace();
            //}
        }
    }

    @Override
    public void onSuccess(JSONObject obj, int Tag) {
        clearRef();
        if (obj.optString("Status").equals("true")) {
            no_data_found.setVisibility(View.GONE);
            lv_pending_request.setVisibility(View.VISIBLE);
            if (Tag == UrlConstants.PENDING_ATTENDANCE_REQUEST_TAG) {
                pendingList = new ArrayList<AttendancePendingDetailModel>();
                AttendancePendingDetailModel model;
                try {
                    JSONArray arr = obj.getJSONArray("objARGetDataRes");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject object = arr.getJSONObject(i);
                        model = new AttendancePendingDetailModel(object.optString("EmpName"),
                                object.optString("Period"),
                                object.optString("RMStatus"),
                                object.optString("RegularisationType"),
                                object.optString("ReqDate"),
                                object.optString("ReqID"),
                                object.optString("ReqNo"),
                                object.optString("Type"));
                        pendingList.add(model);
                    }
                    lv_pending_request.setAdapter(new AttendancePendingRequestAdapter(mainActivity, pendingList));
                } catch (Exception e) {
                }
            }
        } else {
            no_data_found.setVisibility(View.VISIBLE);
            lv_pending_request.setVisibility(View.GONE);
            no_data_found.setText(obj.optString("Message"));
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

}
