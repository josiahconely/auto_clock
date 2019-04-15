package com.example.auto_clock;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import static android.support.v4.app.ActivityCompat.requestPermissions;
import static com.example.auto_clock.BusinessLogic.MyPREFERENCES;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class MyAutoService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_RUN_GPS_CHECK = "com.example.auto_clock.action.RunGPSCheck";
    private BusinessLogic bl;


    public MyAutoService() {
        super("MyAutoService");
    }


    /**
     * Starts this service to perform action ACTION_RUN_GPS_CHECK with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

    public static void startActionRunGPSCheck(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyAutoService.class);
        intent.setAction(ACTION_RUN_GPS_CHECK);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (true) {//ACTION_RUN_GPS_CHECK.equals(action)) { //FIX!!!!!
                handleActionRunGPSCheck();
            }
        }
    }

    /**
     * Handle action RunGPSCheck in the provided background thread with the provided
     * parameters.
     */
    private void handleActionRunGPSCheck() {

        bl = new BusinessLogic(this);
        while (true) {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException ex) {
                android.util.Log.d("YourApplicationName", ex.toString());
            }

            checkIfIn();

            if (checkMovedIn()) {
                bl.clockIn();
            } else if (checkMovedOut()) {
                bl.clockOut();
            }
        }
    }

    private boolean checkMovedIn() {
        SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //If in, but was not in
        if (sharedpreferences.getBoolean("isIn", false)) {
            if (!sharedpreferences.getBoolean("wasIn", true)) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("wasIn", true);
                //updates the wasOutflag flag
                editor.commit();
                return true;
            }
        }
        return false;
    }

    private boolean checkMovedOut() {
        SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //If not in anymore, but was in
        if (!sharedpreferences.getBoolean("isIn", true)) {
            if (sharedpreferences.getBoolean("wasIn", true)) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("wasIn", false);
                //updates the wasInflag flag
                editor.commit();
                return true;
            }
        }
        return false;
    }

    private void checkIfIn() {
        SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String zone = "";
        if (isInZone(zone)) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean("isIn", true);
            editor.commit();
        } else {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean("isIn", false);
            editor.commit();
        }
    }

    private boolean isInZone(String zone) {
        SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String newLocationString = "";
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener LL = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                SharedPreferences sharedpreferences;
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("newLocationString", location.getLongitude() + " " + location.getLatitude() );
                editor.commit();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //startActivity(i);
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return false;
        }
        lm.requestLocationUpdates("gps", 58000, 20, LL);
        newLocationString = sharedpreferences.getString("newLocationString", "");
        return newLocationString == zone;

    }



}
