package com.example.codexivestudio;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private boolean LocationPermissionGranted;
    public static final int PERMISSION_REQUEST_CODE = 404;
    public static final int GOOGLE_PLAY_REQUEST_CODE = 405;
    public static final String P_TAG = "PermissionTag";
    String [] PermissionList = {Manifest.permission.ACCESS_FINE_LOCATION};
    public GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        InitilizeGoogleMap();
        SupportMapFragment SupportMapFrag = SupportMapFragment.newInstance();

        getSupportFragmentManager().beginTransaction().add(R.id.GoogleMaps, SupportMapFrag).commit();
        SupportMapFrag.getMapAsync(this);


    }


    private void InitilizeGoogleMap() {
        if(IsServicesOk()){
            if(CheckLocationPermission()){
                Toast.makeText(this, "Ready to Map", Toast.LENGTH_SHORT).show();
            }else{
                requestLocationPermission();
            }
        }
    }

    private boolean CheckLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


    private Boolean IsServicesOk() {
        GoogleApiAvailability GoogleApi = GoogleApiAvailability.getInstance();

        int result = GoogleApi.isGooglePlayServicesAvailable(this);
        if(result == ConnectionResult.SUCCESS){
            return true;
        }else if(GoogleApi.isUserResolvableError(result)) {
            Dialog GooglePlayDialog = GoogleApi.getErrorDialog(this, result, GOOGLE_PLAY_REQUEST_CODE, null);
        }else{
            Toast.makeText(this, "Play Services are required!", Toast.LENGTH_SHORT);
        }
        return false;

    }
    private void requestLocationPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(PermissionList, PERMISSION_REQUEST_CODE);
            }
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            LocationPermissionGranted = true;
            Log.d(P_TAG, "Permission has been Granted");
        }else{
            Log.d(P_TAG, "Permission has not been Granted");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("Ali", "Showing Map");
    }
}
