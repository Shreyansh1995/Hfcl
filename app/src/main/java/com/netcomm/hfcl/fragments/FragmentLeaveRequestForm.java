package com.netcomm.hfcl.fragments;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.netcomm.hfcl.MainActivity;
import com.netcomm.hfcl.R;
import com.netcomm.hfcl.adapter.LeaveTypeListAdapter;
import com.netcomm.hfcl.constants.UrlConstants;
import com.netcomm.hfcl.interfaces.ResponseListener;
import com.netcomm.hfcl.model.FileUploadModel;
import com.netcomm.hfcl.model.LeaveBalanceModel;
import com.netcomm.hfcl.model.LeaveTypeModel;
import com.netcomm.hfcl.services.ProjectWebRequest;
import com.netcomm.hfcl.sharedpreference.MySharedPreference;
import com.netcomm.hfcl.sharedpreference.PreferenceModel;
import com.netcomm.hfcl.utils.AppAlertDialog;
import com.netcomm.hfcl.utils.FileUtils;
import com.netcomm.hfcl.utils.MyCheckPermission;
import com.netcomm.hfcl.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLeaveRequestForm extends Fragment implements View.OnClickListener, ResponseListener {
    private static final int PERMISSIONS_REQUEST_CAMERA = 20;
    public static final int GALLERY_REQUEST = 402;
    private File capturedImages = null;
    private View viewMain;
    String fromDateString = null;
    String toDateString = null;
    private MainActivity mainActivity;
    private Spinner spnr_leave_type;
    private MySharedPreference pref;
    private ProjectWebRequest request;
    private TextView tv_to_date;
    private TextView tv_from_date;
    private TextView tv_choose_file;
    private ImageView img_set_image;

    private String totalDayToTakeLeaveCount = "0";
    private ArrayList<LeaveBalanceModel> listLeaveBalanceModel;
    private EditText et_reason;
    private EditText et_leave_address;
    private EditText et_contact;
    private TextView tv_submit_regularization_type;
    private ArrayList<LeaveTypeModel> leaveTypeList;
    private int curYear;
    private int curMonth;
    private int curDay;

    private int fromYearSelected;
    private int fromMonthSelected;
    private int fromDaySelected;

    private RelativeLayout rl_show_leave_detail;
    private ImageView img_plus_minus;
    private LinearLayout ll_leave_detail;

    private RadioButton rd_first_half;
    private RadioButton rd_sec_half;

    private LinearLayout ll_first_half;
    private LinearLayout ll_sec_half;
    private LinearLayout ll_to_date;
    private LinearLayout ll_from_date;


    private LinearLayout ll_upload_doc;
    private static ImageView img_uplaod_doc;
    //private TextView tv_calc_days;
    private TextView tv_count_days;
    private TextView tv_rm_name;
    private TextView tv_selected_file_name;

    private RelativeLayout rl_choose_file;
    private FileUploadModel mFileUploadModel = null;

    private LinearLayout ll_leave_detail_row;

    private String companyId;
    double mDays;
    private String userChoosenTask;
    File destination;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String filename;
    private String encodedImageData;

    public FragmentLeaveRequestForm() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
    }


  /*  @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Refresh your fragment here
         //   hitApi();
        }
    }
*/

    @Override
    public void onDetach() {
        super.onDetach();

        mainActivity = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewMain = inflater.inflate(R.layout.fragment_fragment_leave_request_form, container, false);
        initView();

        return viewMain;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_from_date:
                tv_count_days.setVisibility(View.GONE);
                fromDatePicker();
                break;
            case R.id.ll_to_date:
                tv_count_days.setVisibility(View.GONE);
                toDatePicker();
                break;
            case R.id.tv_submit_regularization_type:
                if (startValidation()) {
                    try {
                        request = new ProjectWebRequest(getActivity(), getParamForCalDays(), UrlConstants.LEAVE_CALC_DAYS, new ResponseListener() {
                            @Override
                            public void onSuccess(JSONObject call, int Tag) {
                                if (call.optString("Status").equals("true")) {
                                    totalDayToTakeLeaveCount = call.optString("NoOfDays");
                                    tv_count_days.setVisibility(View.VISIBLE);
                                    tv_count_days.setText("" + totalDayToTakeLeaveCount + " Day");
                                    checkTypeOfLeaveRequest();
                                }
                            }

                            @Override
                            public void onFailure(VolleyError error, int Tag) {
                                totalDayToTakeLeaveCount = "0";
                            }
                        }, UrlConstants.LEAVE_CALC_DAYS_TAG);
                        request.execute();
                    } catch (Exception e) {
                        clearRef();
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.img_uplaod_doc:
                checkPermission();
                break;
            /*case R.id.tv_calc_days:
                tv_count_days.setVisibility(View.GONE);
                hitApiForCalcDays();
                break;*/
            case R.id.rl_show_leave_detail:
                if (ll_leave_detail.getVisibility() == View.VISIBLE) {
                    img_plus_minus.setBackgroundResource(R.drawable.plus_white);
                    //  ll_leave_detail_row.removeAllViews();
                    //  listLeaveBalanceModel.clear();
                    //  hitApi();
                    ll_leave_detail.setVisibility(View.GONE);
                } else {
                    // ll_leave_detail.setVisibility(View.GONE);
                    img_plus_minus.setBackgroundResource(R.drawable.minus_white);
                    //      ll_leave_detail.setVisibility(View.VISIBLE);
                    ll_leave_detail_row.removeAllViews();
                    listLeaveBalanceModel.clear();
                    hitApi();
                    ll_leave_detail.setVisibility(View.VISIBLE);
                }
                break;


            case R.id.ll_first_half:
                if (rd_first_half.isChecked()) {
                    rd_first_half.setChecked(false);
                } else {
                    rd_first_half.setChecked(true);
                    if (fromDateString.equals(toDateString)) {
                        rd_sec_half.setChecked(false);
                    }
                }
                hitApiForCalcDays();
                break;
            case R.id.ll_sec_half:
                if (rd_sec_half.isChecked()) {
                    rd_sec_half.setChecked(false);
                } else {
                    rd_sec_half.setChecked(true);
                    if (fromDateString.equals(toDateString)) {
                        rd_first_half.setChecked(false);
                    }
                }
                hitApiForCalcDays();
                break;


        }
    }

    void initView() {
        pref = MySharedPreference.getInstance(getActivity());
        getCurrentDateTime();
        spnr_leave_type = (Spinner) viewMain.findViewById(R.id.spnr_leave_type);
        tv_submit_regularization_type = (TextView) viewMain.findViewById(R.id.tv_submit_regularization_type);
        tv_to_date = (TextView) viewMain.findViewById(R.id.tv_to_date);
        tv_from_date = (TextView) viewMain.findViewById(R.id.tv_from_date);
        //  tv_choose_file = (TextView) viewMain.findViewById(R.id.tv_choose_file);
        et_reason = (EditText) viewMain.findViewById(R.id.et_reason);
        et_leave_address = (EditText) viewMain.findViewById(R.id.et_leave_address);
        et_contact = (EditText) viewMain.findViewById(R.id.et_contact);
        ll_leave_detail_row = (LinearLayout) viewMain.findViewById(R.id.ll_leave_detail_row);

        rl_show_leave_detail = (RelativeLayout) viewMain.findViewById(R.id.rl_show_leave_detail);
        img_plus_minus = (ImageView) viewMain.findViewById(R.id.img_plus_minus);
        ll_leave_detail = (LinearLayout) viewMain.findViewById(R.id.ll_leave_detail);
        //tv_calc_days = (TextView) viewMain.findViewById(R.id.tv_calc_days);
        tv_count_days = (TextView) viewMain.findViewById(R.id.tv_count_days);
        rd_first_half = (RadioButton) viewMain.findViewById(R.id.rd_first_half);
        rd_sec_half = (RadioButton) viewMain.findViewById(R.id.rd_sec_half);

        ll_first_half = (LinearLayout) viewMain.findViewById(R.id.ll_first_half);
        ll_sec_half = (LinearLayout) viewMain.findViewById(R.id.ll_sec_half);

        ll_to_date = (LinearLayout) viewMain.findViewById(R.id.ll_to_date);
        ll_from_date = (LinearLayout) viewMain.findViewById(R.id.ll_from_date);
        tv_rm_name = (TextView) viewMain.findViewById(R.id.tv_rm_name);
     //   tv_selected_file_name = (TextView) viewMain.findViewById(R.id.tv_selected_file_name);

        tv_rm_name.setText(pref.getsharedPreferenceData().getRMName());

       // rl_choose_file = (RelativeLayout) viewMain.findViewById(R.id.rl_choose_file);
        rl_show_leave_detail.setOnClickListener(this);
        tv_submit_regularization_type.setOnClickListener(this);
        rl_show_leave_detail.setOnClickListener(this);
        ll_upload_doc = (LinearLayout) viewMain.findViewById(R.id.ll_upload_doc);
        img_uplaod_doc = (ImageView) viewMain.findViewById(R.id.img_uplaod_doc);
        img_set_image = (ImageView) viewMain.findViewById(R.id.img_set_image);
        ll_leave_detail.setOnClickListener(this);
        //tv_calc_days.setOnClickListener(this);
        ll_first_half.setOnClickListener(this);
        ll_sec_half.setOnClickListener(this);
        ll_to_date.setOnClickListener(this);
        ll_from_date.setOnClickListener(this);
        img_uplaod_doc.setOnClickListener(this);

        tv_from_date.setText("" + curDay + "-" + (curMonth + 1) + "-" + curYear);
        tv_to_date.setText("" + curDay + "-" + (curMonth + 1) + "-" + curYear);
        //fromDateString = toDateString = (curMonth + 1) + "/" + curDay + "/" + curYear;
        fromDateString = toDateString = curDay + "/" + (curMonth + 1) + "/" + curYear;
        spnr_leave_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (spnr_leave_type.getSelectedItemPosition() != 0) {
                    hitApiForCalcDays();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        hitApi();
    }

    void hitApi() {
        try {
            request = new ProjectWebRequest(getActivity(), getParam(), UrlConstants.GET_LEAVE_TYPE, this, UrlConstants.GET_LEAVE_TYPE_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    void hitApiForSubmitLeaveRequest(String noOfDays) {
        try {
            request = new ProjectWebRequest(getActivity(), getParamToSubmitLeaveRequest(noOfDays), UrlConstants.SUBMIT_LEAVE_REQUEST, this, UrlConstants.SUBMIT_LEAVE_REQUEST_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

    void hitApiForCalcDays() {
        if (validateForCalDays())
            try {
                request = new ProjectWebRequest(getActivity(), getParamForCalDays(), UrlConstants.LEAVE_CALC_DAYS, this, UrlConstants.LEAVE_CALC_DAYS_TAG);
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
        } catch (Exception e) {
        }
        return object;
    }

    JSONObject getParamForCalDays() {
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(PreferenceModel.TokenKey, PreferenceModel.TokenValues);
            object.put("userID", pref.getsharedPreferenceData().getUserId());
            object.put("LeaveTypeID", leaveTypeList.get(spnr_leave_type.getSelectedItemPosition()).getLeaveTypeId());
            if (rd_sec_half.isChecked()) {
                object.put("SecondHalf", "1");
            } else {
                object.put("SecondHalf", "0");
            }

            if (rd_first_half.isChecked()) {
                object.put("FirstHalf", "1");
            } else {
                object.put("FirstHalf", "0");
            }

            object.put("FromDate", fromDateString);
            object.put("ToDate", toDateString);
        } catch (Exception e) {
        }
        return object;
    }


    JSONObject getParamToSubmitLeaveRequest(String noOfDays) {
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(PreferenceModel.TokenKey, PreferenceModel.TokenValues);
            object.put("UserId", pref.getsharedPreferenceData().getUserId());
            object.put("LeaveTypeID", leaveTypeList.get(spnr_leave_type.getSelectedItemPosition()).getLeaveTypeId());
            object.put("LeaveTypeText", leaveTypeList.get(spnr_leave_type.getSelectedItemPosition()).getLeaveType());
            object.put("FromDate", fromDateString);
            object.put("ToDate", toDateString);
            object.put("NoOfDays", noOfDays);
            object.put("ReasonForLeave", et_reason.getText().toString().trim());
            object.put("Address", et_leave_address.getText().toString().trim());
            object.put("ContactNo", et_contact.getText().toString().trim());
            if (rd_first_half.isChecked()) {
                object.put("FirstHalf", "1");
            } else {
                object.put("FirstHalf", "0");
            }

            if (rd_sec_half.isChecked()) {
                object.put("SecondHalf", "1");
            } else {
                object.put("SecondHalf", "0");
            }

            object.put("DeliveryDate", "08/02/2018");
            object.put("ApplyFor", "1");
            object.put("CompanyID", companyId);
            object.put("RMID", pref.getsharedPreferenceData().getRMId());
            object.put("PlantID", pref.getsharedPreferenceData().getPlantID());

            if (encodedImageData != null) {

                object.put("FileInBase64", encodedImageData);
                object.put("FileExt", ".jpg");
            } else {
                object.put("FileInBase64", "");
                object.put("FileExt", "");
            }

            JSONArray array = new JSONArray();
            if (leaveTypeList.get(spnr_leave_type.getSelectedItemPosition()).getLeaveTypeId().equals("5")) {

              /*  for (int i = 0; i < compOffList.size(); i++) {
                    if (compOffList.get(i).isChecked() == true) {
                        JSONObject obj = new JSONObject();
                        if (compOffList.get(i).getCompOffID() != null) {
                            obj.put("CompOffID", compOffList.get(i).getCompOffID());
                            array.put(obj);
                        } else {
                            obj.put("CompOffID", "");
                            array.put(obj);
                        }

                    }
                }*/
            } else {
                JSONObject obj = new JSONObject();
                obj.put("CompOffID", "");
                array.put(obj);
            }
            object.put("LeaveCompOffReq", array);
        } catch (Exception e) {
        }
        return object;
    }

    @Override
    public void onSuccess(JSONObject obj, int Tag) {
        clearRef();
        if (obj.optString("Status").equals("true")) {
            if (Tag == UrlConstants.GET_LEAVE_TYPE_TAG) {
                if (obj.optString("Status").equals("true")) {
                    companyId = obj.optString("CompanyID");
                    et_leave_address.setText(obj.optString("Address"));
                    et_leave_address.setSelection(et_leave_address.getText().length());
                    et_contact.setText(obj.optString("MobileNo"));
                    et_contact.setSelection(et_contact.getText().length());
                    leaveTypeList = new ArrayList<LeaveTypeModel>();
                    LeaveTypeModel model;
                    try {
                        model = new LeaveTypeModel();
                        model.setLeaveType("Please Select Type");
                        model.setLeaveTypeId("-1");
                        leaveTypeList.add(model);
                        JSONArray arr = obj.getJSONArray("objLeaveTypeRes");
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject object = arr.getJSONObject(i);
                            model = new LeaveTypeModel();
                            model.setLeaveTypeId(object.optString("LeaveTypeID"));
                            model.setLeaveType(object.optString("LeaveType"));
                            leaveTypeList.add(model);
                        }
                        spnr_leave_type.setAdapter(new LeaveTypeListAdapter(mainActivity, leaveTypeList));

                        listLeaveBalanceModel = new ArrayList<>();
                        for (int i = 0; i < obj.getJSONArray("objBalanceLeave").length(); i++) {
                            LeaveBalanceModel data = new LeaveBalanceModel();
                            data.setBalanceLeave(obj.getJSONArray("objBalanceLeave").optJSONObject(i).optString("NoOfBalanceLeave"));
                            data.setPendingLeave(obj.getJSONArray("objPendingLeave").optJSONObject(i).optString("NoOfPendingLeave"));
                            data.setTakenLeave(obj.getJSONArray("objTakenLeave").optJSONObject(i).optString("NoOfTakenLeave"));
                            data.setTypeOfLeave(obj.getJSONArray("objTotalLeave").optJSONObject(i).optString("LeaveType"));
                            data.setNoOfLeave(obj.getJSONArray("objTotalNoofLeave").optJSONObject(i).optString("NoOfLeave"));
                            listLeaveBalanceModel.add(data);
                        }
                        initLeaveDetail();
                    } catch (Exception e) {

                    }
                }

            } else if (Tag == UrlConstants.LEAVE_CALC_DAYS_TAG) {
                if (obj.optString("Status").equals("true")) {
                    tv_count_days.setVisibility(View.VISIBLE);
                    tv_count_days.setText(obj.optString("NoOfDays") + " Day");
                    String NoOfDays = obj.optString("NoOfDays");
                    //  int mDays=Integer.parseInt(NoOfDays);
                    mDays = Double.valueOf(NoOfDays);
                    if (mDays > 3.0 && leaveTypeList.get(spnr_leave_type.getSelectedItemPosition()).getLeaveType().equals("Sick Leave")) {
                        ll_upload_doc.setVisibility(View.VISIBLE);
                    } else {
                        ll_upload_doc.setVisibility(View.GONE);

                    }
                }
            } else if (Tag == UrlConstants.SUBMIT_LEAVE_REQUEST_TAG) {
                if (obj.optString("Status").equals("true")) {
                    FragmentLeaveMain frag = (FragmentLeaveMain) getParentFragment();
                    frag.changePageFromOtherFragment(1);
                    et_reason.setText("");
                    et_leave_address.setText("");

                    AppAlertDialog.showDialogSelfFinish(mainActivity, "Leave Request", obj.optString("Message"));
                    listLeaveBalanceModel.clear();
                    ll_leave_detail_row.removeAllViews();
                    hitApi();
                    //getActivity().finish();
                } else {
                    AppAlertDialog.showDialogSelfFinish(mainActivity, "Alert", obj.optString("Message"));
                }
            }
        } else {
            AppAlertDialog.showDialogSelfFinish(mainActivity, "Alert", obj.optString("Message"));
            if (obj.optString("Message").contains("medical")) {
                ll_upload_doc.setVisibility(View.VISIBLE);
            } else {
                ll_upload_doc.setVisibility(View.GONE);
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

    void getCurrentDateTime() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        fromYearSelected = curYear = c.get(Calendar.YEAR);
        fromMonthSelected = curMonth = c.get(Calendar.MONTH);
        fromDaySelected = curDay = c.get(Calendar.DAY_OF_MONTH);
    }

    void fromDatePicker() {
        DatePickerDialog expdatePickerDialog = null;
        expdatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tv_from_date.setText("" + dayOfMonth + "-" + (month + 1) + "-" + year);
                fromYearSelected = year;
                fromDaySelected = dayOfMonth;
                fromMonthSelected = month;

                //fromDateString = toDateString = (fromMonthSelected + 1) + "/" + fromDaySelected + "/" + fromYearSelected;
                fromDateString = toDateString = fromDaySelected + "/" + (fromMonthSelected + 1) + "/" + fromYearSelected;
                tv_to_date.setText("" + fromDaySelected + "-" + (fromMonthSelected + 1) + "-" + fromYearSelected);
                hitApiForCalcDays();
            }
        }, fromYearSelected, fromMonthSelected, fromDaySelected);
        expdatePickerDialog.show();
    }

    boolean startValidation() {
        if (spnr_leave_type.getSelectedItemPosition() == 0) {
            Toast.makeText(mainActivity, "Please select Leave Type", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (fromDateString == null) {
            Toast.makeText(mainActivity, "Please select From Date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (toDateString == null) {
            Toast.makeText(mainActivity, "Please select To Date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (et_reason.getText().toString().trim().length() <= 0) {
            Toast.makeText(mainActivity, "Please fill the reason of leave", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (et_leave_address.getText().toString().trim().length() <= 0) {
            Toast.makeText(mainActivity, "Please fill the address during leave ", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (et_contact.getText().toString().trim().length() <= 0) {
            Toast.makeText(mainActivity, "Please fill the contact number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (et_contact.getText().toString().trim().length() < 10) {
            Toast.makeText(mainActivity, "Please enter the valid number", Toast.LENGTH_SHORT).show();
            return false;
        }
       /* if (mDays > 3) {
            if (img_set_image.getDrawable()==null) {
                Toast.makeText(mainActivity, "Please upload the document/file", Toast.LENGTH_SHORT).show();
                return false;
            }
        }*/

        if (mDays > 3) {
            if ( leaveTypeList.get(spnr_leave_type.getSelectedItemPosition()).getLeaveType().equals("Sick Leave")){
                if (img_set_image.getDrawable()==null) {
                    Toast.makeText(mainActivity, "Please upload the document/file", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }

        }


        return true;

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
                //toDateString = (month + 1) + "/" + dayOfMonth + "/" + year;
                toDateString = dayOfMonth + "/" + (month + 1) + "/" + year;
                tv_to_date.setText("" + dayOfMonth + "-" + (month + 1) + "-" + year);
                hitApiForCalcDays();
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

    boolean validateForCalDays() {
        if (spnr_leave_type.getSelectedItemPosition() == 0) {
            Toast.makeText(mainActivity, "Please select Leave Type", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (fromDateString == null) {
            Toast.makeText(mainActivity, "Please select From Date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (toDateString == null) {
            Toast.makeText(mainActivity, "Please select To Date", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    private void checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((getActivity().checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED)) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        PERMISSIONS_REQUEST_CAMERA);
            } else {
                pickFileFromGallary();
            }
        } else
            pickFileFromGallary();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void pickFileFromGallary() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    //  if(result)
                    cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    //   if(result)
                    galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        final Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        //ImageName = String.valueOf(destination).substring(String.valueOf(destination).lastIndexOf("/") + 1);
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        encodedImageData = getEncoded64ImageStringFromBitmap(thumbnail);

        img_set_image.setImageBitmap(thumbnail);


    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                String file = String.valueOf(data.getData());
                filename = file.substring(file.lastIndexOf("/") + 1);
                encodedImageData = getEncoded64ImageStringFromBitmap(bm);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        img_set_image.setImageBitmap(bm);
    }


    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return imgString;
    }

    /* void checkPermission() {
        if (MyCheckPermission.checkAppPermission(mainActivity, Manifest.permission.CAMERA)) {
            if (MyCheckPermission.checkAppPermission(mainActivity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                pickImageFromCameraAndGallery();
            } else {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1999);
            }
        } else {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1999);
        }
    }

    private void pickImageFromCameraAndGallery() {
        final Dialog dialog = new Dialog(mainActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_select_image_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        LinearLayout galleryImage = (LinearLayout) dialog.findViewById(R.id.layout_gallery_id);
        LinearLayout cameraImage = (LinearLayout) dialog.findViewById(R.id.layout_camera_id);
        galleryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_REQUEST);
                dialog.dismiss();
            }
        });
        cameraImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST);

               *//* File CaptureImagePath = new File(Environment.getExternalStorageDirectory() + File.separator + "Officenet");
                if (!CaptureImagePath.exists()) {
                    CaptureImagePath.mkdir();
                }
                //capturedImages = new File(CaptureImagePath, "Temp.png");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    try {
                        capturedImages = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    capturedImages = createImageFile4();
                }
                String path = capturedImages.getAbsolutePath();
                *//**//*if (!capturedImages.exists()) {
                    try {
                        capturedImages.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }*//**//*
                try {
                    Uri tempURI = FileProvider.getUriForFile(mainActivity, "com.netcomm.hfcl.fileprovider", capturedImages);
                    Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    photoCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempURI);
                    startActivityForResult(photoCaptureIntent, CAMERA_REQUEST);
                } catch (Exception e) {
                    e.printStackTrace();
                }*//*
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private File createImageFile4()
    {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Officenet");
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                //displayMessage(getBaseContext(),"Unable to create directory.");
                return null;
            }
        }

        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "Temp.png");

        return mediaFile;

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                "Temp",  *//* prefix *//*
                ".png",  *//* suffix *//*
                storageDir      *//* directory *//*
        );

        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1999:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        pickImageFromCameraAndGallery();
                    } else {
                        Toast.makeText(mainActivity, "You have to accept these permission ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mainActivity, "You have to accept these permission ", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            mFileUploadModel = new FileUploadModel();
            if (requestCode == GALLERY_REQUEST) {
                try {
                    final String fileName = FileUtils.getPathFromURI(mainActivity, data.getData());
                    mFileUploadModel.setFileExtension(fileName.substring(fileName.lastIndexOf(".") + 1));
                    File f = new File(FileUtils.compressNow(mainActivity, fileName));
                    convertIntoBase64(f.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == CAMERA_REQUEST) {
              *//*  mFileUploadModel.setFileExtension(".png");
                File f = new File(FileUtils.compressNow(mainActivity, capturedImages.getAbsolutePath()));
                convertIntoBase64(f.getAbsolutePath());*//*

                onCaptureImageResult(data);
            }
        } else {
            mFileUploadModel = null;
            tv_selected_file_name.setText("No File Selected");
        }
    }



    private void onCaptureImageResult(Intent data) {
        final Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    String    encodedImageData = getEncoded64ImageStringFromBitmap(thumbnail);

       // img_set_image.setImageBitmap(thumbnail);
        tv_selected_file_name.setText(String.valueOf(destination));


    }


    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return imgString;
    }
*/
    void checkTypeOfLeaveRequest() {
        /*if (leaveTypeList.get(spnr_leave_type.getSelectedItemPosition()).getLeaveTypeId().equals("3")) {
            if (totalDayToTakeLeaveCount > 2) {
                Toast.makeText(mainActivity, "CL should not be applied for more than 2 days. You Applied For " + totalDayToTakeLeaveCount + "day/s.", Toast.LENGTH_SHORT).show();
                return;
            }
        } else if (leaveTypeList.get(spnr_leave_type.getSelectedItemPosition()).getLeaveTypeId().equals("4")) {
            if (totalDayToTakeLeaveCount > 7) {
                Toast.makeText(mainActivity, "Paternity leave can be applied for maximum of 7 days", Toast.LENGTH_SHORT).show();
                return;
            }
        } else if (leaveTypeList.get(spnr_leave_type.getSelectedItemPosition()).getLeaveTypeId().equals("10")) {
            if (mFileUploadModel == null) {
                Toast.makeText(mainActivity, "Please Select a document first", Toast.LENGTH_SHORT).show();
                return;
            }
        } else if (leaveTypeList.get(spnr_leave_type.getSelectedItemPosition()).getLeaveTypeId().equals("5")) {


        } else if (leaveTypeList.get(spnr_leave_type.getSelectedItemPosition()).getLeaveTypeId().equals("1")) {
            if (totalDayToTakeLeaveCount < 3) {
                Toast.makeText(mainActivity, "Earned Leave(EL) should be atleast 3 days long. You Applied For " + totalDayToTakeLeaveCount + "day/s.", Toast.LENGTH_SHORT).show();
                return;
            } else if (totalDayToTakeLeaveCount > 15) {


            }
        } else if (leaveTypeList.get(spnr_leave_type.getSelectedItemPosition()).getLeaveTypeId().equals("2")) {
        } else if (leaveTypeList.get(spnr_leave_type.getSelectedItemPosition()).getLeaveTypeId().equals("7")) {

        }*/


        hitApiForSubmitLeaveRequest("" + totalDayToTakeLeaveCount);
    }


    private void convertIntoBase64(final String fileName) {
        final ProgressDialog progressDoalog = new ProgressDialog(mainActivity);
        progressDoalog.setMessage("Please wait....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.setCancelable(false);
        progressDoalog.show();
        Thread mThread = new Thread() {
            @Override
            public void run() {
                String encodedImage = null;
                try {
                    Bitmap bm = BitmapFactory.decodeFile(fileName);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mFileUploadModel.setFileContent(encodedImage);
                mFileUploadModel.setFileName("" + System.currentTimeMillis());
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_selected_file_name.setText(fileName);
                        progressDoalog.dismiss();
                    }
                });
            }
        };
        mThread.start();
    }

    void initLeaveDetail() {
        for (int i = 0; i < listLeaveBalanceModel.size(); i++) {
            LinearLayout v = (LinearLayout) mainActivity.getLayoutInflater().inflate(R.layout.leave_detail_row, null);
            TextView type_leave = (TextView) v.findViewById(R.id.type_leave);
            TextView taken_leave = (TextView) v.findViewById(R.id.taken_leave);
            TextView pending_leave = (TextView) v.findViewById(R.id.pending_leave);
            TextView balance_leave = (TextView) v.findViewById(R.id.balance_leave);
            type_leave.setText(listLeaveBalanceModel.get(i).getTypeOfLeave() + "(" + listLeaveBalanceModel.get(i).getNoOfLeave() + ")");
            taken_leave.setText(listLeaveBalanceModel.get(i).getTakenLeave());
            pending_leave.setText(listLeaveBalanceModel.get(i).getPendingLeave());
            balance_leave.setText(listLeaveBalanceModel.get(i).getBalanceLeave());
            ll_leave_detail_row.addView(v);
        }
    }

}
