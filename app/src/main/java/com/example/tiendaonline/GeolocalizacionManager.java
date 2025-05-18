package com.example.tiendaonline;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

public class GeolocalizacionManager {
    private static GeolocalizacionManager instance;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private OnLocationUpdateListener listener;

    public interface OnLocationUpdateListener {
        void onLocationUpdate(Location location);
    }

    private GeolocalizacionManager(Context context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        setupLocationCallback();
    }

    public static GeolocalizacionManager getInstance(Context context) {
        if (instance == null) {
            instance = new GeolocalizacionManager(context.getApplicationContext());
        }
        return instance;
    }

    private void setupLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (listener != null) {
                        listener.onLocationUpdate(location);
                    }
                }
            }
        };
    }

    public void startLocationUpdates(Context context, OnLocationUpdateListener listener) {
        this.listener = listener;
        
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) 
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setMinUpdateIntervalMillis(5000)
                .build();

        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    public void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
} 