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

    private final MapScaleModel mapScaleModel;

    private final Paint paint;

    private final int lineWidth = 3;

    public MapScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3);
        paint.setTextSize(25);

        mapScaleModel = new MapScaleModel();
    }

    public void update(Projection projection, CameraPosition cameraPosition) {
        mapScaleModel.setProjection(projection, cameraPosition);
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.save();

        drawView(canvas, paint);

        canvas.restore();
    }

    private void drawView(Canvas canvas, Paint paint) {
        if (!mapScaleModel.isValid()) return;

        float lineLength = mapScaleModel.getPixelLength();
        String text = mapScaleModel.getText();

        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);

        canvas.drawText(text, 0, textRect.height(), paint);

        canvas.drawLine(0, textRect.height(), lineLength, textRect.height(), paint);
        canvas.drawLine(lineLength - lineWidth / 2, textRect.height(), lineLength - lineWidth / 2, textRect.height() / 2, paint);
    }
}
