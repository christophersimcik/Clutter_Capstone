package com.clutter.note.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by csimcik on 11/18/2017.
 */
public class RecordView extends View {
    public RecordView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }
    boolean active = false;
    boolean illum = false;
    Point pointA;
    Point pointB;
    Point pointC;
    Point pointD;
    Point pointE;
    Point pointF;
    Point pointG;
    Point pointH;
    int w;
    int h;
    int wStop;
    int hStop;
    int wStartStop;
    int hStartStop;
    int wStart;
    int hStart;
    int hfill;
    int wfill;
    int wstrtFill;
    int hstartFill;
    int hilite;
    int Expand;
    Paint paintStop;
    Paint paint;
    Path square;
    Path squareFill;
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        w = getWidth() - 10;
        h = getHeight() - 10;
        wStart = 5;
        hStart = 5;
        paint = new Paint();
        wStop = getWidth()-10;
        hStop = getHeight()-10;
        wfill = getWidth()-15;
        hfill = getHeight()-15;
        wstrtFill = 15;
        hstartFill = 15;
        wStartStop = 10;
        hStartStop = 10;
        paintStop = new Paint();
        pointA = new Point(wStartStop,hStartStop);
        pointB =  new Point(wStop,hStartStop);
        pointC = new Point(hStop,wStop);
        pointD =  new Point(wStartStop,hStop);
        pointE = new Point(wstrtFill,hstartFill);
        pointF =  new Point(wfill,hstartFill);
        pointG = new Point(hfill,wfill);
        pointH =  new Point(wstrtFill,hfill);
        square = new Path();
        squareFill = new Path();
        square.moveTo(pointA.x, pointA.y);
        square.lineTo(pointB.x, pointB.y);
        square.lineTo(pointC.x, pointC.y);
        square.lineTo(pointD.x, pointD.y);
        square.close();
        squareFill.moveTo(pointE.x, pointE.y);
        squareFill.lineTo(pointF.x, pointF.y);
        squareFill.lineTo(pointG.x, pointG.y);
        squareFill.lineTo(pointH.x, pointH.y);
        squareFill.close();
        if(illum){
            hilite = Color.parseColor("#f95204");
            w = getWidth();
            h = getWidth();
        }else{
            hilite = Color.parseColor("#d24d0f");
            w = getWidth()-10;
            h = getWidth()-10;
        }

        if(active){
            stopButton(canvas);
        }else{
            recordButton(canvas);
        }



    }
    public void recordButton(Canvas canvas){
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(hilite);
        canvas.drawCircle(w/2,h/2,w/2-10,paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        paint.setColor(Color.parseColor("#808080"));
        canvas.drawCircle(w/2,h/2,w/2-1,paint);
    }
    public void stopButton(Canvas canvas){
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(3);
        paint.setColor(Color.parseColor("#808080"));
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(square, paint);
        paint.setColor(hilite);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(squareFill, paint);
        }
}

