package com.netcomm.hfcl.adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.netcomm.hfcl.fragments.FragmentLeaveRequestArchiveList;
import com.netcomm.hfcl.fragments.FragmentLeaveRequestForm;
import com.netcomm.hfcl.fragments.FragmentLeaveRequestList;
import com.netcomm.hfcl.fragments.FragmentLeaveRequestPendingList;

/**
 * Created by Netcomm on 8/25/2017.
 */

public class LeaveRequestMainAdapter extends FragmentStatePagerAdapter {
    int tabCount;

    public LeaveRequestMainAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Fragment tab1 = new FragmentLeaveRequestForm();
                return tab1;
            case 1:
                Fragment tab2 = new FragmentLeaveRequestList();
                return tab2;
            case 2:
                Fragment tab4 = null;
                if (tabCount == 3) {
                    tab4 = new FragmentLeaveRequestArchiveList();
                } else {
                    tab4 = new FragmentLeaveRequestPendingList();
                }
                return tab4;
            case 3:
                Fragment tab3 = new FragmentLeaveRequestArchiveList();
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