package com.example.localizacaoapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_UPDATES=1;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnStart = findViewById(R.id.buttonStart);
        Button btnStop = findViewById(R.id.buttonStop);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocationUpdate();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLocationUpdate();
            }
        });
    }


    public void startLocationUpdate(){
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            long timeInterval = 5*2000;

            //revisar


            locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY).build();



            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    Location location = locationResult.getLastLocation();
                    atualizaLocationTextView(location);
                }
            };
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }else{
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_UPDATES);


        }
    }
    public void stopLocationUpdate(){
        if (fusedLocationProviderClient!=null)
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        atualizaLocationTextView(null);
    }

    public void atualizaLocationTextView (Location location) {
        TextView locationTextView=(TextView)
                findViewById(R.id.LocationTextView);
        String s="Dados da Última Localização:\n";
        if (location!=null) {
            s+="Latitude: "+ location.getLatitude()+"\n";
            s+="Longitude: "+ location.getLongitude()+"\n";
            s+="Altitude: "+ location.getAltitude()+"\n";
            s+="Rumo: (radianos)"+ location.getBearing()+"\n";
            s+="Velocidade (m/s): "+ location.getSpeed()+"\n";
            s+="Precisão: (m)"+ location.getAccuracy()+"\n";
        }
        locationTextView.setText(s);
    }
}