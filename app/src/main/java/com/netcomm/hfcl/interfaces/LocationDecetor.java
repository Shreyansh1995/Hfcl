package com.netcomm.hfcl.interfaces;

import android.location.Location;

/**
 * Created by Netcomm on 12/27/2016.
 */

public interface LocationDecetor {
    void OnLocationChange(Location location);
    void onErrors(String msg);
}
