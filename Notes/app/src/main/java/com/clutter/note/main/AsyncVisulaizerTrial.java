package com.clutter.note.main;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by csimcik on 11/3/2017.
 */
public class AsyncVisulaizerTrial extends AsyncTask<Void,Void,Bitmap> {
    int counter;
    float maxR;
    float minR;
    float minny;
    float maxxy;
    Bitmap spectrogram;
    Canvas canvas;
    Paint paint;
    private ArrayList<Double> graphDetails;
    Bitmap myBitmap;
    Bitmap oldBit;
    public AsyncVisulaizerTrial(ArrayList<Double> myList, int cntr, Bitmap bitmapInput, int min, int max) {
        this.oldBit = bitmapInput;
        this.graphDetails = myList;
        this.counter = cntr;
        this.maxR = max;
        this.minR = min;
      //  myBitmap = bitmapInput;

    }
    @Override
    protected Bitmap doInBackground(Void... params) {
        Log.i("Async", String.valueOf(graphDetails.size()));
        myBitmap = Bitmap.createBitmap(MainActivity.w,200, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(myBitmap);
        paint = new Paint();
        paint.setAlpha(150);
        canvas.drawBitmap(oldBit,0,0,paint);

    return spectrogram = drawGraph();

    }


    public Bitmap drawGraph() {
        int halfHeight = 175;
        int halfWidth = MainActivity.w/2;
        int xPrevR = halfWidth;
        int xPrevL = halfWidth;
        int yPrev = halfHeight-Integer.parseInt(String.valueOf(Math.round(graphDetails.get((int)maxR)/2048)));
        int colVal;
        for(int i = 0; i < maxR; i ++){
            graphDetails.remove(i);
        }
            for (int i = 1; i < graphDetails.size()/4; i++) {
                float up = Float.parseFloat(String.valueOf(graphDetails.get(i-1)/2048));
                paint.setFlags(Paint.ANTI_ALIAS_FLAG);
                paint.setStrokeWidth(3);
                paint.setColor(Color.parseColor("#808080"));
                colVal = Math.abs(xPrevR-halfWidth);
                colVal = 255 - colVal;
                if(colVal < 0){
                    colVal = 0;
                }
                paint.setAlpha(colVal);

                canvas.drawLine(xPrevR,yPrev,halfWidth+(float)(i*2.8),halfHeight-up,paint);
                canvas.drawLine(xPrevL,yPrev,halfWidth-(float)(i*2.8),halfHeight-up,paint);
                xPrevR = (int) (halfWidth+i*2.8);
                xPrevL = (int) (halfWidth-i*2.8);
                yPrev= (int)(halfHeight-up);


            }

return myBitmap;
    }
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        AudioFrag.imageView.setImageBitmap(bitmap);
       // AudioFrag.imageView.invalidate();
        //AudioFrag.graphQueue.remove(counter-1);

    }
    public float mapColors(float inMin, float inMax, float in){
        float output;
        float input = in;
        float output_end = 200;
        float output_start = 0;
        float input_start = inMin;
        float input_end = inMax;
        output = output_start + ((output_end - output_start) / (input_end - input_start)) * (input - input_start);
        return output;
    }

    }


