package com.clutter.note.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by csimcik on 10/12/2017.
 */
public class AudioBitmp extends AsyncTask<Void,Void,Bitmap> {
    int count;
    int itsPos;
    Bitmap spect;
    String file, filePath;

    public AudioBitmp(int aCounter, Bitmap spectrogram, String filename, String filePath ) {
        this.count = aCounter;
        this.spect = spectrogram;
        this.file = filename;
        this.filePath = filePath;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
      //  spect = Bitmap.createScaledBitmap(spect,MainActivity.w,200,false);
        String[] tempString = new String[3];
        tempString[0] = "S";
        // Audio File
        tempString[1] = file;
        // Img File
        tempString[2] = file;
        MainActivity.daysL.get(MainActivity.todayObj).allObj.add(tempString);
       // MainActivity.todayObj.voiceObj.add(tempString);
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        spect.compress(Bitmap.CompressFormat.PNG, 90, fos);
        try {
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return spect;
    }
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        itsPos = MainActivity.todayObj;
        EventsModel eventsModel =  new EventsModel();
        eventsModel.setEventsID(String.valueOf(itsPos));
        eventsModel.setType("S");
        eventsModel.setThumb(file);
        eventsModel.setUrl(filePath);
        Log.i("this is the VAL",eventsModel.getUrl());
        MainActivity.database.insertRecordB(eventsModel);
        MainActivity.todayArray = MainActivity.database.getTodaysRecords(String.valueOf(MainActivity.todayObj));
        DetailFrag.thisAdapter.notifyDataSetChanged();
    }

}
