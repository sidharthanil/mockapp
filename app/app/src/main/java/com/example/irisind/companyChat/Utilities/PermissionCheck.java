package com.example.irisind.companyChat.Utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by sid on 28/12/16.
 */

public class PermissionCheck {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 1245;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1145;

    public static boolean isNetOn(Context context) {
        return !(((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() == null || !((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo().isConnected());
    }



    public static void requestLocationPermission(final Activity activity){
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);

    }


    public static boolean islocationAccessAvailable(final Activity activity)
    {

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return false;
        }
        return true;
    }

    public static boolean isStorageAccessAvailable(final Activity activity){
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            return false;
        }
        return true;

    }

    public static void requestStoragePermission(final Activity activity){
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

    }


}



/*

 @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermissionCheck.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                for (int i = 0; i < permissions.length; i++) {
                    int grantResult = grantResults[i];

                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }

                break;
            }

            default:
                break;

            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }

*/
