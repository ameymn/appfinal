package com.example.jeeevandan;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TripDetailsAdapter adapter;
    private TableLayout tableLayout;

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 123;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private boolean isLocationTrackingStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        tableLayout = findViewById(R.id.tableLayout);

//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Bundle extras = getIntent().getExtras();
        String driverid = extras.getString("id");
        Log.d(TAG,"id  "+extras.getString("id"));

        ApiService apiServicedriver = RetrofitClient.getClient().create(ApiService.class);
        Call<Driver> call = apiServicedriver.driver(driverid);
        call.enqueue(new Callback<Driver>() {
            @Override
            public void onResponse(Call<Driver> call, Response<Driver> response) {
                Log.d(TAG, "Response code driver: " + response.body());
                List<Driver.TripDetails> tripDetailsList = response.body().getList();
                if (tripDetailsList != null && !tripDetailsList.isEmpty()) {
                    for (Driver.TripDetails tripDetails : tripDetailsList) {
                        TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.row_layout, null);

                        TextView tvTransplantId = row.findViewById(R.id.textViewTransplantId);
                        TextView tvDriverName = row.findViewById(R.id.textViewDriverName);
                        TextView tvDonorLatitude = row.findViewById(R.id.textViewDonorLatitude);
                        TextView tvDonorLongitude = row.findViewById(R.id.textViewDonorLongitude);
                        TextView tvReceiverLatitude = row.findViewById(R.id.textViewReceiverLatitude);
                        TextView tvReceiverLongitude = row.findViewById(R.id.textViewReceiverLongitude);

                        tvTransplantId.setText(String.valueOf(tripDetails.getT_id()));
                        tvDriverName.setText(tripDetails.getDriver());
                        tvDonorLatitude.setText(String.valueOf(tripDetails.getD_lat()));
                        tvDonorLongitude.setText(String.valueOf(tripDetails.getD_lngt()));
                        tvReceiverLatitude.setText(String.valueOf(tripDetails.getR_lat()));
                        tvReceiverLongitude.setText(String.valueOf(tripDetails.getR_lngt()));

                        tableLayout.addView(row);
                    }
                } else {
                    Log.d(TAG, "No trip details found");
                }
            }

            @Override
            public void onFailure(Call<Driver> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
            }
        });

        // Request location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            // Permission already granted, start location tracking
            startLocationTracking();
        }

        Button startButton = findViewById(R.id.buttonStart);
        Button stopButton = findViewById(R.id.buttonStop);

        startButton.setOnClickListener(v -> {
            startLocationTracking();
        });

        stopButton.setOnClickListener(v -> {
            stopLocationTracking();
        });
    }

    private void startLocationTracking() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    for (Location location : locationResult.getLocations()) {
                        Double latitude = location.getLatitude();
                        Double longitude = location.getLongitude();
                        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
                        Call<com.example.jeeevandan.Location> call = apiService.location(latitude, longitude);
                        call.enqueue(new Callback<com.example.jeeevandan.Location>() {
                            @Override
                            public void onResponse(Call<com.example.jeeevandan.Location> call, Response<com.example.jeeevandan.Location> response){
                                Log.d(TAG, "Response code: " + response.code());

                            }

                            @Override
                            public void onFailure(Call<com.example.jeeevandan.Location> call, Throwable t) {
                                Toast.makeText(com.example.jeeevandan.NewActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                            }


                        });


                            // Handle the location updates here
                        Log.d(TAG, "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
                    }
                }
            }
        };

        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000)
                .setFastestInterval(2000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
            isLocationTrackingStarted = true;
            Toast.makeText(this, "Location tracking started", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopLocationTracking() {
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
            isLocationTrackingStarted = false;
            Toast.makeText(this, "Location tracking stopped", Toast.LENGTH_SHORT).show();
        }
    }
}