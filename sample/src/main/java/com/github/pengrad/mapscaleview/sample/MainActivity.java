package com.github.pengrad.mapscaleview.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.pengrad.mapscaleview.MapScaleView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener {

    private GoogleMap map;
    private MapScaleView scaleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        scaleView = (MapScaleView) findViewById(R.id.scaleView);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        googleMap.setOnCameraMoveListener(this);
        googleMap.setOnCameraIdleListener(this);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(21, 105.8), 10));
    }

    @Override
    public void onCameraMove() {
        scaleView.update(map.getProjection(), map.getCameraPosition());
    }

    @Override
    public void onCameraIdle() {
        scaleView.update(map.getProjection(), map.getCameraPosition());
    }
}
