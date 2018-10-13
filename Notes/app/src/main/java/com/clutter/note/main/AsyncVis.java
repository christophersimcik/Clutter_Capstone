package com.clutter.note.main;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by csimcik on 12/20/2017.
 */
public class AsyncVis extends AsyncTask<PassClass,PassClass,Bitmap> {
    int max_index, min_index;
    double real;
    double imaginary;
    Bitmap spectrogram;
    Canvas canvas;
    Paint paint;
    Bitmap myBitmap;
    private ArrayList<Double> processedData = new ArrayList<>();

    @Override
    protected Bitmap doInBackground(PassClass...data) {

        myBitmap = Bitmap.createBitmap(MainActivity.w,300, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(myBitmap);
        paint = new Paint();
        paint.setAlpha(150);
        canvas.drawBitmap(data[0].getBitmap(),0,0,paint);
            return spectrogram = drawGraph(data[0].getData());
    }


    public Bitmap drawGraph(short[] data) {
        int halfHeight = 275;
        int halfWidth = MainActivity.w/2;
        int xPrevR = halfWidth;
        int xPrevL = halfWidth;
        int colVal;
        processedData = getFFT(data);
        int yPrev = halfHeight-Integer.parseInt(String.valueOf(Math.round(processedData.get(max_index)/2048)));
        for(int i = 0; i < max_index; i ++) {
            if (i < processedData.size()) {
                processedData.remove(i);
            }
        }
        for (int i = 1; i < processedData.size()/4; i++) {
            float up = Float.parseFloat(String.valueOf(processedData.get(i-1)/2048));
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

public ArrayList<Double> getFFT(short[] rawAudio) {

    ArrayList<Double> visData = new ArrayList<>();
    double max_magnitude = Double.MIN_VALUE;
    double min_magnitude = Double.MAX_VALUE;
    short[] fftBuffer;
    int count = 0;
    Log.i("size of audi0", String.valueOf(rawAudio.length));
fftBuffer = rawAudio;
/*
    for(int i = 896; i < fftBuffer.length; i++){
            fftBuffer[i] = 0;
        }
*/
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

    double[] magnitude = new double[tryFFT.length / 2];
    for (int i = 0; i < tryFFT.length / 2; i++) {
        real = tryFFT[i+1].re();
        imaginary = tryFFT[i+1].im();
        magnitude[i] = Math.sqrt(real * real + imaginary * imaginary);
        visData.add(magnitude[i]);

    }

    max_index = 0;
    min_index = magnitude.length;
  //  float maxNum;
    // float minNum;
    for (int i = 0; i < magnitude.length; i++) {
        if (magnitude[i] > max_magnitude) {
            max_magnitude = (int) magnitude[i];
            max_index = i;
        }
        if (magnitude[i] < min_magnitude) {
            min_magnitude = (int) magnitude[i];
            min_index = i;
        }
    }
    AudioFrag.magnitudes.add(max_index);
    return visData;
    }

}
