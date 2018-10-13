package com.clutter.note.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by csimcik on 10/13/2017.
 */
public class CircleText extends TextView {

    private float strokeWidth;
    int strokeColor,solidColor,strokeW;
    int h;
    int w;

    public CircleText(Context context) {
        super(context);
    }

    public CircleText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void draw(Canvas canvas) {

        Paint circlePaint = new Paint();
        circlePaint.setColor(solidColor);
        circlePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        Paint strokePaint = new Paint();
        strokePaint.setColor(strokeColor);
        strokePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        int  h = this.getHeight();
        int  w = this.getWidth();

        int diameter = ((h > w) ? h : w);
        int radius = w/2;

        this.setHeight(h);
        this.setWidth(w);

        canvas.drawCircle(w/2,h/2, radius, strokePaint);

        canvas.drawCircle(w/2,h/2, radius-strokeWidth, circlePaint);

        super.draw(canvas);
    }

    public void setStrokeWidth(int dp)
    {
        float scale = getContext().getResources().getDisplayMetrics().density;
        strokeWidth = dp*scale;
    }

    public void setStrokeColor(String color)
    {
        strokeColor = Color.parseColor(color);
    }

    public void setSolidColor(String color)
    {

            solidColor = Color.parseColor(color);


    }
    public void setIntColor(int color)
    {

        solidColor = color;


    }

}
