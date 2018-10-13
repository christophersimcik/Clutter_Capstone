package com.clutter.note.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by csimcik on 12/20/2017.
 */
public class AsyncSpect extends AsyncTask<PassClassEnd,Void,Bitmap> {
    int max_index, min_index;
    double real;
    double imaginary;
    Bitmap spectrogram;
    Canvas canvas;
    Paint paint;
    float span, scale, preData;
    int counting, magMax = 0, magMin = 1024;
    String file, pathFile;
    String duration;
    int spanInit;
    Context mContext;
    private ArrayList<short[]> audioDetails;
    private Bitmap myBitmap;
    ArrayList<Integer>mags;
    ArrayList<Double> prcData = new ArrayList<>();
    Bitmap playIcon;
    public AsyncSpect(Context context, Bitmap bitmap){
        mContext = context;
        playIcon = bitmap;
    }


    @Override
    protected Bitmap doInBackground(PassClassEnd... values) {

        // iterate through the collection of samples
        ArrayList<short[]> samples = new ArrayList<>();
        samples = values[0].getData();
        mags = values[0].mags;
        file = values[0].getFile();
        duration = values[0].getTimer();
        pathFile = values[0].getAudioFile();
        myBitmap = Bitmap.createBitmap(MainActivity.w,125, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(myBitmap);
        paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        span = Math.round(myBitmap.getWidth() / samples.size()+ 1);
        // determine and set min values and max values in the set
        for(int i = 0; i < mags.size(); i ++){
            if(mags.get(i) > magMax) {
                magMax = mags.get(i);
            }
            if(mags.get(i) < magMin){
                magMin = mags.get(i);
            }
        }
        if(magMin < 1){
            magMin = 1;
        }
        scale = (200f-span)/magMax;
        int gryscl = 10;
        paint.setARGB(255,gryscl,gryscl,gryscl);
        magMin = (int)(magMin*scale);
        magMin = myBitmap.getHeight()-magMin;
        int colScale = 255/myBitmap.getHeight();
        Log.i("magMins ", String.valueOf(magMin));
        gryscl = 0;
        for(int i = myBitmap.getHeight(); i > 0; i--){
            canvas.drawLine(0,i,myBitmap.getWidth(),i,paint);
               gryscl = gryscl + colScale;
          //  gryscl += colScale;
                if(gryscl > 255){
                    gryscl = 255;
                }
            Log.i("gryscale value = ", String.valueOf(gryscl));
                paint.setARGB(255, gryscl, gryscl, gryscl);
        }

        Log.i("scale VALS", String.valueOf(scale) + " " + magMax + " " + 200f/magMax);
        for (int i = 0; i < mags.size(); i++) {
            spanInit = i * Math.round(span);
            drawGraphRe(mags.get(i),canvas);
        }

        return spectrogram = myBitmap;
    }


    public Bitmap drawGraphRe(int aData,Canvas canvas) {
        float colorScale = 200f/255f;
        preData = (float)aData;
        aData = canvas.getHeight() - ((int)(preData*scale));
        int grade = 100;
        paint.setARGB(255,grade,grade,grade);
        float vals[] = new float[2];
        Log.i("vals Value", "Bottom > " + String.valueOf(vals[1]) + " Top >  " + String.valueOf(vals[0]) + " " + aData  );
        while (aData < myBitmap.getHeight()) {
                canvas.drawLine(spanInit,aData,spanInit+span,aData,paint);
                aData +=1;
                if(grade < 255) {
                    grade += 1;
                }
                paint.setARGB(255,grade,grade,grade);
                Log.i("mybitmap = ","aData = " + String.valueOf(aData));
        }

        return myBitmap;

    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        Bitmap scaledIcon = Bitmap.createScaledBitmap(playIcon, myBitmap.getHeight(), myBitmap.getHeight(), false);
        paint.setColor(Color.WHITE);
        paint.setAlpha(175);
        canvas.drawBitmap(scaledIcon,(MainActivity.w/2)-200,0f,paint);
        paint.setTextSize(mContext.getResources().getDimension(R.dimen.textsize));
        canvas.drawText(duration,(MainActivity.w /2)-50,(myBitmap.getHeight()/2)+ (myBitmap.getHeight()/4),paint);
        new AudioBitmp(counting, bitmap, file, pathFile).execute();

    }

    public float mapColors(float inMin, float inMax, float in) {
        float output;
        float input = Math.round(in);
        float output_end = 200;
        float output_start = 50;
        float input_start = Math.round(inMin);
        float input_end = Math.round(inMax);
        output = output_start + ((output_end - output_start) / (input_end - input_start)) * (input - input_start);
        return output;
    }
    public float[] gradiate(float top, float bottom, float spanner){
        top = bottom;
        bottom = bottom + spanner;
        float[] vals = new float[2];
        vals[0] = top;
        vals[1] = bottom;
        return vals;
    }

    public ArrayList<Double> getFFT(short[] rawAudio) {

        ArrayList<Double> visData = new ArrayList<>();
        double max_magnitude = Double.MIN_VALUE;
        double min_magnitude = Double.MAX_VALUE;
        // 0 pad the audio buffer

        short[] fftBuffer = new short[4096];

        for(int i = 0; i < fftBuffer.length; i++){
            if(i<rawAudio.length-1){
                fftBuffer[i] = rawAudio[i];
            }else{
                fftBuffer[i] = 0;
            }
        }

        // prepare Complex from 0 padded audio

        Complex[] fft = new Complex[fftBuffer.length];
        for (int i = 0; i < fftBuffer.length; i++) {
            if (i < fftBuffer.length) {
                fft[i] = new Complex(fftBuffer[i], 0);
            }
        }

        // run the fft on the Complex array

        Complex[] tryFFT;
        tryFFT = FFT.fft(fft);

        // find the magnitude of fft

        double[] magnitude = new double[fftBuffer.length / 2];
        for (int i = 0; i < fftBuffer.length / 2; i++) {
            real = tryFFT[i].re();
            imaginary = tryFFT[i].im();
            magnitude[i] = Math.sqrt(real * real + imaginary * imaginary);
            visData.add(magnitude[i]);
        }

        max_index = 0;
        min_index = magnitude.length;
        // get rid a these!?
        //  float maxNum;
        // float minNum;
        for (int i = 0; i < magnitude.length; i++) {
            if (magnitude[i] > max_magnitude && i != 0) {
                max_magnitude = (int) magnitude[i];
                max_index = i;
            }
            if (magnitude[i] < min_magnitude && i != 0) {
                min_magnitude = (int) magnitude[i];
                min_index = i;
            }
        }
        return visData;
    }

    // create spectrogram file


}
