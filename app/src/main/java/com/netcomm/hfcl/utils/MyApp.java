package com.netcomm.hfcl.utils;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Netcomm on 1/10/2017.
 */

public class MyApp extends MultiDexApplication {
    public static final String TAG = MyApp.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static MyApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Fabric.with(this, new Crashlytics());

    }

    public static synchronized MyApp getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setShouldCache(false);
        req.setRetryPolicy(new DefaultRetryPolicy(5000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}