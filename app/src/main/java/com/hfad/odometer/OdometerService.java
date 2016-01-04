package com.hfad.odometer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class OdometerService extends Service {

    private final IBinder binder = new OdometerBinder();
    private static double distanceInMeters;
    private static Location lastLocation;
    private LocationListener listener;
    private LocationManager locManager;

    public class OdometerBinder extends Binder {
        OdometerService getOdometer() {
            return OdometerService.this;
        }
    }

    public OdometerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // The onBind() method returns an IBinder. This is an interface the Binder class implements
        return binder;
    }

    @Override
    public void onCreate() {
        Toast.makeText(getApplicationContext(),
                "onCreateService",
                Toast.LENGTH_LONG).show();
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Toast.makeText(getApplicationContext(),
                        "onLocationChanged",
                        Toast.LENGTH_LONG).show();
                if (lastLocation == null) {
                    lastLocation = location;
                }
                distanceInMeters += location.distanceTo(lastLocation);
                lastLocation = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Toast.makeText(getApplicationContext(),
                        "onStatusChanged",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProviderEnabled(String provider) {
                Toast.makeText(getApplicationContext(),
                        "onProviderEnabled",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(getApplicationContext(),
                        "onProviderDisabled",
                        Toast.LENGTH_LONG).show();
            }
        };
        locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locManager.requestLocationUpdates(locManager.GPS_PROVIDER, 1000, 1, listener);
    }

    @Override
    public void onDestroy() {
        if (locManager != null && listener != null) {
            locManager.removeUpdates(listener);
            locManager = null;
            listener = null;
        }
    }

    public double getMiles() {
        return distanceInMeters / 1609.344;
    }

    public double getDistanceInMeters() {
        return distanceInMeters;
    }
}
