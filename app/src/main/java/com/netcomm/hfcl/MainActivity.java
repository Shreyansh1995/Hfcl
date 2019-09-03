package com.netcomm.hfcl;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netcomm.hfcl.fragments.FragmentDashboard;
import com.netcomm.hfcl.sharedpreference.MySharedPreference;
import com.netcomm.hfcl.utils.FragmentTransections;

import static com.netcomm.hfcl.constants.FragmentsTags.DASH_BOARD_FRAGMENT;
import static com.netcomm.hfcl.constants.FragmentsTags.MARK_ATTENDANCE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout ll_logout;
    public static ImageView back_img;
    public static LinearLayout toolbar_llayout;
    public static TextView toolbar_title;
    public static int main_frame_id = R.id.main_farme;
    private boolean exit = false;

    private MySharedPreference pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
        }

        pref = MySharedPreference.getInstance(MainActivity.this);

        init();

    }

    private void init() {
        ll_logout = (LinearLayout)findViewById(R.id.ll_logout);
        toolbar_llayout = (LinearLayout)findViewById(R.id.toolbar_llayout);
        back_img = (ImageView)findViewById(R.id.back_img);
        toolbar_title = (TextView)findViewById(R.id.toolbar_title);

        ll_logout.setOnClickListener(this);
        toolbar_llayout.setOnClickListener(this);

        FragmentTransections.replaceFragmnet(this, new FragmentDashboard(), DASH_BOARD_FRAGMENT, R.id.main_farme, true);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_logout:
                Intent inte = new Intent(MainActivity.this, LoginActivity.class);
                inte.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(inte);
                pref.clearAll();
                finish();
                break;
            case R.id.toolbar_llayout:
                onBackPressed();
                break;
        }
    }

    void changeTitle(String title) {
        toolbar_title.setText(title);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() >= 2) {
            super.onBackPressed();
        } else {
            if (!exit) {
                Toast.makeText(this, "Press back press to exit", Toast.LENGTH_SHORT).show();
                exit = true;
            } else {
                // LocationUtils.getInstance(this, this).onStop();
                finish();
            }
        }

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(main_frame_id);
        changeTitle(currentFragment.getTag());

        if (currentFragment instanceof FragmentDashboard) {
          /*  back_img.setVisibility(View.GONE);
            toolbar_title.setVisibility(View.GONE);*/
            toolbar_llayout.setVisibility(View.GONE);

        } else {
            toolbar_llayout.setVisibility(View.VISIBLE);

     /*       back_img.setVisibility(View.VISIBLE);
            toolbar_title.setVisibility(View.VISIBLE);*/
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment frg = getSupportFragmentManager().findFragmentByTag(MARK_ATTENDANCE);
        if (frg != null) {
            frg.onActivityResult(requestCode, resultCode, data);
        }
    }
}