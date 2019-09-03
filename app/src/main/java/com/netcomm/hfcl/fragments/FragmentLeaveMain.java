package com.netcomm.hfcl.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.netcomm.hfcl.MainActivity;
import com.netcomm.hfcl.R;
import com.netcomm.hfcl.adapter.LeaveRequestMainAdapter;
import com.netcomm.hfcl.sharedpreference.MySharedPreference;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLeaveMain extends Fragment implements View.OnClickListener {
    private View viewMain;
    private MainActivity mainActivity;
    private MySharedPreference pref;
    private ViewPager pager_leave;
    private LinearLayout ll_new_request_leave;
    private LinearLayout ll_request_details_leave;
    private LinearLayout ll_archive_request_leave;
    private LinearLayout ll_pending_request_leave;


    private ImageView iv_new_request_leave;
    private ImageView iv_request_details_leave;
    private ImageView iv_archive_request_leave;
    private ImageView iv_pending_request_leave;
    private int totalTab = 4;

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


    public FragmentLeaveMain() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewMain = inflater.inflate(R.layout.fragment_fragment_leave_main, container, false);
        initView();
        return viewMain;
    }

    void initView() {
        pref = MySharedPreference.getInstance(mainActivity);
        pager_leave = (ViewPager) viewMain.findViewById(R.id.pager_leave);
        ll_new_request_leave = (LinearLayout) viewMain.findViewById(R.id.ll_new_request_leave);
        ll_request_details_leave = (LinearLayout) viewMain.findViewById(R.id.ll_request_details_leave);
        ll_archive_request_leave = (LinearLayout) viewMain.findViewById(R.id.ll_archive_request_leave);
        ll_pending_request_leave = (LinearLayout) viewMain.findViewById(R.id.ll_pending_request_leave);

        iv_new_request_leave = (ImageView) viewMain.findViewById(R.id.iv_new_request_leave);
        iv_request_details_leave = (ImageView) viewMain.findViewById(R.id.iv_request_details_leave);
        iv_archive_request_leave = (ImageView) viewMain.findViewById(R.id.iv_archive_request_leave);
        iv_pending_request_leave = (ImageView) viewMain.findViewById(R.id.iv_pending_request_leave);

        ll_new_request_leave.setOnClickListener(this);
        ll_request_details_leave.setOnClickListener(this);
        ll_archive_request_leave.setOnClickListener(this);
        ll_pending_request_leave.setOnClickListener(this);

        if (pref.getsharedPreferenceData().getIsRM().equals("True")) {
            totalTab = 4;
        } else {
            totalTab = 2;
            ll_archive_request_leave.setVisibility(View.GONE);
            ll_pending_request_leave.setVisibility(View.GONE);
        }


        // user access has been initialized in FragmentSubMenu.class file for all
        /*if (pref.getsharedPreferenceData().getArchiveAccess()) {
            if (pref.getsharedPreferenceData().getPendingAccess()) {
                totalTab = 4;
            } else {
                totalTab = 3;
                ll_archive_request_leave.setVisibility(View.VISIBLE);
                ll_pending_request_leave.setVisibility(View.GONE);
            }
        } else {
            totalTab = 2;
            ll_archive_request_leave.setVisibility(View.GONE);
            ll_pending_request_leave.setVisibility(View.GONE);
        }*/
        LeaveRequestMainAdapter adapter = new LeaveRequestMainAdapter(getChildFragmentManager(), totalTab);
        pager_leave.setOffscreenPageLimit(totalTab);
        pager_leave.setAdapter(adapter);
        pager_leave.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (totalTab != 3) {
                    Log.e("@@@@@@@@Called", "@@@@@@@@Called");
                    changeTabOnCaseOf4And2Tabs(position);
                } else {
                    changeTabOnCaseOf3Tabs(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    void changeTabOnCaseOf4And2Tabs(int position) {
        switch (position) {
            case 0:
                iv_new_request_leave.setBackgroundResource(R.drawable.new_request_active);
                iv_request_details_leave.setBackgroundResource(R.drawable.request_details);
                iv_archive_request_leave.setBackgroundResource(R.drawable.archived_request);
                iv_pending_request_leave.setBackgroundResource(R.drawable.pending_request);
                break;
            case 1:
                iv_new_request_leave.setBackgroundResource(R.drawable.new_request);
                iv_request_details_leave.setBackgroundResource(R.drawable.request_details_active);
                iv_archive_request_leave.setBackgroundResource(R.drawable.archived_request);
                iv_pending_request_leave.setBackgroundResource(R.drawable.pending_request);
                break;
            case 2:
                iv_new_request_leave.setBackgroundResource(R.drawable.new_request);
                iv_request_details_leave.setBackgroundResource(R.drawable.request_details);
                iv_archive_request_leave.setBackgroundResource(R.drawable.archived_request);
                iv_pending_request_leave.setBackgroundResource(R.drawable.pending_request_active);
                break;
            case 3:
                iv_new_request_leave.setBackgroundResource(R.drawable.new_request);
                iv_request_details_leave.setBackgroundResource(R.drawable.request_details);
                iv_archive_request_leave.setBackgroundResource(R.drawable.archived_request_active);
                iv_pending_request_leave.setBackgroundResource(R.drawable.pending_request);
                break;
        }
    }

    void changeTabOnCaseOf3Tabs(int position) {
        switch (position) {
            case 0:
                iv_new_request_leave.setBackgroundResource(R.drawable.new_request_active);
                iv_request_details_leave.setBackgroundResource(R.drawable.request_details);
                iv_archive_request_leave.setBackgroundResource(R.drawable.archived_request);
                iv_pending_request_leave.setBackgroundResource(R.drawable.pending_request);
                break;
            case 1:
                iv_new_request_leave.setBackgroundResource(R.drawable.new_request);
                iv_request_details_leave.setBackgroundResource(R.drawable.request_details_active);
                iv_archive_request_leave.setBackgroundResource(R.drawable.archived_request);
                iv_pending_request_leave.setBackgroundResource(R.drawable.pending_request);
                break;
            case 2:
                iv_new_request_leave.setBackgroundResource(R.drawable.new_request);
                iv_request_details_leave.setBackgroundResource(R.drawable.request_details);
                iv_archive_request_leave.setBackgroundResource(R.drawable.archived_request);
                iv_pending_request_leave.setBackgroundResource(R.drawable.pending_request_active);
                break;
            case 3:
                iv_new_request_leave.setBackgroundResource(R.drawable.new_request);
                iv_request_details_leave.setBackgroundResource(R.drawable.request_details);
                iv_archive_request_leave.setBackgroundResource(R.drawable.archived_request_active);
                iv_pending_request_leave.setBackgroundResource(R.drawable.pending_request);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_new_request_leave:
                changeTabOnCaseOf4And2Tabs(0);
                pager_leave.setCurrentItem(0);
                break;
            case R.id.ll_request_details_leave:
                changeTabOnCaseOf4And2Tabs(1);
                pager_leave.setCurrentItem(1);
                break;
            case R.id.ll_pending_request_leave:
                changeTabOnCaseOf4And2Tabs(2);
                pager_leave.setCurrentItem(2);
                break;
            case R.id.ll_archive_request_leave:
                changeTabOnCaseOf4And2Tabs(3);
                pager_leave.setCurrentItem(3);
                break;
        }
    }

    public void changePageFromOtherFragment(int position) {
        pager_leave.setCurrentItem(position);

    }

}
