package com.github.pengrad.mapscaleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;

class ViewConfig {

    final int maxWidth;
    final int color;
    final float textSize;
    final float strokeWidth;
    final boolean isMiles;
    final boolean outline;
    final boolean expandRtl;

    ViewConfig(Context context, AttributeSet attrs) {
//        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        float density = context.getResources().getDisplayMetrics().density;

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MapScaleView, 0, 0);
        try {
            maxWidth = a.getDimensionPixelSize(R.styleable.MapScaleView_scale_maxWidth, (int) (100 * density));
            color = a.getColor(R.styleable.MapScaleView_scale_color, Color.parseColor("#333333"));
            textSize = a.getDimension(R.styleable.MapScaleView_scale_textSize, 12 * density);
            strokeWidth = a.getDimension(R.styleable.MapScaleView_scale_strokeWidth, 1.5f * density);
            isMiles = a.getBoolean(R.styleable.MapScaleView_scale_miles, false);
            outline = a.getBoolean(R.styleable.MapScaleView_scale_outline, true);
            expandRtl = a.getBoolean(R.styleable.MapScaleView_scale_expandRtl, false);
        } finally {
            a.recycle();
        }
    }
}
