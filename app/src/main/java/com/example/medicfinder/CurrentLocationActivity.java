package com.example.medicfinder;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.medicfinder.databinding.ActivityCurrentLocationBinding;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class CurrentLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private ActivityCurrentLocationBinding binding;
    private LatLng currentLocation;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCurrentLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        if (checkLocationPermission()) {
            initMap();
        } else {
            requestLocationPermission();
        }

        Button showCurrentLocationButton = findViewById(R.id.showCurrentLocationButton);
        showCurrentLocationButton.setOnClickListener(v -> {

            if (checkLocationPermission()) {
                getCurrentLocation();
            } else {
                Toast.makeText(CurrentLocationActivity.this, "Location permission required", Toast.LENGTH_SHORT).show();
            }
        });


        Button saveLocationButton = findViewById(R.id.saveLocationButton);
        saveLocationButton.setOnClickListener(v -> {

            Intent intent = new Intent(CurrentLocationActivity.this, SaveLocationActivity.class);
            startActivity(intent);
        });

    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Default location if GPS is off Uitm Machang
        currentLocation = new LatLng(5.75710436662611, 102.27347947511261);


        mMap.addMarker(new MarkerOptions()
                .title("UiTM Kampus Machang")
                .position(currentLocation)
                .snippet("My University"));


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 8));


        if (checkLocationPermission()) {
            mMap.setMyLocationEnabled(true);
        }
    }

    private void getCurrentLocation() {
        if (checkLocationPermission()) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            currentLocation = new LatLng(location.getLatitude(), location.getLongitude());


                            String locationName = getLocationName(currentLocation);


                            mMap.addMarker(new MarkerOptions()
                                    .position(currentLocation)
                                    .title("Your Current Location")
                                    .snippet("Here"));


                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 8));
                        } else {
                            Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initMap();
            } else {
                Toast.makeText(this, "Permission denied. Enable location to use this feature.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getLocationName(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;
        String locationName = "Unknown Location";
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                locationName = address.getAddressLine(0);  // Get the full address
            }
        } catch (IOException e) {
            Log.e("Geocoder", "Unable to get location name", e);
        }
        return locationName;
    }
}