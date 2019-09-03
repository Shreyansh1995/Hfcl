package com.netcomm.hfcl.services;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.netcomm.hfcl.interfaces.ResponseListener;
import com.netcomm.hfcl.utils.MyApp;
import com.netcomm.hfcl.utils.NetworkUtils;

import org.json.JSONObject;

/**
 * Created by Netcomm on 7/12/2017.
 */

public class StringRequestProject {

    private Context mContext;
    private String url;
    private ResponseListener listener;
    private int Tag;


    public StringRequestProject(Context mContext, String url, ResponseListener listener, int Tag) {
        this.mContext = mContext;
        this.url = url;
        this.listener = listener;
        this.Tag = Tag;
    }

    synchronized public void execute() {
        if (NetworkUtils.isConnected(mContext)) {
            Log.e("@@@@@@URL->>>>>>>>>>>", url);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                listener.onSuccess(obj, Tag);
                            } catch (Exception e) {
                                e.printStackTrace();
                                listener.onFailure(null,Tag);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    listener.onFailure(error,Tag);
                }
            });
            MyApp.getInstance().addToRequestQueue(stringRequest, "" + Tag);
            System.gc();
        }
    }
}
