package com.clutter.note.main;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by csimcik on 11/3/2017.
 */
public class AsyncVisulaizer extends AsyncTask<Void,Void,Bitmap> {
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
    public AsyncVisulaizer(ArrayList<Double> myList,int cntr,Bitmap bitmapInput,int min, int max) {
        graphDetails = myList;
        counter = cntr;
        maxR = max;
        minR = min;
        myBitmap = bitmapInput;

    }
    @Override
    protected Bitmap doInBackground(Void... params) {
        Log.i("Async", String.valueOf(graphDetails.size()));
        canvas = new Canvas(myBitmap);
        paint = new Paint();
        if(AudioFrag.myBits.size()>0) {
            canvas.drawColor(0,PorterDuff.Mode.CLEAR);
            canvas.drawBitmap(AudioFrag.myBits.get(0),0,0,paint);
            AudioFrag.myBits.remove(0);
        }
    return spectrogram = drawGraph();

    }


    public Bitmap drawGraph() {
        if(graphDetails != null && graphDetails.size() > 0) {
            if(minR < graphDetails.size()) {
                minny = Float.parseFloat(graphDetails.get((int) minR).toString());
            }
            maxxy = Float.parseFloat(graphDetails.get((int)maxR).toString());
            for (int i = 0; i < graphDetails.size(); i++) {
                float thisOne = Float.parseFloat(graphDetails.get(i).toString());
                float up = (float) (graphDetails.get(i) - 1);
                float down = (float) (graphDetails.get(i) + 1);
                    int Col = (int)mapColors(minny,maxxy,thisOne);
              // Log.i("Color", String.valueOf(Col) + " -  count " + minny+ " min " +  maxxy + " max " +  String.valueOf(counter) + " container " + String.valueOf(i) + " value " + graphDetails.get(i) + " containers " + graphDetails.size());
                    paint.setARGB(200, Col, Col, Col);

               //canvas.drawRect(counter, 128 - (int) Math.round(i * 3.99)+1, counter+2, 128 -(int) Math.round(i * 3.99)-1, paint);
                canvas.drawRect(counter,0, counter+1, 127 -(int) Math.round(i * 2.55), paint);
                Bitmap.createBitmap(myBitmap,0,0,counter,myBitmap.getHeight());

            }
            if (counter > 715) {
                AudioFrag.myBits.add(Bitmap.createBitmap(myBitmap, 1, 0, 720 - 1, 256));
                counter = 715;
            }


        }
return myBitmap;
    }
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        AudioFrag.imageView.setImageBitmap(bitmap);
        Log.i("numb = ", String.valueOf(MainActivity.w));
        Log.i("bitmap = ", String.valueOf(bitmap.getWidth()));
       // AudioFrag.imageView.invalidate();

    }
    public float mapColors(float inMin, float inMax, float in){
        float output;
        float input = in;
        float output_end = 225;
        float output_start = 0;
        float input_start = inMin;
        float input_end = inMax;
        output = output_start + ((output_end - output_start) / (input_end - input_start)) * (input - input_start);
        return output;
    }

    }


