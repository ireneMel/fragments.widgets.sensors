package com.example.lab4;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.lab4.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    //TODO rotation
    private ActivityMainBinding binding;

    static final String STATE_FRAGMENT_TEXT = "state_of_fragment_text";

    private EditText inputActivity;
    private String inputText;
    private boolean isTextDisplayed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        inputActivity = binding.inputFromActivity;

        getPermissionForLocation();

        if (savedInstanceState != null) {
            isTextDisplayed = savedInstanceState.getBoolean(STATE_FRAGMENT_TEXT);
            if (isTextDisplayed)
                binding.textButton.setText(R.string.close_text_fragment);
        }

        binding.textButton.setOnClickListener(view -> {
            if (!isTextDisplayed) {
                inputText = String.valueOf(inputActivity.getText());
                displayTextFragment();
            } else {
                closeTextFragment();
            }
        });

        binding.coordinatesButton.setOnClickListener(view -> {
            binding.coordinatesButton.setVisibility(View.GONE);
            binding.textButton.setVisibility(View.GONE);
            displayCoordinatesFragment();
        });

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(STATE_FRAGMENT_TEXT, isTextDisplayed);
    }

    public void displayCoordinatesFragment() {
        CoordinatesFragment coordinatesFragment = new CoordinatesFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_coord, coordinatesFragment)
                .addToBackStack(null).commit();
    }

    public void displayTextFragment() {
        TextFragment textFragment = TextFragment.newInstance(inputText);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_text, textFragment)
                .addToBackStack(null).commit();
        changeLayoutForText(false, R.string.close_text_fragment);
    }

    public void closeTextFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        TextFragment textFragment = (TextFragment) fragmentManager.findFragmentById(R.id.fragment_container_text);

        if (textFragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            inputText = textFragment.getInputText();
            inputActivity.setText(inputText);
            fragmentTransaction.remove(textFragment).commit();
        }

        changeLayoutForText(true, R.string.text_fragment);
    }

    private void changeLayoutForText(boolean bool, int buttonText) {
        inputActivity.setEnabled(bool);
        binding.coordinatesButton.setEnabled(bool);
        binding.textButton.setText(buttonText);
        isTextDisplayed = !bool;
    }

    @Override
    public void onBackPressed() {
        binding.coordinatesButton.setVisibility(View.VISIBLE);
        binding.textButton.setVisibility(View.VISIBLE);
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    private void getPermissionForLocation() {
        //request permission for location
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            Log.d("permissions", "permission granted");

            LocationFragment fr = new LocationFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_coord, fr)
                    .hide(fr)
                    .commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPermissionForLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}