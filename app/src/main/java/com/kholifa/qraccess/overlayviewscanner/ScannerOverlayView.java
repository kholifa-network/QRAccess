package com.kholifa.qraccess.overlayviewscanner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup;


import com.kholifa.qraccess.R;

import static androidx.core.content.ContextCompat.getColor;




public class ScannerOverlayView extends ViewGroup {
    private float left, top, endY;
    private int rect_width, rect_height;
    private int frames;
    private boolean rev_animation;
    private int line_color, line_width;

    public ScannerOverlayView(Context context) {
        super(context);

    }

    public ScannerOverlayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScannerOverlayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        rect_width = 270;
        rect_height = 250;
        line_color = Color.RED;
        line_width = 2;
        frames = 3;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        left = (w - dpToPx(rect_width)) / 2;
        top = (h - dpToPx(rect_height)) / 2;
        endY = top;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int corner_radius = 0;
        Paint eraser = new Paint();
        eraser.setAntiAlias(true);
        eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));


        RectF rect = new RectF(left, top, dpToPx(rect_width) + left, dpToPx(rect_height) + top);
        canvas.drawRoundRect(rect, (float) corner_radius, (float) corner_radius, eraser);

        Paint stroke = new Paint();
        stroke.setStyle(Paint.Style.STROKE);
        stroke.setStrokeWidth(5);
        stroke.setColor(getColor(getContext(), R.color.themeBlueDark));
        stroke.setStrokeJoin(Paint.Join.MITER);
        canvas.drawPath(createCornersPath(left, top, dpToPx(rect_width) + left, dpToPx(rect_height) + top, 90), stroke);


        Paint line = new Paint();
        line.setColor(line_color);
        line.setStrokeWidth(Float.valueOf(line_width));


        if (endY >= top + dpToPx(rect_height) + frames - 5) {
            rev_animation = true;
        } else if (endY == top + frames) {
            rev_animation = false;
        }


        if (rev_animation) {
            endY -= frames;
        } else {
            endY += frames;
        }
        canvas.drawLine(left + 5, endY, left + dpToPx(rect_width) - 5, endY, line);
        invalidate();
    }


    private Path createCornersPath(float left, float top, float right, float bottom, int cornerWidth) {
        Path path = new Path();

        path.moveTo(left, top + cornerWidth);
        path.lineTo(left, top);
        path.lineTo(left + cornerWidth, top);

        path.moveTo(right - cornerWidth, top);
        path.lineTo(right, top);
        path.lineTo(right, top + cornerWidth);

        path.moveTo(left, bottom - cornerWidth);
        path.lineTo(left, bottom);
        path.lineTo(left + cornerWidth, bottom);

        path.moveTo(right - cornerWidth, bottom);
        path.lineTo(right, bottom);
        path.lineTo(right, bottom - cornerWidth);


        return path;
    }

}