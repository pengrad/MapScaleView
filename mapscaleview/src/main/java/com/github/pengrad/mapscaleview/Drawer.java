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

    private boolean expandRtlEnabled;
    private int viewWidth;

    Drawer(int color, float textSize, float strokeWidth, float density, boolean outlineEnabled, boolean expandRtlEnabled) {
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
        this.expandRtlEnabled = expandRtlEnabled;

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
    
    void setExpandRtlEnabled(boolean enabled) {
        expandRtlEnabled = enabled;
    }

    void setViewWidth(int width) {
        viewWidth = width;
    }

    void draw(Canvas canvas, Scales scales) {
        if (scales == null || scales.top() == null) {
            return;
        }
        if (expandRtlEnabled && viewWidth == 0) {
            expandRtlEnabled = false;
        }

        Scale top = scales.top();

        if (expandRtlEnabled) {
            outlinePaint.setTextAlign(Paint.Align.RIGHT);
            textPaint.setTextAlign(Paint.Align.RIGHT);
        } else {
            outlinePaint.setTextAlign(Paint.Align.LEFT);
            textPaint.setTextAlign(Paint.Align.LEFT);
        }

        if (outlineEnabled) {
            outlinePaint.setStrokeWidth(outlineTextStrokeWidth);
            canvas.drawText(top.text(), expandRtlEnabled ? viewWidth : 0, textHeight, outlinePaint);
        }
        canvas.drawText(top.text(), expandRtlEnabled ? viewWidth : 0, textHeight, textPaint);

        strokePath.rewind();
        strokePath.moveTo(expandRtlEnabled ? (viewWidth - outlineStrokeDiff) : outlineStrokeDiff, horizontalLineY);
        strokePath.lineTo(expandRtlEnabled ? (viewWidth - top.length()) : top.length(), horizontalLineY);
        if (outlineEnabled) {
            strokePath.lineTo(expandRtlEnabled ? (viewWidth - top.length()) : top.length(), textHeight + outlineStrokeDiff);
        } else {
            strokePath.lineTo(expandRtlEnabled ? (viewWidth - top.length()) : top.length(), textHeight);
        }

        Scale bottom = scales.bottom();
        if (bottom != null) {

            if (bottom.length() > top.length()) {
                strokePath.moveTo(expandRtlEnabled ? (viewWidth - top.length()) : top.length(), horizontalLineY);
                strokePath.lineTo(expandRtlEnabled ? (viewWidth - bottom.length()) : bottom.length(), horizontalLineY);
            } else {
                strokePath.moveTo(expandRtlEnabled ? (viewWidth - bottom.length()) : bottom.length(), horizontalLineY);
            }

            strokePath.lineTo(expandRtlEnabled ? (viewWidth - bottom.length()) : bottom.length(), textHeight * 2);

            float bottomTextY = horizontalLineY + textHeight + textHeight / 2;
            if (outlineEnabled) {
                canvas.drawText(bottom.text(), expandRtlEnabled ? viewWidth : 0, bottomTextY, outlinePaint);
            }
            canvas.drawText(bottom.text(), expandRtlEnabled ? viewWidth : 0, bottomTextY, textPaint);
        }

        if (outlineEnabled) {
            outlinePaint.setStrokeWidth(outlineStrokeWidth);
            outlineDiffPath.rewind();
            outlineDiffPath.moveTo(expandRtlEnabled ? viewWidth : 0, horizontalLineY);
            outlineDiffPath.lineTo(expandRtlEnabled ? (viewWidth - outlineStrokeDiff) : outlineStrokeDiff, horizontalLineY);
            outlineDiffPath.moveTo(expandRtlEnabled ? (viewWidth - top.length()) : top.length(), textHeight + outlineStrokeDiff);
            outlineDiffPath.lineTo(expandRtlEnabled ? (viewWidth - top.length()) : top.length(), textHeight);
            if (bottom != null) {
                outlineDiffPath.moveTo(expandRtlEnabled ? (viewWidth - bottom.length()) : bottom.length(), textHeight * 2);
                outlineDiffPath.lineTo(expandRtlEnabled ? (viewWidth - bottom.length()) : bottom.length(), textHeight * 2 + outlineStrokeDiff);
            }

            canvas.drawPath(outlineDiffPath, outlinePaint);
            canvas.drawPath(strokePath, outlinePaint);
        }

        canvas.drawPath(strokePath, strokePaint);
    }
}
