package com.example.lab4;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

public class LocationLoader extends AsyncTaskLoader<List<String>> {

    private String latitude;
    private String longitude;
    private LocationListener locationListener;

    public LocationLoader(@NonNull Context context, LocationListener locationListener) {
        super(context);
        this.locationListener = locationListener;
        forceLoad();
    }

    @Nullable
    @Override
    public List<String> loadInBackground() {
        List<String> res = new ArrayList<>();
        locationListener = location -> {
            res.clear();
            latitude = String.format("%.5f", location.getLatitude());
            longitude = String.format("%.5f", location.getLongitude());
            res.add(latitude);
            res.add(longitude);
            updateWidget(longitude, latitude);
            Log.v("LocationFrag", String.valueOf(latitude));
            Log.v("LocationFrag", String.valueOf(longitude));
        };
        return res;
    }

    private void updateWidget(String longitude, String latitude) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());
        RemoteViews remoteViews = new RemoteViews(getContext().getPackageName(), R.layout.location_widget);
        ComponentName thisWidget = new ComponentName(getContext(), LocationWidget.class);

        remoteViews.setTextViewText(R.id.appwidget_longitude, "longitude: " + longitude);
        remoteViews.setTextViewText(R.id.appwidget_latitude, "latitude: " + latitude);

        Intent updateIntent = new Intent(getContext(), MainActivity.class);
        PendingIntent updatePI = PendingIntent.getActivity(getContext(), 0, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setPendingIntentTemplate(R.id.location_widget, updatePI);
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
    }
}
