package com.netcomm.hfcl;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.netcomm.hfcl.sharedpreference.MySharedPreference;


/**
 * Created by Netcomm on 1/13/2017.
 */

public class SplashActivity extends AppCompatActivity {
    private MySharedPreference pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (android.os.Build.VERSION.SDK_INT >= 21)
            getWindow().setStatusBarColor(Color.parseColor("#007d55"));
        pref = MySharedPreference.getInstance(this);
        isLogin();
    }

    private void isLogin() {
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (pref.getsharedPreferenceData().getUserId() != null) {
                        Intent inte = new Intent(SplashActivity.this, MainActivity.class);
                        inte.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(inte);
                        finish();
                    } else {
                       Intent inte = new Intent(SplashActivity.this, LoginActivity.class);
                        inte.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(inte);
                        finish();
                    }
                }
            }, 3000);
        }
    }





}
