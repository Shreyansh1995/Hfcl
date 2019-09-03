package com.netcomm.hfcl.fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.netcomm.hfcl.MainActivity;
import com.netcomm.hfcl.R;
import com.netcomm.hfcl.constants.UrlConstants;
import com.netcomm.hfcl.interfaces.LocationDecetor;
import com.netcomm.hfcl.interfaces.ResponseListener;
import com.netcomm.hfcl.services.ProjectWebRequest;
import com.netcomm.hfcl.sharedpreference.MySharedPreference;
import com.netcomm.hfcl.sharedpreference.PreferenceModel;


import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.netcomm.hfcl.MainActivity.toolbar_llayout;
import static com.netcomm.hfcl.constants.FragmentsTags.LOCATION_SETTINGS_REQUEST;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPunchinOutNew extends Fragment implements ResponseListener, View.OnClickListener {
    private View viewMain;
    private MainActivity mainActivity;
    private ProjectWebRequest request;
    /*private RelativeLayout rl_punchin;
    private RelativeLayout rl_punchout;*/
    private ImageView iv_day_off;
    private ImageView iv_mark;
    private TextView msg;
  //  private Location mLastLocation = null;
   // private String usersAddress = null;
    private String PunchType = null;
    private MySharedPreference pref;
    private String isPunchInVisible = null;
    private String isPunchOutVisible = null;

    private ProgressDialog progressDoalog;

    LocationRequest mLocationRequest;
    FusedLocationProviderClient mFusedLocationClient;
    private String FullAddress = "";
    private double Latitude = 0, Longitude = 0;
    Location location;
    Location mLastLocation;

    public FragmentPunchinOutNew() {
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
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
        mainActivity = null;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewMain = inflater.inflate(R.layout.fragment_fragment_punchin_out, container, false);

        pref = MySharedPreference.getInstance(mainActivity);
        initView();
        return viewMain;
    }

    void initView() {
        /*rl_punchin = (RelativeLayout) viewMain.findViewById(R.id.rl_punchin);
        rl_punchout = (RelativeLayout) viewMain.findViewById(R.id.rl_punchout);*/
        iv_mark = (ImageView) viewMain.findViewById(R.id.iv_mark);
        iv_day_off = (ImageView) viewMain.findViewById(R.id.iv_day_off);
        msg = (TextView) viewMain.findViewById(R.id.msg);
        /*rl_punchin.setOnClickListener(this);
        rl_punchout.setOnClickListener(this);*/


        iv_mark.setOnClickListener(this);
        iv_day_off.setOnClickListener(this);
        clearAddressAndMsg();
        hitApiForIsAbleToPunchOut();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mainActivity);
        LocationManager manager = (LocationManager) mainActivity.getSystemService(Context.LOCATION_SERVICE);

        if (manager != null && !manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            createLocationRequest();
        } else {
            if (mLocationRequest == null) {
                mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(120000); //two minute interval
                mLocationRequest.setFastestInterval(120000); //two minute interval
                mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            }

            requestLocationUpdates();
        }

      /*  if (NetworkUtils.isConnected(mainActivity))
            checkAppPermission();
        else {
            AppAlertDialog.showDialogFinishWithActivity(mainActivity, "Internet", "You are not Connected to internet");
        }*/
    }

   /* void checkAppPermission() {
        if (MyCheckPermission.checkAppPermission(mainActivity, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            if (MyCheckPermission.checkAppPermission(mainActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                getLocation();
            } else {
                ActivityCompat.requestPermissions(mainActivity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1999);
            }
        } else {
            ActivityCompat.requestPermissions(mainActivity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1999);
        }
    }*/

   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1999:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                       getLocation();
                    } else {
                        Toast.makeText(mainActivity, "You have to accept these permission ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mainActivity, "You have to accept these permission ", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }*/

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LocationUtils.LocationTag) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    //LocationUtils.getInstance(mainActivity, this).startProgressNow();
                    getLocation();
                    break;
                case Activity.RESULT_CANCELED:
                    if (progressDoalog.isShowing()) {
                        progressDoalog.dismiss();
                    }
                    AppAlertDialog.showDialogFinishWithActivity(mainActivity, "GPS", "Enabling GPS is mandatory");
                    break;
            }
        }
    }
*/
  /*  void getLocation() {
        if (checkGooglePlayServiceAvailability(mainActivity)) {
            buildProgress();
            LocationUtils.getInstance(mainActivity, this);
        }
    }*/

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); //two minute interval
        mLocationRequest.setFastestInterval(120000); //two minute interval
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        checkResolutionAndProceed();
    }

   /* public boolean checkGooglePlayServiceAvailability(Context context) {
        int statusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if ((statusCode == ConnectionResult.SUCCESS)) {
            return true;
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(statusCode, mainActivity, 10, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Toast.makeText(mainActivity, "You have to update google play service account", Toast.LENGTH_LONG).show();
                    mainActivity.finish();
                }
            });
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            return false;
        }
    }*/


  /*  @Override
    public void OnLocationChange(Location location) {
        if (progressDoalog.isShowing()) {
            progressDoalog.dismiss();
        }
        mLastLocation = location;
    }

    @Override
    public void onErrors(String msg) {

        if(progressDoalog!=null) {
            if (progressDoalog.isShowing()) {
                progressDoalog.dismiss();
            }
        }
        AppAlertDialog.showDialogSelfFinish(mainActivity, "Error", msg);
    }*/

   /* @Override
    public void onStop() {
        LocationUtils.getInstance(mainActivity, this).onStop();
        super.onStop();
    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_mark:
                getLocation();
                PunchType = "PunchIn";
                hitApi();
                break;
            case R.id.iv_day_off:
                getLocation();
                PunchType = "PunchOut";
                hitApi();
                break;
        }
        //mLastLocation = mainActivity.getUpdatedLocation();
       /* if (mLastLocation != null) {
            if (NetworkUtils.isConnected(getActivity())) {
                GeocodingLocation.getAddressFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), getActivity(), new GeocoderHandler());
            } else {
                AppAlertDialog.showDialogFinishWithActivity(getActivity(), "Internet", "You are not Connected to internet");
            }
        } else
            Toast.makeText(getActivity(), "Unable To get Location", Toast.LENGTH_SHORT).show();*/
    }

   /* private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            usersAddress = locationAddress;
//            Toast.makeText(getActivity(),usersAddress,Toast.LENGTH_LONG).show();
            Log.e("ADdRESSSSSS", usersAddress);

            if (usersAddress != null){
                hitApi();
            }
            else {
                Toast.makeText(getActivity(), "Unable To get Location", Toast.LENGTH_SHORT).show();
            }

        }
    }*/

    void clearAddressAndMsg() {
        FullAddress = "";
        msg.setText("");
        msg.setVisibility(View.GONE);

    }

    @Override
    public void onSuccess(JSONObject obj, int Tag) {
        clearRef();
        if (Tag == UrlConstants.PUNCHIN_OUT_TAG) {
            if (obj.optString("Status").equals("true")) {
                /*rl_punchin.setVisibility(View.GONE);
                rl_punchout.setVisibility(View.VISIBLE);*/
                iv_mark.setVisibility(View.GONE);
                iv_day_off.setVisibility(View.VISIBLE);

            }
            msg.setText(obj.optString("Message"));
            msg.setVisibility(View.VISIBLE);

            //Snackbar.make(getActivity().findViewById(android.R.id.content), obj.optString("Message"), Snackbar.LENGTH_LONG).show();
//            LocationUtils.getInstance(activity, this).onStop();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //mainActivity.onBackPressed();
                    if (mainActivity != null){
                        getFragmentManager().popBackStack();
                        toolbar_llayout.setVisibility(View.GONE);
                    }
                }
            }, 3000);

        }
        else if (Tag == UrlConstants.GET_PUNCH_OUT_STATUS_TAG) {
            if (obj.optString("Status").equals("true")) {
                isPunchInVisible = obj.optString("IsInButtonVisable");
                isPunchOutVisible = obj.optString("IsOutButtonVisable");

                if (isPunchInVisible.equals("true")) {
                    iv_mark.setVisibility(View.VISIBLE);
                } else {
                    iv_mark.setVisibility(View.GONE);
                }
                if (isPunchOutVisible.equals("true")) {
                    iv_day_off.setVisibility(View.VISIBLE);
                } else {
                    iv_day_off.setVisibility(View.GONE);
                }

                if (isPunchInVisible.equals("false") && isPunchOutVisible.equals("false")){
                    msg.setText("Punch In and Punch Out for the day is already marked");
                    msg.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mainActivity.onBackPressed();
                        }
                    }, 3000);
                }
            }

        }
    }

    @Override
    public void onFailure(VolleyError error, int Tag) {
        clearRef();

    }


    void clearRef() {
        if (request != null) {
            request = null;
        }
    }

    void hitApi() {

        if (FullAddress.equalsIgnoreCase("")) {
            FullAddress = "Unable To get Location";
        }

        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put(PreferenceModel.TokenKey, PreferenceModel.TokenValues);
            object.put("Uid", pref.getsharedPreferenceData().getUserId());
            object.put("Type", PunchType);
            object.put("Latitude", Latitude);
            object.put("Longitude", Longitude);
            object.put("Address", FullAddress);

            request = new ProjectWebRequest(mainActivity, object, UrlConstants.PUNCHIN_OUT, this, UrlConstants.PUNCHIN_OUT_TAG);
            request.execute();
        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }


    void hitApiForIsAbleToPunchOut() {
        JSONObject object1 = null;
        try {
            object1 = new JSONObject();
            object1.put(PreferenceModel.TokenKey, PreferenceModel.TokenValues);
            object1.put("EmpCode", pref.getsharedPreferenceData().getEmpCode());

            request = new ProjectWebRequest(mainActivity, object1, UrlConstants.GET_PUNCH_OUT_STATUS, this, UrlConstants.GET_PUNCH_OUT_STATUS_TAG);
            request.execute();

        } catch (Exception e) {
            clearRef();
            e.printStackTrace();
        }
    }

   /* void buildProgress() {
        progressDoalog = new ProgressDialog(mainActivity);
        progressDoalog.setMessage("Detecting Location....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.setCancelable(false);
        progressDoalog.show();
    }*/



    private void requestLocationUpdates() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
        getLastLocation();
    }

    private void checkResolutionAndProceed() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(mainActivity).checkLocationSettings(builder.build());
        result.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                requestLocationUpdates();
            }
        });
        result.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(mainActivity, LOCATION_SETTINGS_REQUEST);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            }
        });
    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(mainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(mainActivity)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(mainActivity,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(mainActivity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {

        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(mainActivity, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();
                        }
                    }
                });
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);

                if (location != null) {
                    if (location.getLatitude() != 0 && location.getLongitude() != 0) {
                        mLastLocation = location;
                        //Place current location marker
                        //LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());


                        String Show_lat_long = "Latitude: " + String.valueOf(location.getLatitude()) + "\nLongitude: " + String.valueOf(location.getLongitude());

                        Log.d("PunchInLocation", Show_lat_long);
                    }

                    Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                }
            }
        }
    };

    @SuppressLint("MissingPermission")
    private void getLocation() {

        location = mLastLocation;

        if (location != null && location.getLatitude() != 0 && location.getLongitude() != 0) {
            convertToLocation(location.getLatitude(), location.getLongitude());
            Latitude = location.getLatitude();
            Longitude = location.getLongitude();
            // savePunchToDatabase(encoded_image, location.getLatitude(), location.getLongitude(), punch_flag);
        } else {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(mainActivity, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    mLastLocation = location;
                }
            });
            final Handler handler = new Handler();
            final int[] count = {0};
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    location = mLastLocation;
                    if (location != null && location.getLatitude() != 0 && location.getLongitude() != 0) {
                        convertToLocation(location.getLatitude(), location.getLongitude());
                        Latitude = location.getLatitude();
                        Longitude = location.getLongitude();
                        //savePunchToDatabase(encoded_image, location.getLatitude(), location.getLongitude(), punch_flag);
                    } else {
                        if (count[0]++ < 5) {
                            handler.postDelayed(this, 10000);
                        } else {
                            Toast.makeText(mainActivity, "Error Retriving Location: Try again after some time", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            };

            handler.post(runnable);
        }
    }

    public void convertToLocation(double Latitude, double Longitude) {
        Geocoder geocoder = new Geocoder(mainActivity, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(Latitude, Longitude, 1);
            String address = addresses.get(0).getSubLocality();
            String cityName = addresses.get(0).getLocality();
            String stateName = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            FullAddress = address + " " + cityName + " " + stateName + " " + country;
            // Toast.makeText(activity, "" + FullAddress, Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
