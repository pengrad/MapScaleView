package com.github.pengrad.mapscaleview.sample

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        findViewById<Button>(R.id.googlemaps).setOnClickListener {
            startActivity(Intent(this, GoogleMapsActivity::class.java))
        }
        findViewById<Button>(R.id.mapbox).setOnClickListener {
            startActivity(Intent(this, MapboxActivity::class.java))
        }
    }
}