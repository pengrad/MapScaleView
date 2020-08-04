package com.github.pengrad.mapscaleview.sample

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Base64
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.pengrad.mapscaleview.MapScaleView
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random


private const val TAG = "MapboxActivity"
class MapboxActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapboxMap: MapboxMap
    private lateinit var mapView: MapView
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

        val secret: SecretKey = getSecretKey()

        val mapboxToken = decrypt(getString(R.string.mapbox_token_encrypted), BuildConfig.iv, secret)
        Mapbox.getInstance(this, mapboxToken?.let { String(it) })
        setContentView(R.layout.activity_mapbox)

        mapView = findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        // TODO set style
        // default style ()
        // TODO add scaleView
    }

    /**
     * Decrypt mapbox token using AES-CBC
     *
     * @param cipherText
     * @param iv
     * @param secret
     * @return plain text
     */
    private fun decrypt(cipherText: String, iv: String, secret: SecretKey): ByteArray? {
        val decipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        decipher.init(Cipher.DECRYPT_MODE, secret, IvParameterSpec(Base64.decode(iv, Base64.DEFAULT)))
        return decipher.doFinal(Base64.decode(cipherText, Base64.DEFAULT))
    }

    /**
     * Encrypt mapbox token if need is to regenerate it
     *
     * @param secret the mapbox token
     * @param secretKey from getSecretKey()
     * @return the iv to place in gradle.properties and the encrypted token
     */
    private fun encrypt(secret: String, secretKey: SecretKey): Pair<String, String> {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val iv = cipher.parameters.getParameterSpec(IvParameterSpec::class.java).iv
        val iv64 = Base64.encodeToString(iv, Base64.DEFAULT)
        val ciphertext = cipher.doFinal(secret.toByteArray(charset("UTF-8")))
        val cipherText64 = Base64.encodeToString(ciphertext, Base64.DEFAULT)
        return Pair(iv64, cipherText64)
    }

    /**
     *
     * @return secret key needed to decrypt the token
     */
    private fun getSecretKey(): SecretKey {
        val factory: SecretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        val password = "EUMiF9RuVmPo".toCharArray()
        val salt = "7xJkxAncVgos".toByteArray()
        // Only a few iterations because not a real use case
        val spec: KeySpec = PBEKeySpec(password, salt, 636, 256)
        val tmp: SecretKey = factory.generateSecret(spec)
        return SecretKeySpec(tmp.encoded, "AES")
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        mapboxMap.addOnCameraMoveListener{ update(mapboxMap.cameraPosition) }
        mapboxMap.addOnCameraIdleListener{ update(mapboxMap.cameraPosition)}
        mapboxMap.setStyle(Style.Builder().fromUri("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41"))
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.07770360532252, -94.76820822805165),12.0))
    }

    private fun update(cameraPosition: CameraPosition) {
        for (scale in scales) {
            scale.update(cameraPosition.zoom.toFloat(), cameraPosition.target.latitude)
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