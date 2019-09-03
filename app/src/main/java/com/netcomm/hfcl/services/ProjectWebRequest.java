package com.netcomm.hfcl.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.netcomm.hfcl.interfaces.ResponseListener;
import com.netcomm.hfcl.utils.CustomProgressDialog;
import com.netcomm.hfcl.utils.MyApp;
import com.netcomm.hfcl.utils.NetworkUtils;

import org.json.JSONObject;

/**
 * Created by Netcomm on 1/10/2017.
 */

public class ProjectWebRequest {
    private Context mContext;
    private JSONObject object;
    private String url;
    private ResponseListener listener;
    private CustomProgressDialog dialog;
    private int Tag;


    public ProjectWebRequest(Context mContext, JSONObject object, String url, ResponseListener listener, int Tag) {
        this.mContext = mContext;
        this.object = object;
        this.url = url;
        this.listener = listener;
        this.Tag = Tag;
        dialog = CustomProgressDialog.getInstance(mContext);
    }

    synchronized public void execute() {
        if (NetworkUtils.isConnected(mContext)) {
            dialog.showProgressBar();
            Log.e("@@@@@@URL->>>>>>>>>>>", url);
            Log.e("@@@@@@PARAM->>>>>>>>>", object.toString());
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (dialog != null) {
                                dialog.hideProgressBar();
                            }
                            listener.onSuccess(response, Tag);
                            Log.e("@@@@@@@@", response.toString());
                        }
                    }, new Response.ErrorListener() {


                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.hideProgressBar();
                    error.printStackTrace();
                    listener.onFailure(error, Tag);
                    if (error instanceof NetworkError) {
                    } else if (error instanceof ServerError) {
                    } else if (error instanceof AuthFailureError) {
                    } else if (error instanceof ParseError) {
                    } else if (error instanceof NoConnectionError) {
                    } else if (error instanceof TimeoutError) {
                      //  Toast.makeText(mContext, "Please check your login Id and Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            );
            MyApp.getInstance().addToRequestQueue(jsonObjReq, "" + Tag);
            System.gc();
        } else {
            if (mContext != null)
                Toast.makeText(mContext, "No internet connection found", Toast.LENGTH_LONG).show();
            return;
        }
    }


}
