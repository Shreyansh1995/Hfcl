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
import com.netcomm.hfcl.adapter.RequestLeaveArchiveAdapter;
import com.netcomm.hfcl.constants.UrlConstants;
import com.netcomm.hfcl.interfaces.ResponseListener;
import com.netcomm.hfcl.model.RequestLeaveArchiveListModel;
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
public class FragmentLeaveRequestArchiveList extends Fragment implements ResponseListener, View.OnClickListener {
    private View viewMain;
    private MainActivity mainActivity;
    private ProjectWebRequest request;
    private MySharedPreference pref;
    private LinearLayout ll_archive_leave_list_from_date;
    private LinearLayout ll_archive_leave_list_to_date;
    private LinearLayout ll_archive_leave_list_search;
    private TextView error_text;
    private TextView no_data_found_archive_leave_list;
    private TextView tv_archive_leave_list_from_date;
    private TextView tv_archive_leave_list_to_date;
    private ListView lv_archive_leave_list;
    private DateManagerUtility utility;
    private ArrayList<RequestLeaveArchiveListModel> archiveListShortLeave;

    public FragmentLeaveRequestArchiveList() {
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
        viewMain = inflater.inflate(R.layout.fragment_fragment_leave_request_archive_list, container, false);

        initView();
        return viewMain;
    }

    void initView() {
        pref = MySharedPreference.getInstance(getActivity());
        utility = new DateManagerUtility();

        ll_archive_leave_list_from_date = (LinearLayout) viewMain.findViewById(R.id.ll_archive_leave_list_from_date);
        ll_archive_leave_list_to_date = (LinearLayout) viewMain.findViewById(R.id.ll_archive_leave_list_to_date);
        ll_archive_leave_list_search = (LinearLayout) viewMain.findViewById(R.id.ll_archive_leave_list_search);

        error_text = (TextView) viewMain.findViewById(R.id.error_text);
        no_data_found_archive_leave_list = (TextView) viewMain.findViewById(R.id.no_data_found_archive_leave_list);
        tv_archive_leave_list_from_date = (TextView) viewMain.findViewById(R.id.tv_archive_leave_list_from_date);
        tv_archive_leave_list_to_date = (TextView) viewMain.findViewById(R.id.tv_archive_leave_list_to_date);
        lv_archive_leave_list = (ListView) viewMain.findViewById(R.id.lv_archive_leave_list);
        ll_archive_leave_list_from_date.setOnClickListener(this);
        ll_archive_leave_list_to_date.setOnClickListener(this);
        ll_archive_leave_list_search.setOnClickListener(this);
        utility.setCurrentDate1(mainActivity, tv_archive_leave_list_from_date, tv_archive_leave_list_to_date);
    }

    void hitApiForRequestArchiveList() {
        try {
            request = new ProjectWebRequest(getActivity(), getParamForRequestArchiveList(), UrlConstants.GET_REQUEST_LEAVE_ARCHIVE, this, UrlConstants.GET_REQUEST_LEAVE_ARCHIVE_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    JSONObject getParamForRequestArchiveList() {
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(PreferenceModel.TokenKey, PreferenceModel.TokenValues);
            object.put("UserId", pref.getsharedPreferenceData().getUserId());
            object.put("FromDate", MyCalenderUtil.getDateInServerSendFormat(tv_archive_leave_list_from_date.getText().toString()));
            object.put("ToDate", MyCalenderUtil.getDateInServerSendFormat(tv_archive_leave_list_to_date.getText().toString()));
        } catch (Exception e) {
        }
        return object;
    }


    @Override
    public void onSuccess(JSONObject obj, int Tag) {
        clearRef();
        if (obj.optString("Status").equals("true")) {
            lv_archive_leave_list.setVisibility(View.VISIBLE);
            no_data_found_archive_leave_list.setVisibility(View.GONE);
            if (Tag == UrlConstants.GET_REQUEST_LEAVE_ARCHIVE_TAG) {
                archiveListShortLeave = new ArrayList<RequestLeaveArchiveListModel>();
                RequestLeaveArchiveListModel model;
                try {
                    JSONArray arr = obj.getJSONArray("objLeaveDataRes");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject object = arr.getJSONObject(i);
                        model = new RequestLeaveArchiveListModel(object.optString("EmpName"),
                                object.optString("NoOfLeave"),
                                object.optString("Period"),
                                object.optString("RMStatus"),
                                object.optString("RegularisationType"),
                                object.optString("ReqDate"),
                                object.optString("ReqID"),
                                object.optString("ReqNo"),
                                object.optString("Type"));
                        archiveListShortLeave.add(model);
                    }
                    lv_archive_leave_list.setAdapter(new RequestLeaveArchiveAdapter(mainActivity, archiveListShortLeave));
                } catch (Exception e) {
                }

            }
        } else {
            lv_archive_leave_list.setVisibility(View.GONE);
            no_data_found_archive_leave_list.setVisibility(View.VISIBLE);
            no_data_found_archive_leave_list.setText(obj.optString("Message"));
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_archive_leave_list_from_date:
                utility.FromDatePicker(tv_archive_leave_list_from_date);
                break;
            case R.id.ll_archive_leave_list_to_date:
                utility.ToDatePicker(tv_archive_leave_list_to_date);
                break;
            case R.id.ll_archive_leave_list_search:
                validateNow();
                break;
        }
    }

    void validateNow() {
        if (MyCalenderUtil.validDate1(mainActivity, tv_archive_leave_list_from_date.getText().toString(), tv_archive_leave_list_to_date.getText().toString(), error_text)) {
            error_text.setVisibility(View.GONE);
            hitApiForRequestArchiveList();
        }
    }

}
