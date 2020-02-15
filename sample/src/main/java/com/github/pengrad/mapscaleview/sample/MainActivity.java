package com.github.pengrad.mapscaleview.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.github.pengrad.mapscaleview.MapScaleView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraChangeListener {

    private GoogleMap map;
    private MapScaleView scaleView;
    private MapScaleView scaleViewMiles;
    private MapScaleView scaleViewRtl;
    private MapScaleView scaleViewBg;
    private MapScaleView scaleViewBgFixed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        scaleView = findViewById(R.id.scaleView);
        scaleViewMiles = findViewById(R.id.scaleViewMiles);
        scaleViewRtl = findViewById(R.id.scaleViewRtl);
        scaleViewBg = findViewById(R.id.scaleViewBg);
        scaleViewBgFixed = findViewById(R.id.scaleViewBgFixed);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        googleMap.setOnCameraMoveListener(this);
        googleMap.setOnCameraIdleListener(this);
        googleMap.setOnCameraChangeListener(this);

//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(21, 105.8), 10));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.07770360532252, -94.76820822805165), 12));
    }

    @Override
    public void onCameraMove() {
        update(map.getCameraPosition());
    }

    @Override
    public void onCameraIdle() {
        update(map.getCameraPosition());
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        Log.d("++++", "pos " + cameraPosition);
        update(cameraPosition);
    }

    private void update(CameraPosition cameraPosition) {
        scaleView.update(cameraPosition.zoom, cameraPosition.target.latitude);
        scaleViewMiles.update(cameraPosition.zoom, cameraPosition.target.latitude);
        scaleViewRtl.update(cameraPosition.zoom, cameraPosition.target.latitude);
        scaleViewBg.update(cameraPosition.zoom, cameraPosition.target.latitude);
        scaleViewBgFixed.update(cameraPosition.zoom, cameraPosition.target.latitude);
    }

    public void changeColor(View view) {
        Random random = new Random();
        int color = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
        scaleView.setColor(color);
    }

    public void changeTextSize(View view) {
        scaleView.setTextSize(new Random().nextInt(20) + 18);
    }

    public void changeStrokeWidth(View view) {
        scaleView.setStrokeWidth(new Random().nextInt(7) + 1);
    }

    private boolean isMiles = false;

    public void changeMiles(View view) {
        isMiles = !isMiles;
        scaleView.milesOnly();
    }

    private boolean isMeters = false;

    public void changeMeters(View view) {
        isMeters = !isMeters;
        if (isMeters) scaleView.metersOnly();
        else scaleView.metersAndMiles();
    }

    public void changeSize(View view) {
        ViewGroup.LayoutParams layoutParams = scaleView.getLayoutParams();
        layoutParams.width = new Random().nextInt(200) + 200;
        scaleView.requestLayout();
    }

    private boolean outline = true;

    public void changeOutline(View view) {
        outline = !outline;
        scaleView.setOutlineEnabled(outline);
    }

    private boolean direction = false;

    public void changeDirection(View view) {
        direction = !direction;
        scaleView.setExpandRtlEnabled(direction);
    }
}
