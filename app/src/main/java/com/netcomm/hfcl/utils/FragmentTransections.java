package com.netcomm.hfcl.utils;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.List;

import static com.netcomm.hfcl.MainActivity.back_img;
import static com.netcomm.hfcl.MainActivity.toolbar_llayout;
import static com.netcomm.hfcl.MainActivity.toolbar_title;
import static com.netcomm.hfcl.constants.FragmentsTags.DASH_BOARD_FRAGMENT;


/**
 * Created by kumar on 11/4/2017.
 */

public class FragmentTransections {


    public static void replaceFragmnet(Context context, Fragment fragment, String tag, int layout, Boolean isAddFrag) {
        FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        ft.replace(layout, fragment, tag);
        ft.addToBackStack(tag);

        if (!isAddFrag) {
            if (((AppCompatActivity) context).getSupportFragmentManager().getBackStackEntryCount() > 1) {
                for (int i = ((AppCompatActivity) context).getSupportFragmentManager().getBackStackEntryCount(); i > 1; i--) {
                    ((AppCompatActivity) context).getSupportFragmentManager().popBackStack();
                }
            }

        }
        Log.e("FragmentCount", "" + ((AppCompatActivity) context).getSupportFragmentManager().getBackStackEntryCount() + " Tag " + tag);
        ft.commit();

        toolbar_title.setText(tag);
        if (tag.equals(DASH_BOARD_FRAGMENT)) {
          toolbar_llayout.setVisibility(View.GONE);
        }else {
            toolbar_llayout.setVisibility(View.VISIBLE);
            back_img.setVisibility(View.VISIBLE);
            toolbar_title.setVisibility(View.VISIBLE);
        }

    }

    public static void removeAllFragments(FragmentManager fragmentManager) {
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                fragmentManager.beginTransaction().remove(fragment).commit();
            }
        }
    }

//    public Fragment currentFragment(FragmentManager fragmentManager){
//        return fragmentManager.findFragmentById(main_frame_id);
//    }

}
