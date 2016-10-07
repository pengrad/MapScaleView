package com.github.pengrad.mapscaleview;

import android.graphics.Point;

import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

class MapScaleModel {

    private final int[] meters = {
            5, 10, 20, 50, 100, 200, 500, 1000, 2000, 5000, 10000, 20000, 50000, 100000, 200000, 500000, 1000000, 2000000};

    private final LatLng src = new LatLng(23, 105);

    private int maxWidth;

    private float zoom = -1;
    private Scale scale;

    MapScaleModel() {
    }

    void setMaxWidth(int width) {
        maxWidth = width;
    }

    Scale setProjection(Projection projection, CameraPosition cameraPosition) {
        if (zoom == cameraPosition.zoom) return scale;

        int distance = 0;
        int distanceIndex = meters.length;
        double screenDistance = maxWidth + 1;

        while (screenDistance > maxWidth && distanceIndex > 0) {
            distance = meters[--distanceIndex];

            LatLng dest = DistanceUtils.translatePoint(src, distance, 120);

            Point pointSrc = projection.toScreenLocation(src);
            Point pointDest = projection.toScreenLocation(dest);

            screenDistance = Math.sqrt(Math.pow(pointSrc.x - pointDest.x, 2) + Math.pow(pointSrc.y - pointDest.y, 2));
        }

        zoom = cameraPosition.zoom;
        scale = new Scale(text(distance), (float) screenDistance);

        return scale;
    }

    private String text(int distance) {
        if (distance < 1000) return distance + " m";
        else return distance / 1000 + " km";
    }
}
