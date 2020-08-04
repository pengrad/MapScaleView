package com.github.pengrad.mapscaleview.sample

import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener
import com.google.android.gms.maps.GoogleMap.OnCameraMoveListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class GoogleMapsActivity : GenericActivity(), OnMapReadyCallback, OnCameraMoveListener, OnCameraIdleListener {

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_googlemaps)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        googleMap.setOnCameraMoveListener(this)
        googleMap.setOnCameraIdleListener(this)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.07770360532252, -94.76820822805165), 12f))
    }

    override fun onCameraMove() {
        update(map.cameraPosition)
    }

    override fun onCameraIdle() {
        update(map.cameraPosition)
    }
}
