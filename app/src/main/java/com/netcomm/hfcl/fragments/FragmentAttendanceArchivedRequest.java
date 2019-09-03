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
import com.netcomm.hfcl.adapter.ArchivedAttendanceRequestAdapter;
import com.netcomm.hfcl.constants.UrlConstants;
import com.netcomm.hfcl.interfaces.ResponseListener;
import com.netcomm.hfcl.model.AttendanceArchiveDetail;
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
public class FragmentAttendanceArchivedRequest extends Fragment  implements View.OnClickListener, ResponseListener {
    private View viewMain;
    private MainActivity mainActivity;
    private ListView lv_archive_request;
    private MySharedPreference pref;
    private ProjectWebRequest request;
    private LinearLayout ll_submit;
    private LinearLayout ll_archive_from_date;
    private LinearLayout ll_archive_to_date;
    private TextView tv_archive_from_date;
    private TextView tv_archive_to_date;
    private TextView error_text;
    private ArrayList<AttendanceArchiveDetail> archiveList;
    private TextView no_data_found;
    private DateManagerUtility utility;

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

    public FragmentAttendanceArchivedRequest() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewMain = inflater.inflate(R.layout.fragment_fragment_attendance_archived_request, container, false);
        initView();
        return viewMain;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_submit:
                hitApiForRgularizationDetail();
                break;
            case R.id.ll_archive_from_date:
                utility.FromDatePicker(tv_archive_from_date);
                break;
            case R.id.ll_archive_to_date:
                utility.ToDatePicker(tv_archive_to_date);
                break;
        }
    }

    void initView() {
        pref = MySharedPreference.getInstance(getActivity());
        utility=new DateManagerUtility();
        lv_archive_request = (ListView) viewMain.findViewById(R.id.lv_archive_request);
        ll_submit = (LinearLayout) viewMain.findViewById(R.id.ll_submit);
        ll_archive_from_date = (LinearLayout) viewMain.findViewById(R.id.ll_archive_from_date);
        ll_archive_to_date = (LinearLayout) viewMain.findViewById(R.id.ll_archive_to_date);
        tv_archive_from_date = (TextView) viewMain.findViewById(R.id.tv_archive_from_date);
        tv_archive_to_date = (TextView) viewMain.findViewById(R.id.tv_archive_to_date);
        no_data_found = (TextView) viewMain.findViewById(R.id.no_data_found);
        error_text = (TextView) viewMain.findViewById(R.id.error_text);
        ll_submit.setOnClickListener(this);
        ll_archive_from_date.setOnClickListener(this);
        ll_archive_to_date.setOnClickListener(this);
        utility.setCurrentDate1(mainActivity,tv_archive_from_date,tv_archive_to_date);
        //hitApiForRgularizationDetail();
    }

    JSONObject getParamArchiveAttendanceList() {
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(PreferenceModel.TokenKey, PreferenceModel.TokenValues);
            object.put("UserId", pref.getsharedPreferenceData().getUserId());
            object.put("FromDate", MyCalenderUtil.getDateInServerSendFormat(tv_archive_from_date.getText().toString()));
            object.put("ToDate", MyCalenderUtil.getDateInServerSendFormat(tv_archive_to_date.getText().toString()));
            object.put("PlantID", pref.getsharedPreferenceData().getPlantID());
        } catch (Exception e) {
        }
        return object;
    }

    void hitApiForRgularizationDetail() {
        if (MyCalenderUtil.validDate1(mainActivity, tv_archive_from_date.getText().toString(), tv_archive_to_date.getText().toString(), error_text)) {
            error_text.setVisibility(View.GONE);
            try {
                request = new ProjectWebRequest(getActivity(), getParamArchiveAttendanceList(), UrlConstants.ARCHIVE_ATTENDANCE_REQUEST, this, UrlConstants.ARCHIVE_ATTENDANCE_REQUEST_TAG);
                request.execute();
            } catch (Exception e) {
                clearRef();
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onSuccess(JSONObject obj, int Tag) {
        clearRef();
        if (obj.optString("Status").equals("true")) {
            no_data_found.setVisibility(View.GONE);
            lv_archive_request.setVisibility(View.VISIBLE);
            if (Tag == UrlConstants.ARCHIVE_ATTENDANCE_REQUEST_TAG) {
                archiveList = new ArrayList<AttendanceArchiveDetail>();
                AttendanceArchiveDetail model;
                try {
                    JSONArray arr = obj.getJSONArray("objARGetDataRes");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject object = arr.getJSONObject(i);
                        model = new AttendanceArchiveDetail(object.optString("EmpName"),
                                object.optString("Period"),
                                object.optString("RMStatus"),
                                object.optString("RegularisationType"),
                                object.optString("ReqDate"),
                                object.optString("ReqID"),
                                object.optString("ReqNo"),
                                object.optString("Type"));
                        archiveList.add(model);
                    }
                    lv_archive_request.setAdapter(new ArchivedAttendanceRequestAdapter(mainActivity, archiveList));
                } catch (Exception e) {
                }
            }
        } else {
            no_data_found.setVisibility(View.VISIBLE);
            lv_archive_request.setVisibility(View.GONE);
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
