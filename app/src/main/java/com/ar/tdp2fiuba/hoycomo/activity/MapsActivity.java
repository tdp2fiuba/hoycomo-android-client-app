package com.ar.tdp2fiuba.hoycomo.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.ar.tdp2fiuba.hoycomo.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String ARG_LAT = "LATITUDE";
    private Double mLat;
    public static final String ARG_LNG = "LONGITUDE";
    private Double mLng;
    public static final String ARG_ADDRESS_NAME = "ADDRESS_NAME";
    private String mAddressName;
    public static final String ARG_MARKER_NAME = "MARKER_NAME";
    private String mMarkerName;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);

        mLat = getIntent().getDoubleExtra(ARG_LAT, -34.617614);
        mLng = getIntent().getDoubleExtra(ARG_LNG, -58.367931);
        mAddressName = getIntent().getStringExtra(ARG_ADDRESS_NAME);
        mMarkerName = getIntent().getStringExtra(ARG_MARKER_NAME);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(mLat, mLng);
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(mMarkerName).snippet(mAddressName));
        marker.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);
        }
    }

    public void closeMap(final View view) {
        finish();
    }
}
