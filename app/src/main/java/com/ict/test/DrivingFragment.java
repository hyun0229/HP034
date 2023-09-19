package com.ict.test;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class DrivingFragment extends Fragment implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private ImageButton btn_back;
    private TextView speed;

    private Location previousLocation;
    private long previousTimeMillis;

    private Marker currentLocationMarker; // Store the current location marker

    private SharedPreferences sharedPreferences;
    int drive_Vib;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_driving, container, false);
        btn_back = v.findViewById(R.id.btn_back_driving);
        btn_back.setOnClickListener(v1 -> {
            MainActivity mainActivity = (MainActivity) requireActivity();
            mainActivity.commit_MenuFragment();
            closeFragment();
        });
        speed = v.findViewById(R.id.testspeed);
        sharedPreferences = requireActivity().getSharedPreferences("user_settings", Context.MODE_PRIVATE);

        drive_Vib = sharedPreferences.getInt("drive_Vib", 100);

        initMap();

        return v;
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        setupMap();
        startLocationUpdates();
    }

    private void setupMap() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        previousLocation = null;
        previousTimeMillis = 0;

        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    private void startLocationUpdates() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    updateCurrentLocationMarker(currentLatLng);
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateCurrentLocationMarker(LatLng latLng) {
        if (currentLocationMarker == null) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title("현재 위치");
            currentLocationMarker = googleMap.addMarker(markerOptions);
        } else {
            currentLocationMarker.setPosition(latLng);
        }
        if (previousLocation != null) {
            float distance = latLngToLocation(latLng).distanceTo(previousLocation);
            long currentTimeMillis = System.currentTimeMillis();
            long timeInterval = currentTimeMillis - previousTimeMillis;

            if (timeInterval > 0) {
                float speedMps = distance / (timeInterval / 1000f);
                float speedKph = speedMps * 3.6f; // Convert m/s to km/h
                speed.setText("현재 속도: "+ speedKph + " km/h");
                VibAlarm(speedKph);
                Log.d("Speed", "Speed: " + speedKph + " km/h");
            }
        }
        previousLocation = latLngToLocation(latLng);
        previousTimeMillis = System.currentTimeMillis();

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (fusedLocationProviderClient != null && locationCallback != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (fusedLocationProviderClient != null && locationCallback != null) {
            startLocationUpdates();
        }
    }
    private Location latLngToLocation(LatLng latLng) {
        Location location = new Location("custom_provider");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        return location;
    }

    private void closeFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(this);
        fragmentTransaction.commit();
    }

    private  void VibAlarm(float speed){
        Vibrator vibrator = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);

        if (vibrator != null && vibrator.hasVibrator()) {
            long[] pattern = {0, 1000, 1000-drive_Vib};
            vibrator.vibrate(pattern, -1);
        }
        else {
            vibrator.cancel();
        }
    }

}

