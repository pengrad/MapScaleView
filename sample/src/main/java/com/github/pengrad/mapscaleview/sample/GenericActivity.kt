package com.github.pengrad.mapscaleview.sample

import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.pengrad.mapscaleview.MapScaleView
import com.mapbox.mapboxsdk.camera.CameraPosition
import kotlin.random.Random

open class GenericActivity : AppCompatActivity() {
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
    fun update(cameraPosition: CameraPosition) {
        for (scale in scales) {
            scale.update(cameraPosition.zoom.toFloat(), cameraPosition.target.latitude)
        }
    }

    fun update(cameraPosition: com.google.android.gms.maps.model.CameraPosition) {
        for (scale in scales) {
            scale.update(cameraPosition.zoom, cameraPosition.target.latitude)
        }
    }

    fun changeColor(view: View) {
        val getColor = { Color.rgb(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255)) }
        scales.forEach { it.setColor(getColor()) }
    }

    fun changeTextSize(view: View) {
        scales.forEach { it.setTextSize((Random.nextInt(10) + 12) * density) }
    }

    fun changeStrokeWidth(view: View) {
        scales.forEach { it.setStrokeWidth((Random.nextInt(3) + 0.5f) * density) }
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
        scaleView.layoutParams.width = Random.nextInt(200) + 200
        scaleView.requestLayout()
    }

    private var isDefaultFont = true
    fun changeFont(view: View) {
        isDefaultFont = if (isDefaultFont) {
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

    private var largeTiles: Boolean = false
    fun changeTileSize(view: View) {
        largeTiles = !largeTiles
        scales.forEach { it.setTileSize(if (largeTiles) 512 else 256) }
    }
}