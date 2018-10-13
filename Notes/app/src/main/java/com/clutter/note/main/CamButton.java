package com.clutter.note.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by csimcik on 11/18/2017.
 */
public class CamButton extends View {
    public CamButton(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }
    boolean illum = false;
    int w;
    int h;
    int hilite;
    Paint paint = new Paint();
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        w = getWidth();
        h = getHeight();
        if(illum){
            hilite = Color.parseColor("#E33B65");
        }else{
            hilite = Color.parseColor("#E3763B");
        }


            recordButton(canvas);




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

}

