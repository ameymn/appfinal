package com.example.jeeevandan;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
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
    private TableLayout tableLayout;
    private TextView textViewDriverName;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 123;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Integer Transplant_Id;
    private Double r_lat,r_lngt;
    private boolean isLocationTrackingStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        tableLayout = findViewById(R.id.tableLayout);
        textViewDriverName = findViewById(R.id.textViewDriverName); // Find the TextView
        Bundle extras = getIntent().getExtras();
        String driverid = extras.getString("id");
        Log.d(TAG, "id  " + extras.getString("id"));

        ApiService apiServicedriver = RetrofitClient.getClient().create(ApiService.class);
        Call<Driver> call = apiServicedriver.driver(driverid);
        call.enqueue(new Callback<Driver>() {
            @Override
            public void onResponse(Call<Driver> call, Response<Driver> response) {
                Log.d(TAG, "Response code driver: " + response.body());
                List<Driver.TripDetails> tripDetailsList = response.body().getList();
                if (tripDetailsList != null && !tripDetailsList.isEmpty()) {
                    String driverName = tripDetailsList.get(0).getDriver(); // Get the driver's name from the first TripDetails object
                    textViewDriverName.setText("Welcome "+ driverName+" \uD83D\uDC4B");
                    for (Driver.TripDetails tripDetails : tripDetailsList) {
                        TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.row_layout, null);
                        TextView tvTransplantId = row.findViewById(R.id.textViewTransplantId);
                        TextView tvDriverName = row.findViewById(R.id.textViewDriverName);
                        TextView tvtextViewStatus = row.findViewById(R.id.textViewStatus);
                        Button startButton = row.findViewById(R.id.buttonStart);
                        Button stopButton = row.findViewById(R.id.buttonStop);
                        Button navigateButton=row.findViewById(R.id.buttonNavigate);
                        Transplant_Id=tripDetails.getT_id();
                        tvTransplantId.setText(String.valueOf(tripDetails.getT_id()));
                        tvDriverName.setText(tripDetails.getDriver());



                        String Trans_status = "";
                        if (tripDetails.isTrans_end() == false) {
                            Trans_status = "Ongoing";
                            r_lat=tripDetails.getR_lat();
                            r_lngt=tripDetails.getR_lngt();
                        } else {
                            Trans_status = "Completed";
                        }
                        Log.d(TAG, "Rlat"+ r_lat+"Rlngt"+r_lngt);

                        tvtextViewStatus.setText(Trans_status);
                        // Set button visibility based on Trans_status
                        if (Trans_status.equals("Completed")) {
                            startButton.setVisibility(View.GONE);
                            stopButton.setVisibility(View.GONE);
                            navigateButton.setVisibility(View.GONE);
                        } else {
                            startButton.setVisibility(View.VISIBLE);
                            stopButton.setVisibility(View.VISIBLE);
                            navigateButton.setVisibility(View.VISIBLE);

                            Log.d(TAG, "T_ID" + Transplant_Id);

                            startButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startLocationTracking(Transplant_Id);
                                }
                            });

                            stopButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    stopLocationTracking();
                                }
                            });

                            navigateButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Check if r_lat and r_lngt are not null
                                    Log.d(TAG, "OOttd" +r_lat+"OOdddd"+r_lngt);
                                    if (r_lat != null && r_lngt != null) {
                                        // Create a URI for the Google Maps navigation
                                        Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("google.navigation:q="+r_lat+","+r_lngt));
                                        intent.setPackage("com.google.android.apps.maps");
                                        Log.d(TAG, "google.navigation:q="+r_lat+","+r_lngt);
                                        startActivity(intent);
//
//                                        if (intent.resolveActivity(getPackageManager()) != null) {
//                                            Log.d(TAG, "Intent resolved successfully");
//                                            startActivity(intent);
//                                        } else {
//                                            Log.d(TAG, "No activity found to handle intent");
//                                            Toast.makeText(NewActivity.this, "No navigation app installed", Toast.LENGTH_SHORT).show();
//                                        }
                                    } else {
                                        Toast.makeText(NewActivity.this, "Destination not available", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
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
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
//        } else {
//            // Permission already granted, start location tracking
//            startLocationTracking(Transplant_Id);
//        }
    }

    private void startLocationTracking(Integer id) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Log.e(TAG, "Star_id " + id);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.e(TAG, "Transss_id " + id);
                if (locationResult != null) {
                    for (Location location : locationResult.getLocations()) {
                        Double latitude = location.getLatitude();
                        Double longitude = location.getLongitude();
                        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
                        Call<com.example.jeeevandan.Location> call = apiService.location(id,latitude,longitude);
                        call.enqueue(new Callback<com.example.jeeevandan.Location>() {
                            @Override
                            public void onResponse(Call<com.example.jeeevandan.Location> call, Response<com.example.jeeevandan.Location> response) {
                                Log.d(TAG, "response location" + response.code());
                                Log.d(TAG, "response l" + response.body());


                            }
                            @Override
                            public void onFailure(Call<com.example.jeeevandan.Location> call, Throwable t) {
                                Toast.makeText(com.example.jeeevandan.NewActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                        // Handle the location updates here
                        Log.d(TAG, "trasn_idd"+ id + "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
                    }
                }
            }
        };

        LocationRequest locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(15000).setFastestInterval(2000);

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