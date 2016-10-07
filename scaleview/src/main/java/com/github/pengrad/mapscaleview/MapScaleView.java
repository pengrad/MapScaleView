package com.github.pengrad.mapscaleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;

public class MapScaleView extends View {

    private final Paint paint;
    private final float density;
    private final MapScaleModel mapScaleModel;

    private Scale scale;

    public MapScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mapScaleModel = new MapScaleModel();

        density = context.getResources().getDisplayMetrics().density;

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(12 * density);
        paint.setStrokeWidth(1.5f * density);
        paint.setColor(Color.parseColor("#333333"));
    }

    public void update(Projection projection, CameraPosition cameraPosition) {
        scale = mapScaleModel.setProjection(projection, cameraPosition);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureDimension(desiredWidth(), widthMeasureSpec);
        int height = measureDimension(desiredHeight(), heightMeasureSpec);

        mapScaleModel.setMaxWidth(width);

        setMeasuredDimension(width, height);
    }

    private int desiredWidth() {
        return (int) (100 * density);
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

        final float lineLength = scale.length();
        final String text = scale.text();

        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);

        canvas.drawText(text, 0, textRect.height(), paint);

        int textHeight = textRect.height();
        float horizontalLineY = textHeight + textHeight / 2;
        float verticalLineX = lineLength - paint.getStrokeWidth() / 2;

        canvas.drawLine(0, horizontalLineY, lineLength, horizontalLineY, paint);
        canvas.drawLine(verticalLineX, horizontalLineY, verticalLineX, textHeight, paint);
    }
}
