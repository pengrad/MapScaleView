# Map Scale View
[ ![Download](https://api.bintray.com/packages/pengrad/maven/mapscaleview/images/download.svg) ](https://bintray.com/pengrad/maven/mapscaleview/_latestVersion)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-MapScaleView-green.svg?style=true)](https://android-arsenal.com/details/1/4541)

Scale view for any Android Maps SDK (not only Google Maps)

![Image](images/image_rtl.png)

## Contributing
I encourage you to participate in this project. Feel free to open issues with bugs or ideas, fork and send pull requests.  
Check [list of "help wanted" issues](https://github.com/pengrad/MapScaleView/issues?q=is%3Aissue+is%3Aopen+label%3A%22help+wanted%22) to start with.

## Usage
```groovy
dependencies {
    implementation 'com.github.pengrad:mapscaleview:1.5.0'
}
```

Include in layout file over map
```xml
<FrameLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/activity_main"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <fragment
        android:id="@+id/mapFragment"
        class="com.google.android.gms.maps.SupportMapFragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>

    <com.github.pengrad.mapscaleview.MapScaleView
        android:id="@+id/scaleView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom|end"
        android:layout_margin="4dp"/>
</FrameLayout>
```

With miles or custom style
```xml
<com.github.pengrad.mapscaleview.MapScaleView
        android:id="@+id/scaleView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom|end"
        android:layout_margin="4dp"
        app:scale_maxWidth="100dp"
        app:scale_color="#009"
        app:scale_miles="true"
        app:scale_outline="true"
        app:scale_strokeWidth="3dp"
        app:scale_textSize="20sp"
        app:scale_expandRtl="true"/>
```

Update on map changed
```kotlin
val scaleView: MapScaleView = findViewById(R.id.scaleView)
val cameraPosition = map.cameraPosition
// need to pass zoom and latitude
scaleView.update(cameraPosition.zoom, cameraPosition.target.latitude)
```

Full example with subscribing to map events and updating scale view
```kotlin
override fun onMapReady(googleMap: GoogleMap) {
    map = googleMap
    googleMap.setOnCameraMoveListener(this)
    googleMap.setOnCameraIdleListener(this)
}

override fun onCameraMove() {
    val cameraPosition = map.cameraPosition
    scaleView.update(cameraPosition.zoom, cameraPosition.target.latitude)
}

override fun onCameraIdle() {
    val cameraPosition = map.cameraPosition
    scaleView.update(cameraPosition.zoom, cameraPosition.target.latitude)
}
```

## Customization
```java
mapScaleView.setColor(@ColorInt int color)
mapScaleView.setTextSize(float textSize)
mapScaleView.setStrokeWidth(float strokeWidth)
mapScaleView.setTextFont(Typeface font)

// enable/disable white outline, enabled by default
mapScaleView.setOutlineEnabled(false)

mapScaleView.metersAndMiles() // default
mapScaleView.metersOnly()
mapScaleView.milesOnly()

// expand scale bar from right to left, disabled by default
mapScaleView.setExpandRtlEnabled(true)
```
