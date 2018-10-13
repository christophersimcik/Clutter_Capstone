package com.clutter.note.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by csimcik on 1/6/2018.
 */
public class SubmitViewR extends View {
    public SubmitViewR(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }
    boolean active = false;
    boolean illum = false;
    int w,h,hStart,wStart;
    Paint paintStop;
    Paint paint;
    Path tri,triStroke;
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        w = getWidth();
        h = getHeight();
        wStart = 0;
        hStart = 0;
        paint = new Paint();
        paintStop = new Paint();
        if(active){
            submitButton(canvas);
        }else{
            submitButton(canvas);
        }



    }
    public void submitButton(Canvas canvas){
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.parseColor("#e89358"));
        RectF rectA = new RectF(w-h,hStart,w,h);
        RectF rectB = new RectF(wStart+(h/2),hStart,w-(h/2),h);
        RectF rectC = new RectF(wStart,hStart,wStart+h,h);
        canvas.drawArc(rectA,90,-180,true,paint);
        canvas.drawRect(rectB,paint);
        canvas.drawArc(rectC,90,180,true,paint);
    }
    public void setWidth(int wideVal){
        this.w = wideVal;
    }
}