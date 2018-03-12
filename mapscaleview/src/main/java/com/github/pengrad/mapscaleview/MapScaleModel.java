package com.github.pengrad.mapscaleview;

class MapScaleModel {

    private static final double TILE_SIZE_METERS_AT_0_ZOOM = 156543.03;
    private static final double TILE_SIZE_NM_AT_0_ZOOM = 84526.457812;

    private static final int[] METERS = {1, 2, 5, 10, 20, 50, 100, 200, 500, 1000, 2000, 5000,
            10000, 20000, 50000, 100000, 200000, 500000, 1000000, 2000000};
    private static final int[] NM = {1, 2, 5, 10, 20, 50, 100, 200, 500, 1000, 2000, 5000,
            10000, 20000, 50000, 100000, 200000, 500000, 1000000, 2000000};

    private final float density;
    private int maxWidth;

    private float lastZoom = -1;
    private double lastLatitude = -100;

    MapScaleModel(float density) {
        this.density = density;
    }

    void setMaxWidth(int width) {
        maxWidth = width;
    }

    void setPosition(float zoom, double latitude) {
        lastZoom = zoom;
        lastLatitude = latitude;
    }

    /**
     * See http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames#Resolution_and_Scale
     */
    Scale update(boolean meters) {
        float zoom = lastZoom;
        double latitude = lastLatitude;
        if (zoom < 0 || Math.abs(latitude) > 90) return null;

        double tileSizeAtZoom0 = meters ? TILE_SIZE_METERS_AT_0_ZOOM : TILE_SIZE_NM_AT_0_ZOOM;
        int[] distances = meters ? METERS : NM;

        final double resolution = tileSizeAtZoom0 / density * Math.cos(latitude * Math.PI / 180) / Math.pow(2, zoom);

        int distance = 0;
        int distanceIndex = distances.length;
        double screenDistance = maxWidth + 1;

        while (screenDistance > maxWidth && distanceIndex > 0) {
            distance = distances[--distanceIndex];
            screenDistance = Math.abs(distance / resolution);
        }

        lastZoom = zoom;
        lastLatitude = latitude;
        return new Scale(text(distance, meters), (float) screenDistance);
    }

    private String text(int distance, boolean meters) {
        if (meters) {
            if (distance < 1000) return distance + " m";
            else return distance / 1000 + " km";
        } else {
            return distance / 1000.0 + " M";
        }
    }
}
