package com.example.lab4;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.Locale;

public class LocationFragment extends Fragment {

    //to save to shared pref, so widget could update itself
    private static final String mSharedPrefFile =
            "com.example.lab4";
    private static final String LONGITUDE_KEY = "longitude";
    private static final String LATITUDE_KEY = "latitude";
    private SharedPreferences mSharedPreferences;

    //location classes
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastLocation;

    public LocationFragment() {
        // Required empty public constructor
    }

    //location is requested in main activity
    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_location, container, false);
        mSharedPreferences = getActivity().getSharedPreferences(mSharedPrefFile, 0);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        Log.d("permissions", "on create view");

        //updates within 10 sec
        LocationRequest locationRequest = LocationRequest.create().setInterval(10 * 1000) //updates within 10 sec
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(1);

        mFusedLocationProviderClient.requestLocationUpdates(
                locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        mLastLocation = locationResult.getLastLocation();
                        getAndSave(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    }
                }, Looper.myLooper()
        );
        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void updateWidget(String longitude, String latitude) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());
        RemoteViews remoteViews = new RemoteViews(getContext().getPackageName(), R.layout.location_widget);
        ComponentName thisWidget = new ComponentName(getContext(), LocationWidget.class);

        remoteViews.setTextViewText(R.id.appwidget_longitude, getResources().getString(R.string.longitude_1_s, longitude));
        remoteViews.setTextViewText(R.id.appwidget_latitude, getResources().getString(R.string.latitude_1_s, latitude));

        Intent updateIntent = new Intent(getContext(), MainActivity.class);
        PendingIntent updatePI = PendingIntent.getActivity(getContext(), 0, updateIntent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setPendingIntentTemplate(R.id.location_widget, updatePI);
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getAndSave(double latitude, double longitude) {
        String lat = String.format(Locale.getDefault(), "%.5f", latitude);
        String lon = String.format(Locale.getDefault(), "%.5f", longitude);

        SharedPreferences.Editor pref = mSharedPreferences.edit();
        pref.putString(LONGITUDE_KEY, lon);
        pref.putString(LATITUDE_KEY, lat);
        pref.apply();
        updateWidget(lon, lat);
    }
}