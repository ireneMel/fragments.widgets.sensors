package com.example.lab4;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

/**
 * by sensors finds azimuth, pitch and poll
 * displays them
 */
public class CoordinatesFragment extends Fragment implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensorAccelerometer;
    private Sensor mSensorMagnetometer;

    private TextView x_axis;
    private TextView y_axis;
    private TextView z_axis;

    public CoordinatesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_coordinates, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        x_axis = rootView.findViewById(R.id.x_axis);
        y_axis = rootView.findViewById(R.id.y_axis);
        z_axis = rootView.findViewById(R.id.z_axis);

        final Toolbar toolbar = rootView.findViewById(R.id.toolbar_home);
        toolbar.setNavigationIcon(R.drawable.ic_back_24);
        toolbar.setOnClickListener(view -> getActivity().onBackPressed());

        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        String sensorError = "No sensor found";
        if (mSensorAccelerometer == null || mSensorMagnetometer == null) {
            x_axis.setText(sensorError);
            y_axis.setText(sensorError);
            z_axis.setText(sensorError);
        }

        return rootView;
    }

    private float[] mAccelerometerData = new float[3];
    private float[] mMagnetometerData = new float[3];

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int type = sensorEvent.sensor.getType();
        if (type == Sensor.TYPE_ACCELEROMETER) {
            mAccelerometerData = sensorEvent.values.clone();
        } else if (type == Sensor.TYPE_MAGNETIC_FIELD) {
            mMagnetometerData = sensorEvent.values.clone();
        }

        float[] rotationMatrix = new float[9];
        boolean rotationOK = SensorManager.getRotationMatrix(rotationMatrix,
                null, mAccelerometerData, mMagnetometerData);
        Log.v("LOCATIONMSG", String.valueOf(rotationMatrix[0]));

        float[] orientationValues = new float[3];
        if (rotationOK) {
            SensorManager.getOrientation(rotationMatrix, orientationValues);
        }

        float azimuth = orientationValues[0];
        float pitch = orientationValues[1];
        float roll = orientationValues[2];

        x_axis.setText(getResources().getString(R.string.x_axis, azimuth));
        y_axis.setText(getResources().getString(R.string.y_axis, pitch));
        z_axis.setText(getResources().getString(R.string.z_axis, roll));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mSensorAccelerometer != null)
            mSensorManager.registerListener(this, mSensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        if (mSensorMagnetometer != null)
            mSensorManager.registerListener(this, mSensorMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

}