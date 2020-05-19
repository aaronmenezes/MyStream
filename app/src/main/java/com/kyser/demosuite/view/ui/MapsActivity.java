package com.kyser.demosuite.view.ui;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kyser.demosuite.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private LocationManager mLocationManager;
    final private int ACCESS_COARSE_LOCATION_REQUEST_CODE = 54754;
    final private int ACCESS_FINE_LOCATION_REQUEST_CODE = 54854;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        /*
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */
        try {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5, this);
        } catch (SecurityException sr) {
            Log.v("==exception==", " == "+sr.getLocalizedMessage());
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_COARSE_LOCATION_REQUEST_CODE);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_REQUEST_CODE);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng cur = new LatLng(location.getLatitude(), location.getLongitude());
        System.out.println(location.getLatitude()+" === "+location.getLongitude());
        Log.v("==onLocationChanged==","======================");
        Log.v("==onLocationChanged=="," lat "+location.getLatitude());
        Log.v("==onLocationChanged=="," long "+location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(cur).title("Party spot"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cur));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.v("==onStatusChanged==","======================");
    }

    @Override
    public void onProviderEnabled(String provider) {
        try {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5, this);
        } catch (SecurityException sr) {
            Log.v("==exception==", " == "+sr.getLocalizedMessage());
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_COARSE_LOCATION_REQUEST_CODE);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_REQUEST_CODE);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.v("==onProviderDisabled==","======================");
    }
}
