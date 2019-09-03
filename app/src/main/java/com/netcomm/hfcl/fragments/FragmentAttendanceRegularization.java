package com.netcomm.hfcl.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.netcomm.hfcl.MainActivity;
import com.netcomm.hfcl.R;
import com.netcomm.hfcl.constants.UrlConstants;
import com.netcomm.hfcl.interfaces.ResponseListener;
import com.netcomm.hfcl.services.ProjectWebRequest;
import com.netcomm.hfcl.sharedpreference.MySharedPreference;
import com.netcomm.hfcl.sharedpreference.PreferenceModel;
import com.netcomm.hfcl.utils.AppAlertDialog;
import com.netcomm.hfcl.utils.MyCalenderUtil;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Netcomm on 1/17/2017.
 */

public class FragmentAttendanceRegularization extends Fragment implements View.OnClickListener, ResponseListener {
    private View viewMain;

    private MainActivity mainActivity;
    private Spinner spnr_regularization_type;
    private MySharedPreference pref;
    private ProjectWebRequest request;
    private TextView tv_to_date;
    private TextView tv_from_date;
    private TextView tv_to_time;
    private TextView tv_from_time;
    private EditText et_reason;
    private EditText et_contact;
    private EditText et_place;
    private TextView tv_submit_regularization_type;
    private TextView tv_manger;

    private TextView tv_from_date_txt;
    private TextView tv_from_time_txt;

    private LinearLayout ll_to_date;
    private LinearLayout ll_from_date;
    private LinearLayout ll_reason;
    private LinearLayout ll_contact;
    private LinearLayout ll_place;
    private LinearLayout ll_manager;


    TimePickerDialog.OnTimeSetListener timePickerListener;
    //private ArrayList<AttandanceRegularizationTypeModel> reqularizationTypeList;
    private int curYear;
    private int curMonth;
    private int curDay;

    private int fromYearSelected;
    private int fromMonthSelected;
    private int fromDaySelected;
    private String currentTime;

    private LinearLayout ll_to_date_time_layout;
    private LinearLayout ll_from_date_time_layout;
    private LinearLayout ll_to_date_portion;
    private ScrollView scroll_attande_regular;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mainActivity = null;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewMain = inflater.inflate(R.layout.fragment_attendance_regularization, null);
        initView();

        return viewMain;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_from_date:
                fromDatePicker();
                break;
            case R.id.ll_to_date:
                toDatePicker();
                break;
            case R.id.tv_from_time:
                setTime(tv_from_time, tv_from_time.getText().toString());
                break;
            case R.id.tv_to_time:
                setTime(tv_to_time, tv_to_time.getText().toString());
                break;
            case R.id.tv_submit_regularization_type:
                startValidation();
                break;


        }
    }


    void initView() {
        pref = MySharedPreference.getInstance(getActivity());
        getCurrentDateTime();



        spnr_regularization_type = (Spinner) viewMain.findViewById(R.id.spnr_regularization_type);
        tv_submit_regularization_type = (TextView) viewMain.findViewById(R.id.tv_submit_regularization_type);
        tv_manger = (TextView) viewMain.findViewById(R.id.tv_manger);
        tv_to_date = (TextView) viewMain.findViewById(R.id.tv_to_date);
        tv_from_date = (TextView) viewMain.findViewById(R.id.tv_from_date);
        et_reason = (EditText) viewMain.findViewById(R.id.et_reason);
        et_contact = (EditText) viewMain.findViewById(R.id.et_contact);
        et_place = (EditText) viewMain.findViewById(R.id.et_place);
        tv_to_time = (TextView) viewMain.findViewById(R.id.tv_to_time);
        tv_from_time = (TextView) viewMain.findViewById(R.id.tv_from_time);
        ll_to_date = (LinearLayout) viewMain.findViewById(R.id.ll_to_date);
        ll_from_date = (LinearLayout) viewMain.findViewById(R.id.ll_from_date);
        ll_reason = (LinearLayout) viewMain.findViewById(R.id.ll_reason);
        ll_contact = (LinearLayout) viewMain.findViewById(R.id.ll_contact);
        ll_place = (LinearLayout) viewMain.findViewById(R.id.ll_place);
        ll_manager = (LinearLayout) viewMain.findViewById(R.id.ll_manager);
        ll_to_date_time_layout = (LinearLayout) viewMain.findViewById(R.id.ll_to_date_time_layout);
        ll_from_date_time_layout = (LinearLayout) viewMain.findViewById(R.id.ll_from_date_time_layout);
        ll_to_date_portion = (LinearLayout) viewMain.findViewById(R.id.ll_to_date_portion);
        ll_to_date.setOnClickListener(this);
        ll_from_date.setOnClickListener(this);
        ll_to_date.setOnClickListener(this);
        tv_to_time.setOnClickListener(this);
        tv_from_time.setOnClickListener(this);
        tv_submit_regularization_type.setOnClickListener(this);
        tv_from_date.setText("" + curDay + "/" + (curMonth + 1) + "/" + curYear);
        tv_to_date.setText("" + curDay + "/" + (curMonth + 1) + "/" + curYear);
        tv_to_time.setText(currentTime);
        tv_from_time.setText(currentTime);

        tv_from_date_txt = (TextView) viewMain.findViewById(R.id.tv_from_date_txt);
        tv_from_time_txt = (TextView) viewMain.findViewById(R.id.tv_from_time_txt);

        et_contact.setText(pref.getsharedPreferenceData().getMobileNo());
        et_place.setText(pref.getsharedPreferenceData().getLocation());
        tv_manger.setText(pref.getsharedPreferenceData().getRMName());
        spnr_regularization_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    ll_reason.setVisibility(View.GONE);
                    ll_contact.setVisibility(View.GONE);
                    ll_to_date_time_layout.setVisibility(View.GONE);
                    ll_from_date_time_layout.setVisibility(View.GONE);
                    ll_place.setVisibility(View.GONE);
                    ll_manager.setVisibility(View.GONE);
                } else {
                    showLayoutCorrespondingToCondition(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        //hitApi();
    }


    /*void hitApi() {
        try {
            request = new ProjectWebRequest(getActivity(), getParam(), UrlConstants.GET_ATTENDANCE_REGULARIZATION_TYPE, this, UrlConstants.GET_ATTENDANCE_REGULARIZATION_TYPE_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }*/

    void hitApiForRgularizationType() {
        try {
            request = new ProjectWebRequest(getActivity(), getParamToSendRegulerizationData(), UrlConstants.SUBMIT_ATTENDANCE_REGULARIZATION_TYPE, this, UrlConstants.SUBMIT_ATTENDANCE_REGULARIZATION_TYPE_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    /*JSONObject getParam() {
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(PreferenceModel.TokenKey, PreferenceModel.TokenValues);
            object.put("ClientID", pref.getsharedPreferenceData().getCompCode());
        } catch (Exception e) {
        }
        return object;
    }*/


    JSONObject getParamToSendRegulerizationData() {
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(PreferenceModel.TokenKey, PreferenceModel.TokenValues);
            object.put("UserId", pref.getsharedPreferenceData().getUserId());
            //object.put("FromDate", MyCalenderUtil.getDateInServerSendFormat(tv_from_date.getText().toString()));
            object.put("FromDate", tv_from_date.getText().toString());
            String fromTime = tv_from_time.getText().toString();
            object.put("FromTimeHour", fromTime.split(":")[0]);
            object.put("FromTimeMin", fromTime.split(":")[1]);
            object.put("Reason", et_reason.getText().toString());
            object.put("ContactNo", et_contact.getText().toString());
            object.put("Place", et_place.getText().toString());
            object.put("hodId", pref.getsharedPreferenceData().getRMId());
            object.put("PlantID", pref.getsharedPreferenceData().getPlantID());
            String mType = spnr_regularization_type.getSelectedItem().toString();
            String mTypeId = null;
            if (mType.equals("On Duty")) {
                mTypeId = "1";
                //object.put("ToDate", MyCalenderUtil.getDateInServerSendFormat(tv_to_date.getText().toString()));
                object.put("ToDate", tv_to_date.getText().toString());
                String toTime = tv_to_time.getText().toString();
                object.put("ToTimeHour", toTime.split(":")[0]);
                object.put("ToTimeMin", toTime.split(":")[1]);
            } else if (mType.equals("Mis-Punch")) {
                mTypeId = "2";
                object.put("ToDate", "5/7/2018");
                object.put("ToTimeHour", "11");
                object.put("ToTimeMin", "30");
            }
            object.put("RegularizationTypeID", mTypeId);
            object.put("RegularizationTypeText", mType);
        } catch (Exception e) {
        }
        return object;
    }

    @Override
    public void onSuccess(JSONObject obj, int Tag) {
        clearRef();
        if (obj.optString("Status").equals("true")) {
            if (Tag == UrlConstants.SUBMIT_ATTENDANCE_REGULARIZATION_TYPE_TAG) {
                AttendanceRegularizationMain frag = (AttendanceRegularizationMain) getParentFragment();
                frag.changePageFromOtherFragment(1);
                AppAlertDialog.showDialogSelfFinish(mainActivity, "Attendance", obj.optString("message"));
             /*   et_contact.setText("");
                et_place.setText("");
                et_reason.setText("");*/
            }
        } else {
            Toast.makeText(getActivity(), obj.optString("message"), Toast.LENGTH_SHORT).show();
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

    void showLayoutCorrespondingToCondition(int position) {
        ll_reason.setVisibility(View.VISIBLE);
        ll_contact.setVisibility(View.VISIBLE);
        ll_manager.setVisibility(View.VISIBLE);

        if (spnr_regularization_type.getItemAtPosition(position).toString().equals("On Duty")) {
            ll_to_date_time_layout.setVisibility(View.VISIBLE);
            ll_from_date_time_layout.setVisibility(View.VISIBLE);
            ll_to_date_portion.setVisibility(View.VISIBLE);
            ll_place.setVisibility(View.VISIBLE);
            tv_from_date_txt.setText("From Date");
            tv_from_time_txt.setText("From Time");
        } else if (spnr_regularization_type.getItemAtPosition(position).toString().equals("Mis-Punch")) {
            ll_to_date_time_layout.setVisibility(View.GONE);
            ll_from_date_time_layout.setVisibility(View.VISIBLE);
            ll_to_date_portion.setVisibility(View.INVISIBLE);
            ll_place.setVisibility(View.GONE);
            tv_from_date_txt.setText("Date");
            tv_from_time_txt.setText("Time");
        }

        /*AttandanceRegularizationTypeModel model = reqularizationTypeList.get(position);
        if (model.getId().equals("1")) //Late In
        {
            showFromDateAndToTimeNotToDate();

        } else if (model.getId().equals("2"))//Early out
        {
            showFromDateAndToTimeNotToDate();

        } else if (model.getId().equals("3"))//Forgot punch
        {
            showFromDateAndToTimeNotToDate();
        } else if (model.getId().equals("4")) //Outdoor Duty
        {
            ll_to_date_time_layout.setVisibility(View.VISIBLE);
            ll_from_date_time_layout.setVisibility(View.VISIBLE);
            ll_to_date_portion.setVisibility(View.VISIBLE);

        } else if (model.getId().equals("5")) //On Tour
        {
            ll_from_date_time_layout.setVisibility(View.VISIBLE);
            ll_to_date_time_layout.setVisibility(View.VISIBLE);
            ll_to_date_portion.setVisibility(View.VISIBLE);
        }*/
    }

    /*void showFromDateAndToTimeNotToDate() {
        ll_to_date_time_layout.setVisibility(View.VISIBLE);
        ll_from_date_time_layout.setVisibility(View.VISIBLE);
        ll_to_date_portion.setVisibility(View.INVISIBLE);
    }*/

    void getCurrentDateTime() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        fromYearSelected = curYear = c.get(Calendar.YEAR);
        fromMonthSelected = curMonth = c.get(Calendar.MONTH);
        fromDaySelected = curDay = c.get(Calendar.DAY_OF_MONTH);
        DateFormat df = new SimpleDateFormat("kk:mm");
        currentTime = df.format(Calendar.getInstance().getTime());
    }

    void fromDatePicker() {
        DatePickerDialog expdatePickerDialog = null;
        expdatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tv_from_date.setText("" + dayOfMonth + "/" + (month + 1) + "/" + year);
                fromYearSelected = year;
                fromDaySelected = dayOfMonth;
                fromMonthSelected = month;
                tv_to_date.setText("" + fromDaySelected + "/" + (fromMonthSelected + 1) + "/" + fromYearSelected);
            }
        }, fromYearSelected, fromMonthSelected, fromDaySelected);
        expdatePickerDialog.show();
    }

    void startValidation() {
        if (spnr_regularization_type.getSelectedItemPosition() == 0) {
            Toast.makeText(mainActivity, "Please select regularization type", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_reason.getText().toString().trim().length() <= 0) {
            Toast.makeText(mainActivity, "Please fill the remark field", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_contact.getText().toString().trim().length() <= 0) {
            Toast.makeText(mainActivity, "Please fill the contact number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_contact.getText().toString().trim().length() < 10) {
            Toast.makeText(mainActivity, "Please enter the valid number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (spnr_regularization_type.getSelectedItem().toString().equals("On Duty")) {
            if (et_place.getText().toString().trim().length() <= 0) {
                Toast.makeText(mainActivity, "Please fill the place field", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!MyCalenderUtil.validateFromDateAndToDate(tv_from_date.getText().toString(), tv_from_time.getText().toString(), tv_to_date.getText().toString(), tv_to_time.getText().toString())) {
                Toast.makeText(mainActivity, "To Time Should Be Greater Than From Time.", Toast.LENGTH_SHORT).show();
                return;
            }
           /* if (!MyCalenderUtil.validateFromTimeAndToTime(tv_from_time.getText().toString(), tv_to_time.getText().toString())) {
                Toast.makeText(mainActivity, "From time can't be greater then to time", Toast.LENGTH_SHORT).show();
                return;
            }*/
        }
        hitApiForRgularizationType();
    }

    void toDatePicker() {
        int month, day, year;
        if (fromYearSelected != 0 && fromDaySelected != 0) {
            month = fromMonthSelected;
            year = fromYearSelected;
            day = fromDaySelected;
        } else {
            month = curMonth;
            year = curYear;
            day = curDay;
        }
        DatePickerDialog expdatePickerDialog = null;
        expdatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tv_to_date.setText("" + dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, year, month, day);
        Calendar cal = createCalender("" + day, "" + (month - 1), "" + year);
        expdatePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
        expdatePickerDialog.show();
    }


    Calendar createCalender(String day, String month, String year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.MONTH, Integer.parseInt(month) + 1);
        calendar.set(Calendar.YEAR, Integer.parseInt(year));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
        return calendar;
    }

    void setTime(final TextView tv, String time) {
        int hour = 0;
        int minute = 0;
        if (!time.matches("")) {
            try {
                String timespilit[] = time.split(":");
                hour = Integer.parseInt(timespilit[0]);
                minute = Integer.parseInt(timespilit[1]);
            } catch (Exception e) {
                Calendar mcurrentTime = Calendar.getInstance();
                hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                minute = mcurrentTime.get(Calendar.MINUTE);
            }


        } else {
            Calendar mcurrentTime = Calendar.getInstance();
            hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            minute = mcurrentTime.get(Calendar.MINUTE);
        }

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(mainActivity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                // SimpleDateFormat df=new SimpleDateFormat("HH:mm",Locale.getDefault());

                String aTime = new StringBuilder().append(selectedHour).append(':').append(String.valueOf(selectedMinute)).toString();

                tv.setText(aTime);
            }
        }, hour, minute, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();


    }
}
