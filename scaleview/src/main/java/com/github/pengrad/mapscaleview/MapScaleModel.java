package com.github.pengrad.mapscaleview;

class MapScaleModel {

    private static final double TILE_SIZE_METERS_AT_0_ZOOM = 156543.03;
    private static final double TILE_SIZE_FT_AT_0_ZOOM = 513592.62;

    private static final int FT_IN_MILE = 5280;

    private static final int[] METERS = {1, 2, 5, 10, 20, 50, 100, 200, 500, 1000, 2000, 5000,
            10000, 20000, 50000, 100000, 200000, 500000, 1000000, 2000000};

    private static final int[] FT = {1, 2, 5, 10, 20, 50, 100, 200, 500, 1000, 2000,
            FT_IN_MILE, 2 * FT_IN_MILE, 5 * FT_IN_MILE, 10 * FT_IN_MILE, 20 * FT_IN_MILE, 50 * FT_IN_MILE,
            100 * FT_IN_MILE, 200 * FT_IN_MILE, 500 * FT_IN_MILE, 1000 * FT_IN_MILE, 2000 * FT_IN_MILE};

    private int maxWidth;
    private boolean isMiles = false;
    private int[] distances = METERS;
    private double tileSizeAtZoom0 = TILE_SIZE_METERS_AT_0_ZOOM;

    private float lastZoom = -1;
    private double lastLatitude = -1;
    private Scale scale;

    MapScaleModel() {
    }

    void setMaxWidth(int width) {
        maxWidth = width;
        lastZoom = -1;
    }

    void setIsMiles(boolean miles) {
        if (miles) {
            tileSizeAtZoom0 = TILE_SIZE_FT_AT_0_ZOOM;
            distances = FT;
        } else {
            tileSizeAtZoom0 = TILE_SIZE_METERS_AT_0_ZOOM;
            distances = METERS;
        }
        isMiles = miles;
        lastZoom = -1;
    }

    /**
     * See http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames#Resolution_and_Scale
     */
    Scale update(final float zoom, final double latitude) {
        if (lastZoom == zoom && lastLatitude == latitude) return scale;

        final double resolution = tileSizeAtZoom0 * Math.cos(latitude * Math.PI / 180) / Math.pow(2, zoom);

        int distance = 0;
        int distanceIndex = distances.length;
        double screenDistance = maxWidth + 1;

        while (screenDistance > maxWidth && distanceIndex > 0) {
            distance = distances[--distanceIndex];
            screenDistance = Math.abs(distance / resolution);
        }

        lastZoom = zoom;
        lastLatitude = latitude;
        scale = new Scale(text(distance), (float) screenDistance);

        return scale;
    }

    private String text(int distance) {
        if (isMiles) {
            if (distance < FT_IN_MILE) return distance + " ft";
            else return distance / FT_IN_MILE + " mi";
        } else {
            if (distance < 1000) return distance + " m";
            else return distance / 1000 + " km";
        }
    }
}
