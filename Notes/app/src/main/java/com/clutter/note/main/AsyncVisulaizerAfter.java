package com.clutter.note.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by csimcik on 11/3/2017.
 */
public class AsyncVisulaizerAfter extends AsyncTask<Void,Void,Bitmap> {
    float minny;
    float maxxy;
    Bitmap spectrogram;
    Canvas canvas;
    Paint paint;
    float span;
    int counting;
    String file, pathFile;
    String duration;
    int spanInit;
    Context mContext;
    private ArrayList<ArrayList<Double>> graphDetails;
    private Bitmap myBitmap;
    ArrayList<int[]> miniMaxi;
    public AsyncVisulaizerAfter(ArrayList<ArrayList<Double>> myList, ArrayList<int[]> minandmax, int count, String bmpFile, Context context, String timer, String pathFile) {
        graphDetails = myList;
        mContext = context;
        miniMaxi = minandmax;
        counting = count;
        this.pathFile = pathFile;
        file = bmpFile;
        duration = timer;

    }
    @Override
    protected Bitmap doInBackground(Void... params) {
        myBitmap = Bitmap.createBitmap(MainActivity.w+75,200, Bitmap.Config.ARGB_8888);
        span = Math.round(myBitmap.getWidth()/graphDetails.size()+1);

        canvas = new Canvas(myBitmap);
        paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        canvas.drawColor(Color.CYAN);
        return spectrogram = drawGraph();

    }
    public Bitmap drawGraph() {
        if(graphDetails != null && graphDetails.size() > 0) {
            for(int i = 0; i < graphDetails.size(); i++){
                Log.i("graphDetails size ", String.valueOf(graphDetails.size()));
                Log.i("minnymaxxy ", String.valueOf(miniMaxi.size()));
                minny = (Float.parseFloat(graphDetails.get(i).get(miniMaxi.get(i)[0]).toString()));
                maxxy = (Float.parseFloat(graphDetails.get(i).get(miniMaxi.get(i)[1]).toString()));
                spanInit = i * Math.round(span);
                for (int j = 0; j < graphDetails.get(i).size(); j++) {
                        float up = (Float.parseFloat(graphDetails.get(i).get(j).toString()));
                    float amplitude = up;
                    int Col = Math.round(mapColors(minny, maxxy, amplitude));
                    if(Col < 0){
                        Col = 0;
                    }else if(Col > 200){
                        Col = 200;
                    }
                        paint.setARGB(255, Col, Col, Col);
                    //    canvas.drawRect(spanInit, 0, spanInit + span, myBitmap.getHeight() - (int) Math.round(j *2.55), paint);
                    if(Col == 0 ) {
                    }
                    }
                }
            }
return myBitmap;
    }
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        Bitmap playIcon = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.play_icon);
        Bitmap scaledIcon = Bitmap.createScaledBitmap(playIcon,myBitmap.getHeight(),myBitmap.getHeight(),false);
        paint.setColor(Color.WHITE);
        paint.setAlpha(175);
        canvas.drawBitmap(scaledIcon,10,0,paint);
        paint.setAlpha(175);
        paint.setTextSize(128);
        canvas.drawText(duration,220,myBitmap.getHeight()/2+45,paint);
        new AudioBitmp(counting,bitmap,file,pathFile).execute();

    }
    public float mapColors(float inMin, float inMax, float in){
        float output;
        float input = Math.round(in);
        float output_end = 175;
        float output_start = 50;
        float input_start = Math.round(inMin);
        float input_end = Math.round(inMax);
        output = output_start + ((output_end - output_start) / (input_end - input_start)) * (input - input_start);
        return output;
    }


    }


