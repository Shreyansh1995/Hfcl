package com.netcomm.hfcl.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.netcomm.hfcl.MainActivity;
import com.netcomm.hfcl.R;
import com.netcomm.hfcl.constants.UrlConstants;
import com.netcomm.hfcl.interfaces.ResponseListener;
import com.netcomm.hfcl.services.ProjectWebRequest;
import com.netcomm.hfcl.sharedpreference.MySharedPreference;
import com.netcomm.hfcl.sharedpreference.PreferenceModel;
import com.netcomm.hfcl.utils.FragmentTransections;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import static com.netcomm.hfcl.constants.FragmentsTags.ATTENDANCE_CALENDER;
import static com.netcomm.hfcl.constants.FragmentsTags.ATTENDANCE_REGULARIZATION;
import static com.netcomm.hfcl.constants.FragmentsTags.LEAVE;
import static com.netcomm.hfcl.constants.FragmentsTags.MARK_ATTENDANCE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDashboard extends Fragment implements View.OnClickListener, ResponseListener {
    View v;

    private TextView txt_emp_name;
    private TextView txt_emp_id;
    private TextView txt_emp_logtime;
    private TextView txt_emp_duration;
    private LinearLayout puching_llayout;
    private LinearLayout calender_llayout;
    private LinearLayout regularization_llayout;
    private LinearLayout leave_llayout;
    private CircularImageView user_img;

    private MySharedPreference sharedPreference;
    private ProjectWebRequest request;

    MainActivity mainActivity;

    public FragmentDashboard() {
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
        v = inflater.inflate(R.layout.fragment_fragment_dashboard, container, false);

        initView();

        return v;
    }

    private void initView() {
        txt_emp_name = (TextView) v.findViewById(R.id.txt_emp_name);
        txt_emp_id = (TextView) v.findViewById(R.id.txt_emp_id);
        txt_emp_logtime = (TextView) v.findViewById(R.id.txt_emp_logtime);
        txt_emp_duration = (TextView) v.findViewById(R.id.txt_emp_duration);
        puching_llayout = (LinearLayout) v.findViewById(R.id.puching_llayout);
        calender_llayout = (LinearLayout) v.findViewById(R.id.calender_llayout);
        regularization_llayout = (LinearLayout) v.findViewById(R.id.regularization_llayout);
        leave_llayout = (LinearLayout) v.findViewById(R.id.leave_llayout);
        user_img = (CircularImageView) v.findViewById(R.id.user_img);

        puching_llayout.setOnClickListener(this);
        calender_llayout.setOnClickListener(this);
        regularization_llayout.setOnClickListener(this);
        leave_llayout.setOnClickListener(this);

        setData();

        hitApi();

    }

    void hitApi() {
        try {
            request = new ProjectWebRequest(getActivity(), getParam(), UrlConstants.GET_USER_LOGIN_DETAILS, this, UrlConstants.GET_USER_LOGIN_DETAILS_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    void clearRef() {
        if (request != null) {
            request = null;
        }
    }

    @Override
    public void onSuccess(JSONObject obj, int Tag) {
        clearRef();
        if (Tag == UrlConstants.GET_USER_LOGIN_DETAILS_TAG) {
            if (obj.optString("Status").equals("true")) {
                txt_emp_logtime.setText(obj.optString("LoginTime"));
                txt_emp_duration.setText(obj.optString("Duration"));

                txt_emp_logtime.setVisibility(View.VISIBLE);
                txt_emp_duration.setVisibility(View.VISIBLE);
            } else {
                txt_emp_logtime.setVisibility(View.GONE);
                txt_emp_duration.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onFailure(VolleyError error, int Tag) {
        clearRef();
    }

    JSONObject getParam() {
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(PreferenceModel.TokenKey, PreferenceModel.TokenValues);
            object.put("EmpCode", sharedPreference.getsharedPreferenceData().getEmpCode());
        } catch (Exception e) {
        }
        return object;
    }

    private void setData() {
        sharedPreference = MySharedPreference.getInstance(getActivity());
        txt_emp_name.setText(sharedPreference.getsharedPreferenceData().getUserName());
        txt_emp_id.setText("Emp Code : " + sharedPreference.getsharedPreferenceData().getEmpCode());
        if (!sharedPreference.getsharedPreferenceData().getUserImage().matches("")){
            Picasso.with(getActivity())
                    .load(sharedPreference.getsharedPreferenceData().getUserImage())
                    .error(R.drawable.user_dummy)
                    .placeholder(R.drawable.user_dummy)
                    .into(user_img);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.puching_llayout:

               // FragmentTransections.replaceFragmnet(getActivity(), new FragmentPunchinOutNew(), MARK_ATTENDANCE, R.id.main_farme, true);
                if (sharedPreference.getsharedPreferenceData().getAttendanceInput().equalsIgnoreCase("A"))

                    FragmentTransections.replaceFragmnet(getActivity(), new FragmentPunchinOutNew(), MARK_ATTENDANCE, R.id.main_farme, true);
                else
                    Toast.makeText(mainActivity, "You are not authorized to punch through Mobile application! ", Toast.LENGTH_SHORT).show();
                  //  Toast(getActivity().findViewById(android.R.id.content), "You are not authorized to mark your attendance from app", Snackbar.LENGTH_SHORT).show();*/
                break;
            case R.id.calender_llayout:
                //Toast.makeText(mainActivity, "Under Construction", Toast.LENGTH_SHORT).show();
                FragmentTransections.replaceFragmnet(getActivity(), new FragmentAttendance(), ATTENDANCE_CALENDER, R.id.main_farme, true);
                break;
            case R.id.regularization_llayout:
                FragmentTransections.replaceFragmnet(getActivity(), new AttendanceRegularizationMain(), ATTENDANCE_REGULARIZATION,R.id.main_farme,true );

            break;
            case R.id.leave_llayout:
                //Toast.makeText(mainActivity, "Under Construction", Toast.LENGTH_SHORT).show();
                FragmentTransections.replaceFragmnet(getActivity(), new FragmentLeaveMain(), LEAVE, R.id.main_farme, true);
                break;

        }
    }
}
