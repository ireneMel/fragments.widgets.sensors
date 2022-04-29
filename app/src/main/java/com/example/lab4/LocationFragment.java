package com.example.lab4;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.io.IOException;
import java.util.List;

public class LocationFragment extends Fragment {

    private LocationManager locationManager;
    private LocationListener locationListener;
    double latitude;
    double longitude;

    public LocationFragment() {
        // Required empty public constructor
    }

    SharedPreferences mPref;
    SharedPreferences.Editor medit;
    @SuppressLint({"MissingPermission", "DefaultLocale"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_location, container, false);

        mPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        medit = mPref.edit();

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = location -> {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            updateWidget(String.format("%.5f",longitude), String.format("%.5f",latitude));
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000,
                100,
                locationListener);

        return rootView;
    }

    private void updateWidget(String longitude, String latitude) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());
        RemoteViews remoteViews = new RemoteViews(getContext().getPackageName(), R.layout.location_widget);
        ComponentName thisWidget = new ComponentName(getContext(), LocationWidget.class);

        remoteViews.setTextViewText(R.id.appwidget_longitude, getResources().getString(R.string.longitude_1_s, longitude));
        remoteViews.setTextViewText(R.id.appwidget_latitude, getResources().getString(R.string.longitude_1_s, latitude));

        Intent updateIntent = new Intent(getContext(), MainActivity.class);
        PendingIntent updatePI = PendingIntent.getActivity(getContext(), 0, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setPendingIntentTemplate(R.id.location_widget, updatePI);
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
    }

}