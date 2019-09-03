package com.netcomm.hfcl.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import com.netcomm.hfcl.R;


/**
 * Created by Netcomm on 11/1/2016.
 */

public class CustomProgressDialog {

    private static CustomProgressDialog mInstance;
    private Dialog dialog;
    private Context mContext;


    public static CustomProgressDialog getInstance(Context mContext) {
        /*if (mInstance == null) {
            mInstance = new CustomProgressDialog(mContext);
        }
        return mInstance;*/

        if (mInstance != null) {
            return mInstance;
        } else{
            return new CustomProgressDialog(mContext);
        }
    }


    private CustomProgressDialog(Context mContext) {
        this.mContext = mContext;
    }

    public void showProgressBar() {
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        dialog = new Dialog(mContext);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loader_layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        if (! ((Activity) mContext).isFinishing()) {
            dialog.show();
        }
    }

    public void hideProgressBar() {
        /*if (dialog != null)
            dialog.dismiss();*/
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
        dialog = null;
    }
}
