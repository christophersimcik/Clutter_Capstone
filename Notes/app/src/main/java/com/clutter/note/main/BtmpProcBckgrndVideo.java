package com.clutter.note.main;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by csimcik on 10/12/2017.
 */
public class BtmpProcBckgrndVideo extends AsyncTask<Void,Void,Bitmap> {
    String data;
    String dataThumb;
    String vidDur;
    int itsPos;
    long vidDurConvert;
    long vidDurFetch;
    Bitmap frame;
    Bitmap bitmapPre;
    int heightMod;
    Bitmap scaledBitmap;
    Bitmap greyBitmap;
    Bitmap processedBtmp;
    Context mContext;
    MediaMetadataRetriever retriever;


    public BtmpProcBckgrndVideo(String mainVid, String thumbVid, Context context, int itsPos ) {
        data = mainVid;
        this.itsPos = itsPos;
        dataThumb = thumbVid;
        mContext = context;
        retriever = new MediaMetadataRetriever();
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        retriever.setDataSource(data);
        vidDur = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        vidDurConvert = Long.parseLong(vidDur);
        vidDurFetch = vidDurConvert/2;
        frame = retriever.getFrameAtTime(vidDurFetch);
        retriever.release();
        try {
            processedBtmp = getBitmap(frame);
            Log.i("this is it", String.valueOf( vidDurConvert));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("this is the bitmap", String.valueOf(data)+ "this is the bitmap " + String.valueOf(frame) + " " + vidDur + " " + String.valueOf(vidDurConvert ));
        String[] tempString = new String[3];
        tempString[0] = "M";
        tempString[1] = data;
        tempString[2] = dataThumb;
        EventsModel eventsModel =  new EventsModel();
        eventsModel.setEventsID(String.valueOf(itsPos));
        eventsModel.setType("M");
        eventsModel.setThumb(dataThumb);
        eventsModel.setUrl(data);
        MainActivity.database.insertRecordB(eventsModel);
        MainActivity.daysL.get(MainActivity.todayObj).allObj.add(tempString);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(dataThumb);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Log.i("this is a BITMAP", String.valueOf(processedBtmp));
        processedBtmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
        try {
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return processedBtmp;
    }
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        Log.i("tha bits width()", String.valueOf(bitmap.getWidth()));
        MainActivity.todayArray = MainActivity.database.getTodaysRecords(String.valueOf(MainActivity.todayObj));
        DetailFrag.thisAdapter.notifyDataSetChanged();
    }
    public Bitmap getBitmap(Bitmap aFrame) throws IOException {
        Log.i("(((SIZE)))"," INPUT W > " + aFrame.getWidth() + " INPUT H > " + aFrame.getHeight()+ " " + (MainActivity.w / aFrame.getWidth()));
        heightMod =  aFrame.getHeight() * (MainActivity.w / aFrame.getWidth());
        Log.i("(((ADJUST)))"," OUTPUT W > " + MainActivity.w + " OUTPUT H > " + heightMod);
        scaledBitmap = Bitmap.createScaledBitmap(aFrame, MainActivity.w, heightMod, false);
        bitmapPre = Bitmap.createBitmap(scaledBitmap, 0, scaledBitmap.getHeight() / 2 + 63, scaledBitmap.getWidth(), 125);
       // bitmapPre = Bitmap.createBitmap(scaledBitmap, 0, scaledBitmap.getHeight() / 2 - 10, scaledBitmap.getWidth(), 125);
        Log.i("here is the things","WIDTH > " +  bitmapPre.getWidth() +"HEIGHT > " + bitmapPre.getHeight() );
        greyBitmap = toGrayscale(bitmapPre);
        return greyBitmap;
    }
    public Bitmap rotateImage(Bitmap source, float angle) {

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        int quarterWidth = MainActivity.w/8, quarterHeight = height/8;
        int halfHeight = height/2, halfWidth = MainActivity.w/2;
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        Rect rect = new Rect((c.getWidth()/2)-(height/2),0,(c.getWidth()/2)+(height/2),height);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        paint.setColor(Color.WHITE);
        paint.setAlpha(175);
        paint.setTextSize(mContext.getResources().getDimension(R.dimen.textsize));
        c.drawText(buildDurationString(vidDur),(MainActivity.w /2)-50,(height/2)+quarterHeight,paint);
        Bitmap playIcon = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.play_icon);
        Bitmap scaledIcon = Bitmap.createScaledBitmap(playIcon,height,height,true);
        paint.setAlpha(175);
        c.drawBitmap(scaledIcon,(MainActivity.w/2)-200,0f,paint);

        return bmpGrayscale;
    }
    public String buildDurationString(String dur){
        int Lngth = dur.length();
        String mins = String.valueOf(Math.round((Float.parseFloat(dur)/1000))/60);
        if(mins.length() == 1){
            mins = "0"+mins;
        }
        String secs = String.valueOf(Math.round(Float.parseFloat(dur)/1000));
        if(secs.length() == 1){
            secs = "0"+secs;
        }
        String hundreths = String.valueOf(Math.round((Float.parseFloat(dur)/10)/10));
      Log.i("duration ", dur + " / " + mins + ":" + secs + ":" + hundreths );
        String durVal = mins + ":" + secs + ":" + hundreths;
        return  durVal;
    }
}
