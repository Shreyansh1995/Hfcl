package com.netcomm.hfcl.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.netcomm.hfcl.MainActivity;
import com.netcomm.hfcl.R;
import com.netcomm.hfcl.adapter.AttendanceRegularizationMainAdapter;
import com.netcomm.hfcl.sharedpreference.MySharedPreference;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceRegularizationMain extends Fragment implements View.OnClickListener {
    private View viewMain;
    private MainActivity mainActivity;
    private MySharedPreference pref;
    private ViewPager pager2;
    private LinearLayout ll_new_request;
    private LinearLayout ll_request_details;
    private LinearLayout ll_archive_request;
    private LinearLayout ll_pending_request;
    private LinearLayout header;
    private int totalTab = 4;
    private ImageView iv_new_request;
    private ImageView iv_request_details;
    private ImageView iv_archive_request;
    private ImageView iv_pending_request;
    AttendanceRegularizationMainAdapter adapter;

    public AttendanceRegularizationMain() {
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
        viewMain = inflater.inflate(R.layout.fragment_attendance_regularization_main, container, false);

        initView();

        return viewMain;
    }

    void initView() {
        pref = MySharedPreference.getInstance(mainActivity);
        pager2 = (ViewPager) viewMain.findViewById(R.id.pager2);
        ll_new_request = (LinearLayout) viewMain.findViewById(R.id.ll_new_request);
        ll_request_details = (LinearLayout) viewMain.findViewById(R.id.ll_request_details);
        ll_archive_request = (LinearLayout) viewMain.findViewById(R.id.ll_archive_request);
        ll_pending_request = (LinearLayout) viewMain.findViewById(R.id.ll_pending_request);
        header = (LinearLayout) viewMain.findViewById(R.id.header);
        iv_new_request = (ImageView) viewMain.findViewById(R.id.iv_new_request);
        iv_request_details = (ImageView) viewMain.findViewById(R.id.iv_request_details);
        iv_archive_request = (ImageView) viewMain.findViewById(R.id.iv_archive_request);
        iv_pending_request = (ImageView) viewMain.findViewById(R.id.iv_pending_request);

        ll_new_request.setOnClickListener(this);
        ll_request_details.setOnClickListener(this);
        ll_archive_request.setOnClickListener(this);
        ll_pending_request.setOnClickListener(this);

        if (pref.getsharedPreferenceData().getIsRM().equals("True")) {
            totalTab = 4;
        } else {
            totalTab = 2;
            ll_archive_request.setVisibility(View.GONE);
            ll_pending_request.setVisibility(View.GONE);
        }

        /*if (pref.getsharedPreferenceData().getArchiveAccess()) {
            if (pref.getsharedPreferenceData().getPendingAccess()) {
                totalTab = 4;
            } else {
                totalTab = 3;
                ll_archive_request.setVisibility(View.VISIBLE);
                ll_pending_request.setVisibility(View.GONE);
            }
        } else {
            totalTab = 2;
            ll_archive_request.setVisibility(View.GONE);
            ll_pending_request.setVisibility(View.GONE);
        }*/

 adapter = new AttendanceRegularizationMainAdapter(getChildFragmentManager(), totalTab);
        pager2.setOffscreenPageLimit(totalTab);
        pager2.setAdapter(adapter);
        pager2.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (totalTab != 3) {
                    changeTabOnCaseOf4And2Tabs(position);
                    adapter.notifyDataSetChanged();
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
                iv_new_request.setBackgroundResource(R.drawable.new_request_active);
                iv_request_details.setBackgroundResource(R.drawable.request_details);
                iv_archive_request.setBackgroundResource(R.drawable.archived_request);
                iv_pending_request.setBackgroundResource(R.drawable.pending_request);
                break;
            case 1:
                iv_new_request.setBackgroundResource(R.drawable.new_request);
                iv_request_details.setBackgroundResource(R.drawable.request_details_active);
                iv_archive_request.setBackgroundResource(R.drawable.archived_request);
                iv_pending_request.setBackgroundResource(R.drawable.pending_request);
                break;
            case 2:
                iv_new_request.setBackgroundResource(R.drawable.new_request);
                iv_request_details.setBackgroundResource(R.drawable.request_details);
                iv_archive_request.setBackgroundResource(R.drawable.archived_request);
                iv_pending_request.setBackgroundResource(R.drawable.pending_request_active);
                break;
            case 3:
                iv_new_request.setBackgroundResource(R.drawable.new_request);
                iv_request_details.setBackgroundResource(R.drawable.request_details);
                iv_archive_request.setBackgroundResource(R.drawable.archived_request_active);
                iv_pending_request.setBackgroundResource(R.drawable.pending_request);
                break;
        }
    }

    void changeTabOnCaseOf3Tabs(int position) {
        switch (position) {
            case 0:
                iv_new_request.setBackgroundResource(R.drawable.new_request_active);
                iv_request_details.setBackgroundResource(R.drawable.request_details);
                iv_archive_request.setBackgroundResource(R.drawable.archived_request);
                iv_pending_request.setBackgroundResource(R.drawable.pending_request);
                break;
            case 1:
                iv_new_request.setBackgroundResource(R.drawable.new_request);
                iv_request_details.setBackgroundResource(R.drawable.request_details_active);
                iv_archive_request.setBackgroundResource(R.drawable.archived_request);
                iv_pending_request.setBackgroundResource(R.drawable.pending_request);
                break;
            case 2:
                iv_new_request.setBackgroundResource(R.drawable.new_request);
                iv_request_details.setBackgroundResource(R.drawable.request_details);
                iv_archive_request.setBackgroundResource(R.drawable.archived_request_active);
                iv_pending_request.setBackgroundResource(R.drawable.pending_request);
                break;
            case 3:
                iv_new_request.setBackgroundResource(R.drawable.new_request);
                iv_request_details.setBackgroundResource(R.drawable.request_details);
                iv_archive_request.setBackgroundResource(R.drawable.archived_request);
                iv_pending_request.setBackgroundResource(R.drawable.pending_request_active);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_new_request:
                changeTabOnCaseOf4And2Tabs(0);
                pager2.setCurrentItem(0);
                break;
            case R.id.ll_request_details:
                changeTabOnCaseOf4And2Tabs(1);
                pager2.setCurrentItem(1);
                break;
            case R.id.ll_pending_request:
                changeTabOnCaseOf4And2Tabs(2);
                pager2.setCurrentItem(2);
                break;
            case R.id.ll_archive_request:
                changeTabOnCaseOf4And2Tabs(3);
                pager2.setCurrentItem(3);
                break;
        }
    }

    public void changePageFromOtherFragment(int position) {
        pager2.setCurrentItem(position);

    }

}
