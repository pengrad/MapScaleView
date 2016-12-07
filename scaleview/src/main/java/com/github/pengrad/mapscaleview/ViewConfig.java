package com.github.pengrad.mapscaleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;

public class ViewConfig {

    private int color;
    private float textSize;
    private float strokeWidth;

    private int desiredWidth;

    private Context context;


    ViewConfig(Context context, AttributeSet attrs) {
        this.context = context;
//        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        float density = context.getResources().getDisplayMetrics().density;

        desiredWidth = (int) (100 * density);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MapScaleView, 0, 0);
        try {
            color = a.getColor(R.styleable.MapScaleView_scale_color, Color.parseColor("#333333"));
            textSize = a.getDimension(R.styleable.MapScaleView_scale_textSize, 12 * density);
            strokeWidth = a.getDimension(R.styleable.MapScaleView_scale_strokeWidth, 1.5f * density);
        } finally {
            a.recycle();
        }

    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setColorResId(int colorResId){
        setColor(ResourcesCompat.getColor(context.getResources(),colorResId,null));
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public int getDesiredWidth() {
        return desiredWidth;
    }

    public void setDesiredWidth(int desiredWidth) {
        this.desiredWidth = desiredWidth;
    }
}
