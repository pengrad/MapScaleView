package com.github.pengrad.mapscaleview;

import android.graphics.Point;

import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

class MapScaleModel {

    private final LatLng src = new LatLng(23, 105);

    private float zoom = -1;

    private String text;
    private float lenght;

    MapScaleModel() {
    }

    void setProjection(Projection projection, CameraPosition cameraPosition) {
        if (zoom == cameraPosition.zoom) return;

        zoom = cameraPosition.zoom;

        int diff = 10000;

        LatLng dest = DistanceUtils.translatePoint(src, diff, 120);

        Point pointSrc = projection.toScreenLocation(src);
        Point pointDest = projection.toScreenLocation(dest);
        double screenDistance = Math.sqrt(Math.pow(pointSrc.x - pointDest.x, 2) + Math.pow(pointSrc.y - pointDest.y, 2));

        lenght = (float) screenDistance;
        text = "10 km";
    }

    float getPixelLength() {
        return lenght;
    }

    String getText() {
        return text;
    }

    boolean isValid() {
        return zoom > 0;
    }
}
