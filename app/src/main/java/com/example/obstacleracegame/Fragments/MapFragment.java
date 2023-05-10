package com.example.obstacleracegame.Fragments;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.obstacleracegame.R;
import com.example.obstacleracegame.SignalGenerator;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapFragment extends Fragment {
    private GoogleMap gMap;
    private final int FINE_PERMISSION_CODE = 1;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final float DEFAULT_ZOOM = 15f;
    public Location currentLocation;
    private double latitude;
    private double longitude;
    private FusedLocationProviderClient fusedLocationProviderClient;
    public boolean locationPermissionGranted = false;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);
        initMap();

        getLocationPermission();
        getDeviceLocation();

        return view;
    }

    public void getDeviceLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.getContext());

        try {
            if (locationPermissionGranted) {
                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: success");
                            currentLocation = (Location) task.getResult();
                            Log.d("LATITUDE", "" + currentLocation.getLatitude());
                            setLat(currentLocation.getLatitude());
                            setLon(currentLocation.getLongitude());
                            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            gMap.addMarker(new MarkerOptions()
                                    .position(latLng));
                            moveCamera(latLng, DEFAULT_ZOOM);
                            //        gMap.animateCamera(CameraUpdateFactory.zoomTo(0.0f));

                        } else {
                            Log.d(TAG, "onComplete: location is null");
                            SignalGenerator.getInstance().toast("Cant get location", Toast.LENGTH_SHORT);
                        }
                    }
                });
            }

        } catch (SecurityException e) {
            Log.d(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void setLat(double latitude) {
        this.latitude = latitude;
    }

    private void setLon(double longitude) {
        this.longitude = longitude;
    }

    public double getLat() {
        return latitude;
    }

    public double getLon() {
        return longitude;
    }

    private void moveCamera(LatLng latLng, float zoom) {
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    public void getLocation() {
        FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(view.getContext());
        LatLng currLocation = new LatLng(30.0, 150.0);
        gMap.addMarker(new MarkerOptions()
                .position(currLocation));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(currLocation));
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(googleMap -> {
            gMap = googleMap;
        });
    }

    private void getLocationPermission() {
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(view.getContext().getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(view.getContext().getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
                initMap();
            }
        } else {
            ActivityCompat.requestPermissions(this.getActivity(), permission, FINE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = false;
            } else {
                locationPermissionGranted = true;
                SignalGenerator.getInstance().toast("Location Permission is denied", Toast.LENGTH_SHORT);
            }
        }
    }
}
