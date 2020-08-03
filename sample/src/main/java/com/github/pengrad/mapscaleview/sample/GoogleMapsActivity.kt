package com.github.pengrad.mapscaleview.sample

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.github.pengrad.mapscaleview.MapScaleView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener
import com.google.android.gms.maps.GoogleMap.OnCameraMoveListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import kotlin.random.Random.Default.nextInt

class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback, OnCameraMoveListener, OnCameraIdleListener {

    private lateinit var map: GoogleMap
    private val scales by lazy {
        listOf<MapScaleView>(
            findViewById(R.id.scaleView),
            findViewById(R.id.scaleViewMiles),
            findViewById(R.id.scaleViewRtl),
            findViewById(R.id.scaleViewBg),
            findViewById(R.id.scaleViewBgFixed)
        )
    }
    private val scaleView get() = scales[0]
    private val density by lazy { resources.displayMetrics.density }

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

    private fun update(cameraPosition: CameraPosition) {
        for (scale in scales) {
            scale.update(cameraPosition.zoom, cameraPosition.target.latitude)
        }
    }

    fun changeColor(view: View) {
        val getColor = { Color.rgb(nextInt(255), nextInt(255), nextInt(255)) }
        scales.forEach { it.setColor(getColor()) }
    }

    fun changeTextSize(view: View) {
        scales.forEach { it.setTextSize((nextInt(10) + 12) * density) }
    }

    fun changeStrokeWidth(view: View) {
        scales.forEach { it.setStrokeWidth((nextInt(3) + 0.5f) * density) }
    }

    fun changeMiles(view: View) {
        scaleView.milesOnly()
    }

    private var isMeters = false
    fun changeMeters(view: View) {
        isMeters = !isMeters
        if (isMeters) scales.forEach { it.metersOnly() }
        else scales.forEach { it.metersAndMiles() }
    }

    fun changeSize(view: View) {
        scaleView.layoutParams.width = nextInt(200) + 200
        scaleView.requestLayout()
    }

    private var isDefaultFont = true
    fun changeFont(view: View) {
        isDefaultFont = if (isDefaultFont){
            scales.forEach { it.setTextFont(Typeface.DEFAULT) }
            false
        } else {
            scales.forEach { it.setTextFont(Typeface.DEFAULT_BOLD) }
            true
        }
    }

    private var outline = true
    fun changeOutline(view: View) {
        outline = !outline
        scales.forEach { it.setOutlineEnabled(outline) }
    }

    private var direction = false
    fun changeDirection(view: View) {
        direction = !direction
        scales.forEach { it.setExpandRtlEnabled(direction) }
    }

    var largeTiles: Boolean = false
    fun changeTileSize(view: View) {
        largeTiles = !largeTiles
        scales.forEach { it.setTileSize(if (largeTiles) 512 else 256) }
    }
}
