package com.clutter.note.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;

/**
 * Created by csimcik on 10/14/2017.
 */
public class TagGen {
    static Context mContext;
    static int avg;
    public static Bitmap drawHex(String fill, Context context, int average) {
        String selectFill = fill;
        mContext = context;
        avg =average;
        Bitmap finalBitmap = Bitmap.createBitmap(125, 125, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(finalBitmap);
        Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, finalBitmap.getWidth(),
                finalBitmap.getHeight());
        int halfH = finalBitmap.getWidth() / 2;
        int halfV = finalBitmap.getHeight() / 2;
        Path triR = new Path();
        Path triL = new Path();
        Point pointA,pointB,pointC,pointD,pointE,pointF;
        pointA = new Point(9,finalBitmap.getHeight());
        pointB = new Point(finalBitmap.getWidth(),9);
        pointC = new Point(finalBitmap.getWidth(),finalBitmap.getHeight());
        pointD = new Point(0,0);
        pointE = new Point(finalBitmap.getWidth()-9,0);
        pointF = new Point(0,finalBitmap.getHeight()-9);
        triR.moveTo(pointA.x,pointA.y);
        triR.lineTo(pointB.x,pointB.y);
        triR.lineTo(pointC.x,pointC.y);
        triR.close();
        triL.moveTo(pointD.x,pointD.y);
        triL.lineTo(pointE.x,pointE.y);
        triL.lineTo(pointF.x,pointF.y);
        triL.close();
        int wPlace = finalBitmap.getWidth()-67;
        int hPlace = finalBitmap.getHeight()-62;
        switch(selectFill){
            case "P":
                paint.setAntiAlias(true);
                paint.setFilterBitmap(true);
                paint.setAlpha(125);
               // paint.setColor(Color.parseColor("#4B4B4B"));
               // canvas.drawPath(triR,paint);
               // paint.setColor(Color.parseColor("#E3763B"));
               // canvas.drawPath(triL,paint);
                Bitmap tmpBit = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.photo_grey);
                tmpBit = Bitmap.createScaledBitmap(tmpBit,75,75,false);
                canvas.drawBitmap(tmpBit,10,25,paint);
                paint.setAlpha(255);
                paint.setColor(Color.parseColor("#E3763B"));
                canvas.drawCircle(95,20,15,paint);
                break;
            case "V":
                paint.setAntiAlias(true);
                paint.setFilterBitmap(true);
                paint.setAlpha(125);
                //paint.setColor(Color.parseColor("#4B4B4B"));
                //canvas.drawPath(triR,paint);
                //paint.setColor(Color.parseColor("#EAE17F"));
                //canvas.drawPath(triL,paint);
                tmpBit = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.term_grey);
                tmpBit = Bitmap.createScaledBitmap(tmpBit,75,75,false);
                canvas.drawBitmap(tmpBit,10,25,paint);
                paint.setAlpha(255);
                paint.setColor(Color.parseColor("#EAE17F"));
                canvas.drawCircle(95,20,15,paint);
                break;
            case "N":
                paint.setAntiAlias(true);
                paint.setFilterBitmap(true);
                paint.setAlpha(125);
                //paint.setColor(Color.parseColor("#4B4B4B"));
                //canvas.drawPath(triR,paint);
                //paint.setColor(Color.parseColor("#d5ebe5"));
                //canvas.drawPath(triL,paint);
                tmpBit = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.note);
                tmpBit = Bitmap.createScaledBitmap(tmpBit,75,75,false);
                canvas.drawBitmap(tmpBit,10,25,paint);
                paint.setAlpha(255);
                paint.setColor(Color.parseColor("#d5ebe5"));
                canvas.drawCircle(95,20,15,paint);
                break;
            case "M":
                paint.setAntiAlias(true);
                paint.setFilterBitmap(true);
                paint.setAlpha(125);
                //paint.setColor(Color.parseColor("#4B4B4B"));
                //canvas.drawPath(triR,paint);
                //paint.setColor(Color.parseColor("#BFB9A4"));
                //canvas.drawPath(triL,paint);
                tmpBit = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.mov_grey);
                tmpBit = Bitmap.createScaledBitmap(tmpBit,75,75,false);
                canvas.drawBitmap(tmpBit,10,25,paint);
                paint.setAlpha(255);
                paint.setColor(Color.parseColor("#BFB9A4"));
                canvas.drawCircle(95,20,15,paint);
                break;
            case "S":
                paint.setAntiAlias(true);
                paint.setFilterBitmap(true);
                //paint.setColor(Color.parseColor("#4B4B4B"));
                //canvas.drawPath(triR,paint);
                //paint.setColor(Color.parseColor("#EAE17F"));
                //canvas.drawPath(triL,paint);
                paint.setAlpha(125);
                tmpBit = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.snd_grey);
                tmpBit = Bitmap.createScaledBitmap(tmpBit,75,75,false);
                canvas.drawBitmap(tmpBit,10,25,paint);
                paint.setAlpha(255);
                paint.setColor(Color.parseColor("#63db41"));
                canvas.drawCircle(95,20,15,paint);
                break;
            case "G":
                paint.setAntiAlias(true);
                paint.setFilterBitmap(true);
                paint.setColor(Color.parseColor("#f1f1f1"));
                //canvas.drawCircle(halfH,halfV,42,paint);
                canvas.drawColor(Color.parseColor("#f1f1f1"));
                tmpBit = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.photo_mono);
                tmpBit = Bitmap.createScaledBitmap(tmpBit,62,62,false);
                canvas.drawBitmap(tmpBit,31,30,paint);
                break;
            case "B":
                paint.setAntiAlias(true);
                paint.setFilterBitmap(true);
                paint.setAlpha(125);
                //paint.setColor(Color.parseColor("#4B4B4B"));
                //canvas.drawPath(triR,paint);
                //paint.setColor(Color.parseColor("#EAE17F"));
                //canvas.drawPath(triL,paint);
                tmpBit = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.book_gray);
                tmpBit = Bitmap.createScaledBitmap(tmpBit,75,75,false);
                canvas.drawBitmap(tmpBit,10,25,paint);
                paint.setAlpha(255);
                paint.setColor(Color.parseColor("#EAE17F"));
                canvas.drawCircle(95,20,15,paint);
                break;

        }
        canvas.drawBitmap(finalBitmap, rect, rect, paint);
        return finalBitmap;

    }
}

//