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
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

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
    //Intent name hardcoded in Main Activity
    private static final String ACTION_RUN_GPS_CHECK = "com.example.auto_clock.action.RunGPSCheck"; //
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
            if (ACTION_RUN_GPS_CHECK.equals(action)) {
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

            if (checkMovedIn()) {
                System.out.println("Auto service clock in");
                bl.clockIn();
                ShowToastInIntentService("Auto Clocked In");

            } else if (checkMovedOut()) {
                System.out.println("Auto service clock out");
                bl.clockOut();
                ShowToastInIntentService("Auto Clocked Out");
            }
            //Pause for 60 seconds
            try {
                Thread.sleep(60000);
            } catch (InterruptedException ex) {
                android.util.Log.d("YourApplicationName", ex.toString());
            }
        }
    }

    private boolean checkMovedIn() {
        SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //If in, but was not in
        if ( checkIfIn()) {
            if (!sharedpreferences.getBoolean("wasIn", false)) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("wasIn", true);
                //updates the wasOutflag flag
                editor.commit();
                System.out.println("MyAutoService: checkMovedIn: returned:" + true);
                return true;
            }
        }
        System.out.println("MyAutoService: checkMovedIn: returned:" + false);
        return false;
    }

    private boolean checkMovedOut() {
        SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //If not in anymore, but was in
        if (!checkIfIn()) {
            if (sharedpreferences.getBoolean("wasIn", false)) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("wasIn", false);
                //updates the wasInflag flag
                editor.commit();
                System.out.println("MyAutoService: checkMovedOut: returned:" + true);
                return true;
            }
        }
        System.out.println("MyAutoService: checkMovedOut: returned:" + false);
        return false;
    }

    private Boolean checkIfIn() {
        SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String zone = "";
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if (isInZone(zone)) {
            editor.putBoolean("isIn", true);
            editor.commit();
            //System.out.println("MyAutoService: checkIfIn: returned:" + true);
            return true;
        }
        editor.putBoolean("isIn", false);
        editor.commit();
        //System.out.println("MyAutoService: checkIfIn: returned:" + false);
        return false;
    }

    private boolean isInZone(String zone) {
        SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String newLocationString = "";
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener LL = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                SharedPreferences sharedpreferences;
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("newLocationString", location.getLongitude() + " " + location.getLatitude());
                editor.commit();
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            @Override
            public void onProviderEnabled(String provider) {
            }
            //launches Permissions access
            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            String locationProvider = LocationManager.GPS_PROVIDER;

            lm.requestLocationUpdates(locationProvider, 60000, 0, LL);
            //newLocationString = sharedpreferences.getString("newLocationString", "");
            Location location = lm.getLastKnownLocation(locationProvider);
            if (location != null) {
                Double Lat = location.getLatitude();
                Double Lon = location.getLongitude();
                //System.out.println(lm.getLastKnownLocation());

                System.out.println("MyAutoService: isInZone: newLocation :"
                        + "lat:" + Lat
                        + " Lon:" + Lon);
                Double Lat_upLeft = 38.925732;
                Double Lat_bottomRight = 38.925430;
                Double Lon_upLeft = 0 - 94.538329;
                Double Lon_bottomRight = 0 - 94.537767;

                if (Lat < Lat_upLeft && Lat > Lat_bottomRight) {
                    if (Lon > Lon_upLeft && Lon < Lon_bottomRight) {
                        //System.out.println("MyAutoService: isInZone: returned:"+ true);
                        return true;
                    }
                }
            }
        }
        //System.out.println("MyAutoService: isInZone: returned:"+ false);
        return false;
    }

    public void ShowToastInIntentService(final String sText) {
        final Context MyContext = this;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast toast1 = Toast.makeText(MyContext, sText, Toast.LENGTH_LONG);
                toast1.show();
            }
        });
    };
}
