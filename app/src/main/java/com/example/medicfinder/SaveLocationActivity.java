package com.example.medicfinder;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

public class SaveLocationActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;
    private TextView locationTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_location);

        locationTextView = findViewById(R.id.locationTextView);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Get current location and save it
        getCurrentLocationAndSave();
    }


    private void getCurrentLocationAndSave() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {

                        saveLocation(location.getLatitude(), location.getLongitude());

                        // nak display saved location
                        displayLocation(location.getLatitude(), location.getLongitude());
                    } else {
                        Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveLocation(double latitude, double longitude) {

        SharedPreferences sharedPreferences = getSharedPreferences("UserLocation", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("latitude", (float) latitude);
        editor.putFloat("longitude", (float) longitude);
        editor.apply();
    }

    private void displayLocation(double latitude, double longitude) {
        String locationText = "Latitude: " + latitude + "\nLongitude: " + longitude;
        locationTextView.setText(locationText);
    }
}
