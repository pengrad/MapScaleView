package com.github.pengrad.mapscaleview;

import com.google.android.gms.maps.model.CameraPosition;

class MapScaleModel {

    private static final double TILE_SIZE_AT_0_ZOOM = 156543.03;

    private final int[] meters = {
            5, 10, 20, 50, 100, 200, 500, 1000, 2000, 5000, 10000, 20000, 50000, 100000, 200000, 500000, 1000000, 2000000};

    private int maxWidth;

    private float lastZoom = -1;
    private Scale scale;

    MapScaleModel() {
    }

    void setMaxWidth(int width) {
        maxWidth = width;
    }

    /**
     * See http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames#Resolution_and_Scale
     */
    Scale setCameraPosition(CameraPosition cameraPosition) {
        if (lastZoom == cameraPosition.zoom) return scale;

        final float zoom = cameraPosition.zoom;
        final double latitude = cameraPosition.target.latitude;
        final double resolution = TILE_SIZE_AT_0_ZOOM * Math.cos(latitude) / Math.pow(2, zoom);

        int distance = 0;
        int distanceIndex = meters.length;
        double screenDistance = maxWidth + 1;

        while (screenDistance > maxWidth && distanceIndex > 0) {
            distance = meters[--distanceIndex];
            screenDistance = Math.abs(distance / resolution);
        }

        lastZoom = zoom;
        scale = new Scale(text(distance), (float) screenDistance);

        return scale;
    }

    private String text(int distance) {
        if (distance < 1000) return distance + " m";
        else return distance / 1000 + " km";
    }
}
