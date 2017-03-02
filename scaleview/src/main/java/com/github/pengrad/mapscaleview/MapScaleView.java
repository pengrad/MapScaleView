package com.github.pengrad.mapscaleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;

public class MapScaleView extends View {

    private final Paint paint;
    private final ViewConfig viewConfig;
    private final MapScaleModel mapScaleModel;

    private float textHeight;
    private float strokeWidth;
    private float horizontalLineY;

    private Scale scale;

    public MapScaleView(Context context) {
        this(context, null);
    }

    public MapScaleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        viewConfig = new ViewConfig(context, attrs);

        mapScaleModel = new MapScaleModel();
        mapScaleModel.setIsMiles(viewConfig.isMiles);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(viewConfig.textSize);
        paint.setColor(viewConfig.color);
        paint.setStrokeWidth(viewConfig.strokeWidth);
        strokeWidth = viewConfig.strokeWidth;

        updateTextHeight();
    }

    private void updateTextHeight() {
        Rect textRect = new Rect();
        paint.getTextBounds("A", 0, 1, textRect);
        textHeight = textRect.height();
        horizontalLineY = textHeight + textHeight / 2;
    }

    public void setColor(@ColorInt int color) {
        paint.setColor(color);
        invalidate();
    }

    public void setTextSize(float textSize) {
        paint.setTextSize(textSize);
        updateTextHeight();
        invalidate();
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        paint.setStrokeWidth(strokeWidth);
        invalidate();
    }

    public void setIsMiles(boolean miles) {
        scale = mapScaleModel.setIsMiles(miles);
        invalidate();
    }

    public void update(float zoom, double latitude) {
        scale = mapScaleModel.update(zoom, latitude);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureDimension(desiredWidth(), widthMeasureSpec);
        int height = measureDimension(desiredHeight(), heightMeasureSpec);

        scale = mapScaleModel.setMaxWidth(width);

        setMeasuredDimension(width, height);
    }

    private int desiredWidth() {
        return viewConfig.desiredWidth;
    }

    private int desiredHeight() {
        return (int) (paint.getTextSize() * 1.5 + paint.getStrokeWidth());
    }

    private int measureDimension(int desiredSize, int measureSpec) {
        int mode = View.MeasureSpec.getMode(measureSpec);
        int size = View.MeasureSpec.getSize(measureSpec);

        if (mode == View.MeasureSpec.EXACTLY) {
            return size;
        } else if (mode == View.MeasureSpec.AT_MOST) {
            return Math.min(desiredSize, size);
        } else {
            return desiredSize;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.save();
        drawView(canvas, paint);
        canvas.restore();
    }

    private void drawView(Canvas canvas, Paint paint) {
        if (scale == null) return;

        final String text = scale.text();
        final float lineLength = scale.length();

        canvas.drawText(text, 0, textHeight, paint);

        float verticalLineX = lineLength - strokeWidth / 2;

        canvas.drawLine(0, horizontalLineY, lineLength, horizontalLineY, paint);
        canvas.drawLine(verticalLineX, horizontalLineY, verticalLineX, textHeight, paint);
    }
}
