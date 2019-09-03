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
import com.netcomm.hfcl.adapter.AttendanceRegularizationListAdapter;
import com.netcomm.hfcl.constants.UrlConstants;
import com.netcomm.hfcl.interfaces.ResponseListener;
import com.netcomm.hfcl.model.AttendanceRegularizationListModel;
import com.netcomm.hfcl.services.ProjectWebRequest;
import com.netcomm.hfcl.sharedpreference.MySharedPreference;
import com.netcomm.hfcl.sharedpreference.PreferenceModel;
import com.netcomm.hfcl.utils.DateManagerUtility;
import com.netcomm.hfcl.utils.MyCalenderUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAttendanceRegularizationList extends Fragment implements View.OnClickListener, ResponseListener {
    private View viewMain;
    private ListView lv_request_detail;
    private MainActivity mainActivity;
    private MySharedPreference pref;
    private ProjectWebRequest request;
    private LinearLayout ll_from_date;
    private LinearLayout ll_to_date;
    private LinearLayout ll_submit;
    private DateManagerUtility utility;
    private TextView tv_from_date;
    private TextView tv_to_date;
    private TextView error_text;
    private ArrayList<AttendanceRegularizationListModel> reqularizationTypeList;
    private TextView no_data_found;

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

    public FragmentAttendanceRegularizationList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewMain = inflater.inflate(R.layout.fragment_fragment_attendance_regularization_list, container, false);

        initView();

        return viewMain;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_from_date:
                utility.FromDatePicker(tv_from_date);
                break;
            case R.id.ll_to_date:
                utility.ToDatePicker(tv_to_date);
                break;
            case R.id.ll_submit:
                getRegularizationDetail();
                break;
        }
    }

    void initView() {
        pref = MySharedPreference.getInstance(getActivity());
        utility=new DateManagerUtility();
        lv_request_detail = (ListView) viewMain.findViewById(R.id.lv_request_detail);
        ll_from_date = (LinearLayout) viewMain.findViewById(R.id.ll_from_date);
        ll_to_date = (LinearLayout) viewMain.findViewById(R.id.ll_to_date);
        ll_submit = (LinearLayout) viewMain.findViewById(R.id.ll_submit);
        error_text = (TextView) viewMain.findViewById(R.id.error_text);
        tv_from_date = (TextView) viewMain.findViewById(R.id.tv_from_date);
        tv_to_date = (TextView) viewMain.findViewById(R.id.tv_to_date);
        no_data_found = (TextView) viewMain.findViewById(R.id.no_data_found);
        ll_from_date.setOnClickListener(this);
        ll_to_date.setOnClickListener(this);
        ll_submit.setOnClickListener(this);
        utility.setCurrentDate(mainActivity,tv_from_date,tv_to_date);
        //getRegularizationDetail();
    }

    void hitApiForRgularizationDetail() {
        try {
            request = new ProjectWebRequest(getActivity(), getParam(), UrlConstants.VIEW_ATTENDANCE_REGULARIZATION, this, UrlConstants.VIEW_ATTENDANCE_REGULARIZATION_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    JSONObject getParam() {
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(PreferenceModel.TokenKey, PreferenceModel.TokenValues);
            object.put("UserId", pref.getsharedPreferenceData().getUserId());
            object.put("FromDate", MyCalenderUtil.getDateInServerSendFormat(tv_from_date.getText().toString()));
            object.put("ToDate", MyCalenderUtil.getDateInServerSendFormat(tv_to_date.getText().toString()));

        } catch (Exception e) {
        }
        return object;
    }


    @Override
    public void onSuccess(JSONObject obj, int Tag) {
        clearRef();
        if (obj.optString("Status").equals("true")) {
            no_data_found.setVisibility(View.GONE);
            lv_request_detail.setVisibility(View.VISIBLE);
            if (Tag == UrlConstants.VIEW_ATTENDANCE_REGULARIZATION_TAG) {
                reqularizationTypeList = new ArrayList<AttendanceRegularizationListModel>();
                AttendanceRegularizationListModel model;
                try {
                    JSONArray arr = obj.getJSONArray("objARGetDataRes");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject object = arr.getJSONObject(i);
                        model = new AttendanceRegularizationListModel(object.optString("EmpName"),object.optString("Period"),object.optString("RMStatus"),object.optString("RegularisationType"),object.optString("ReqDate"),object.optString("ReqID"),object.optString("ReqNo"),object.optString("Type"));
                        reqularizationTypeList.add(model);
                    }
                    lv_request_detail.setAdapter(new AttendanceRegularizationListAdapter(mainActivity, reqularizationTypeList));
                } catch (Exception e) {
                }
            }
        } else {
            no_data_found.setVisibility(View.VISIBLE);
            lv_request_detail.setVisibility(View.GONE);
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

    void getRegularizationDetail() {
        if (MyCalenderUtil.validDate(mainActivity, tv_from_date.getText().toString(), tv_to_date.getText().toString(), error_text)) {
            error_text.setVisibility(View.GONE);
            hitApiForRgularizationDetail();
        }
    }

}
