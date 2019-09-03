package com.netcomm.hfcl;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.netcomm.hfcl.constants.UrlConstants;
import com.netcomm.hfcl.interfaces.ResponseListener;
import com.netcomm.hfcl.services.ProjectWebRequest;
import com.netcomm.hfcl.sharedpreference.MySharedPreference;
import com.netcomm.hfcl.sharedpreference.PreferenceModel;

import org.json.JSONObject;

/**
 * Created by Netcomm on 1/10/2017.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ResponseListener {
    private TextView tv_login;
    private EditText et_user_id;
    private EditText et_user_password;
    private ProjectWebRequest request;
    private MySharedPreference pref;
    //private SliderLayout slider_layout;
    private int data[] = {
            R.drawable.login_bnr_1
           };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
        }
        pref = MySharedPreference.getInstance(LoginActivity.this);
        initView();
    }

    void initView() {
        tv_login = (TextView) findViewById(R.id.tv_login);
        et_user_id = (EditText) findViewById(R.id.et_user_id);
        et_user_password = (EditText) findViewById(R.id.et_user_password);
        //slider_layout = (SliderLayout) findViewById(R.id.slider);
        tv_login.setOnClickListener(this);
        //initSlider();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_login:
                if (startValidation()) {
                    hitApi();
                }
                break;
        }
    }

    boolean startValidation() {
        if (et_user_id.getText().toString().trim().length() <= 0) {
            Toast.makeText(this, "Please enter user id", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(et_user_id.getText().toString().trim().contains("@") || et_user_id.getText().toString().trim().contains("\\"))
        {
            Toast.makeText(this, "Character @ and \\ not allowed here", Toast.LENGTH_SHORT).show();
            return false;

        }
        if (et_user_password.getText().toString().trim().length() <= 0) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    void hitApi() {
        try {
            request = new ProjectWebRequest(LoginActivity.this, getParam(), UrlConstants.LOGIN, this, UrlConstants.LOGIN_TAG);
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
            object.put("AdLoginID", et_user_id.getText().toString().trim());
            object.put("AdPassword", et_user_password.getText().toString().trim());
        } catch (Exception e) {
        }
        return object;
    }

    @Override
    public void onSuccess(JSONObject obj, int Tag) {
        clearRef();
        if (obj.optString("Status").equals("true")) {
            if (Tag == UrlConstants.LOGIN_TAG) {
                pref.setAttendanceInput(obj.optString("AttendanceInput"));
                pref.setUserId(obj.optString("UserID"));
                pref.setUserName(obj.optString("UserName"));
                pref.setEmail(obj.optString("EmailID"));
                pref.setEmpCode(obj.optString("EmpCode"));/**/
                pref.setHODId(obj.optString("HODId"));
                pref.setHODName(obj.optString("HODName"));
                pref.setIsRM(obj.optString("IsRM"));
                pref.setPlantID(obj.optString("PlantID"));
                pref.setRMId(obj.optString("RMId"));
                pref.setRMName(obj.optString("RMName"));
                pref.setUserImage(obj.optString("UserImage"));
                pref.setMobile(obj.optString("MobileNo"));
                pref.setLocation(obj.optString("Location"));
                Intent inte = new Intent(this, MainActivity.class);
                inte.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(inte);
                finish();
            }
        } else {
            Toast.makeText(LoginActivity.this, obj.optString("Message"), Toast.LENGTH_SHORT).show();
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

    /*void initSlider() {
        for (int i = 0; i < data.length; i++) {
            DefaultSliderView textSliderView = new DefaultSliderView(this);
            textSliderView.image(data[i]).setScaleType(BaseSliderView.ScaleType.Fit);
            textSliderView.bundle(new Bundle());
            slider_layout.addSlider(textSliderView);
        }
        slider_layout.setPresetTransformer(SliderLayout.Transformer.DepthPage);
        slider_layout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider_layout.setCustomAnimation(new DescriptionAnimation());
        slider_layout.setDuration(8000);
    }*/

}
