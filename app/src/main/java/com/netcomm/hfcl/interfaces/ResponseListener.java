package com.netcomm.hfcl.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by Netcomm on 1/10/2017.
 */

public interface ResponseListener {

    void onSuccess(JSONObject call, int Tag);
    void onFailure(VolleyError error, int Tag);

}
