package com.github.pengrad.mapscaleview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;

public class Drawer {

    private final Paint textPaint = new Paint();
    private final Paint strokePaint = new Paint();
    private final Path strokePath = new Path();

    private final Paint outlinePaint = new Paint();
    private final Path outlineDiffPath = new Path();
    private float outlineStrokeWidth = 2; // strokeWidth * 2
    private float outlineStrokeDiff = outlineStrokeWidth / 2 / 2;  // strokeWidth / 2
    private float outlineTextStrokeWidth = 3; // density * 2
    private boolean outlineEnabled = true;

    private float textHeight;
    private float horizontalLineY;

    Drawer(int color, float textSize, float strokeWidth, float density, boolean outlineEnabled) {
        textPaint.setAntiAlias(true);
        textPaint.setColor(color);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(textSize);

        strokePaint.setAntiAlias(true);
        strokePaint.setColor(color);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(strokeWidth);

        outlinePaint.set(strokePaint);
        outlinePaint.setARGB(255, 255, 255, 255);
        outlineStrokeWidth = strokeWidth * 2;
        outlineStrokeDiff = strokeWidth / 2;
        outlineTextStrokeWidth = density * 2;
        this.outlineEnabled = outlineEnabled;

        update();
    }

    void update() {
        outlinePaint.setTextSize(textPaint.getTextSize());
        outlinePaint.setStrokeWidth(outlineTextStrokeWidth);

        Rect textRect = new Rect();
        if (outlineEnabled) {
            outlinePaint.getTextBounds("A", 0, 1, textRect);
        } else {
            textPaint.getTextBounds("A", 0, 1, textRect);
        }
        textHeight = textRect.height();

        horizontalLineY = textHeight + textHeight / 2;
    }

    int getHeight() {
        return (int) (textPaint.getTextSize() * 3 + textPaint.getStrokeWidth());
    }

    void setColor(int color) {
        textPaint.setColor(color);
        strokePaint.setColor(color);
    }

    void setTextSize(float textSize) {
        textPaint.setTextSize(textSize);
        update();
    }

    void setStrokeWidth(float strokeWidth) {
        strokePaint.setStrokeWidth(strokeWidth);
        outlineStrokeWidth = strokeWidth * 2;
        outlineStrokeDiff = strokeWidth / 2;
        update();
    }

    void setOutlineEnabled(boolean enabled) {
        outlineEnabled = enabled;
        update();
    }

    void draw(Canvas canvas, Scales scales) {
        if (scales == null || scales.top() == null) {
            return;
        }

        Scale top = scales.top();

        if (outlineEnabled) {
            outlinePaint.setStrokeWidth(outlineTextStrokeWidth);
            canvas.drawText(top.text(), 0, textHeight, outlinePaint);
        }
        canvas.drawText(top.text(), 0, textHeight, textPaint);

        strokePath.rewind();
        strokePath.moveTo(outlineStrokeDiff, horizontalLineY);
        strokePath.lineTo(top.length(), horizontalLineY);
        if (outlineEnabled) {
            strokePath.lineTo(top.length(), textHeight + outlineStrokeDiff);
        } else {
            strokePath.lineTo(top.length(), textHeight);
        }

        Scale bottom = scales.bottom();
        if (bottom != null) {

            if (bottom.length() > top.length()) {
                strokePath.moveTo(top.length(), horizontalLineY);
                strokePath.lineTo(bottom.length(), horizontalLineY);
            } else {
                strokePath.moveTo(bottom.length(), horizontalLineY);
            }

            strokePath.lineTo(bottom.length(), textHeight * 2);

            float bottomTextY = horizontalLineY + textHeight + textHeight / 2;
            if (outlineEnabled) {
                canvas.drawText(bottom.text(), 0, bottomTextY, outlinePaint);
            }
            canvas.drawText(bottom.text(), 0, bottomTextY, textPaint);
        }

        if (outlineEnabled) {
            outlinePaint.setStrokeWidth(outlineStrokeWidth);
            outlineDiffPath.rewind();
            outlineDiffPath.moveTo(0, horizontalLineY);
            outlineDiffPath.lineTo(outlineStrokeDiff, horizontalLineY);
            outlineDiffPath.moveTo(top.length(), textHeight + outlineStrokeDiff);
            outlineDiffPath.lineTo(top.length(), textHeight);
            if (bottom != null) {
                outlineDiffPath.moveTo(bottom.length(), textHeight * 2);
                outlineDiffPath.lineTo(bottom.length(), textHeight * 2 + outlineStrokeDiff);
            }

            canvas.drawPath(outlineDiffPath, outlinePaint);
            canvas.drawPath(strokePath, outlinePaint);
        }

        canvas.drawPath(strokePath, strokePaint);
    }
}
