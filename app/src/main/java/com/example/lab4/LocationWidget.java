package com.example.lab4;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class LocationWidget extends AppWidgetProvider {

    private static final String mSharedPrefFile =
            "com.example.lab4";
    private static final String LONGITUDE_KEY = "longitude";
    private static final String LATITUDE_KEY = "latitude";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(mSharedPrefFile, 0);
        String latitude = sharedPreferences.getString(LATITUDE_KEY, "DEFAULT");
        String longitude = sharedPreferences.getString(LONGITUDE_KEY, "DEFAULT");

//        String longitude = LocationFragment.getSharedPref(LONGITUDE_KEY, "longitude");
//        String latitude = LocationFragment.getSharedPref(LATITUDE_KEY, "latitude");

//        Log.v(LATITUDE_KEY, longitude);
//        Log.v(LATITUDE_KEY, latitude);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.location_widget);
        views.setTextViewText(R.id.appwidget_longitude, context.getResources().getString(R.string.longitude_1_s, longitude));
        views.setTextViewText(R.id.appwidget_latitude, context.getResources().getString(R.string.latitude_1_s, latitude));

//        SharedPreferences.Editor pref = sharedPreferences.edit();
//        pref.putString(LONGITUDE_KEY, longitude);
//        pref.putString(LATITUDE_KEY, latitude);
//        pref.apply();

        Intent intent = new Intent(context, LocationWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] idArray = new int[]{appWidgetId};
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray);
//        context.sendBroadcast(intent);

        PendingIntent pendingUpdate = PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.location_widget, pendingUpdate);

//        ComponentName component = new ComponentName(context, WidgetTaskSchedular.class);
//        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i], R.id.listWidget);
//        appWidgetManager.updateAppWidget(component, remoteView);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

}