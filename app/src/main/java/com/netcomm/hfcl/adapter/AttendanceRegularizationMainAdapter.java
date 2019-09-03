package com.netcomm.hfcl.adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.netcomm.hfcl.fragments.FragmentAttendanceArchivedRequest;
import com.netcomm.hfcl.fragments.FragmentAttendancePendingRequest;
import com.netcomm.hfcl.fragments.FragmentAttendanceRegularization;
import com.netcomm.hfcl.fragments.FragmentAttendanceRegularizationList;


/**
 * Created by Netcomm on 1/18/2017.
 */

public class AttendanceRegularizationMainAdapter extends FragmentStatePagerAdapter {
    int tabCount;

    public AttendanceRegularizationMainAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Fragment tab1 = new FragmentAttendanceRegularization();
                return tab1;
            case 1:
                Fragment tab2 = new FragmentAttendanceRegularizationList();
                return tab2;
            case 2:
                Fragment tab4 = null;
                if (tabCount == 3) {
                    tab4 = new FragmentAttendanceArchivedRequest();
                } else {
                    tab4 = new FragmentAttendancePendingRequest();
                }
                return tab4;
            case 3:
                Fragment tab3 = new FragmentAttendanceArchivedRequest();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}