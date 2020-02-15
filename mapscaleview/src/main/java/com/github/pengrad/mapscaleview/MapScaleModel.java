package com.github.pengrad.mapscaleview;

class MapScaleModel {

    private static final double TILE_SIZE_METERS_AT_0_ZOOM = 156543.03;
    private static final double TILE_SIZE_FT_AT_0_ZOOM = 513592.62;

    private static final int FT_IN_MILE = 5280;

    private static final float[] METERS = {0.2f, 0.5f, 1, 2, 5, 10, 20, 50, 100, 200, 500, 1000,
            2000, 5000, 10000, 20000, 50000, 100000, 200000, 500000, 1000000, 2000000};

    private static final float[] FT = {1, 2, 5, 10, 20, 50, 100, 200, 500, 1000, 2000,
            FT_IN_MILE, 2 * FT_IN_MILE, 5 * FT_IN_MILE, 10 * FT_IN_MILE, 20 * FT_IN_MILE, 50 * FT_IN_MILE,
            100 * FT_IN_MILE, 200 * FT_IN_MILE, 500 * FT_IN_MILE, 1000 * FT_IN_MILE, 2000 * FT_IN_MILE};

    private final float density;
    private int maxWidth;

    private float lastZoom = -1;
    private double lastLatitude = -100;

    MapScaleModel(float density) {
        this.density = density;
    }

    // returns true if width changed
    boolean updateMaxWidth(int width) {
        if (maxWidth != width) {
            maxWidth = width;
            return true;
        } else return false;
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

        double tileSizeAtZoom0 = meters ? TILE_SIZE_METERS_AT_0_ZOOM : TILE_SIZE_FT_AT_0_ZOOM;
        float[] distances = meters ? METERS : FT;

        final double resolution = tileSizeAtZoom0 / density * Math.cos(latitude * Math.PI / 180) / Math.pow(2, zoom);

        float distance = 0;
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

    private String text(float distance, boolean meters) {
        if (meters) {
            if (distance < 1) return (int) (distance * 100) + " cm";
            if (distance < 1000) return (int) distance + " m";
            else return (int) distance / 1000 + " km";
        } else {
            if (distance < FT_IN_MILE) return (int) distance + " ft";
            else return (int) distance / FT_IN_MILE + " mi";
        }
    }
}
