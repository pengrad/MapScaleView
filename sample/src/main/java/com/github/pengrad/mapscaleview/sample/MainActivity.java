package com.github.pengrad.mapscaleview.sample;

import android.graphics.Color;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener {

    private GoogleMap map;
    private MapScaleView[] scales;
    private MapScaleView scaleView;
    private float density;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        scales = new MapScaleView[]{
                findViewById(R.id.scaleView),
                findViewById(R.id.scaleViewMiles),
                findViewById(R.id.scaleViewRtl),
                findViewById(R.id.scaleViewBg),
                findViewById(R.id.scaleViewBgFixed),
        };
        scaleView = scales[0];
        density = getResources().getDisplayMetrics().density;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        googleMap.setOnCameraMoveListener(this);
        googleMap.setOnCameraIdleListener(this);

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

    private void update(CameraPosition cameraPosition) {
        for (MapScaleView scale : scales) {
            scale.update(cameraPosition.zoom, cameraPosition.target.latitude);
        }
    }

    public void changeColor(View view) {
        Random random = new Random();
        int color = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
        for (MapScaleView scale : scales) scale.setColor(color);
    }

    public void changeTextSize(View view) {
        for (MapScaleView scale : scales) scale.setTextSize((new Random().nextInt(10) + 12) * density);
    }

    public void changeStrokeWidth(View view) {
        for (MapScaleView scale : scales) scale.setStrokeWidth((new Random().nextInt(3) + 0.5f) * density);
    }

    private boolean isMiles = false;

    public void changeMiles(View view) {
        isMiles = !isMiles;
        scaleView.milesOnly();
    }

    private boolean isMeters = false;

    public void changeMeters(View view) {
        isMeters = !isMeters;
        if (isMeters) for (MapScaleView scale : scales) scale.metersOnly();
        else for (MapScaleView scale : scales) scale.metersAndMiles();
    }

    public void changeSize(View view) {
        ViewGroup.LayoutParams layoutParams = scaleView.getLayoutParams();
        layoutParams.width = new Random().nextInt(200) + 200;
        scaleView.requestLayout();
    }

    private boolean outline = true;

    public void changeOutline(View view) {
        outline = !outline;
        for (MapScaleView scale : scales) scale.setOutlineEnabled(outline);
    }

    private boolean direction = false;

    public void changeDirection(View view) {
        direction = !direction;
        for (MapScaleView scale : scales) scale.setExpandRtlEnabled(direction);
    }
}
