package com.clutter.note.main;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by csimcik on 10/12/2017.
 */
public class BtmpProcBckgrnd extends AsyncTask<Void,Void,Bitmap> {
    String data;
    String dataThumb;
    int itspos;

    public BtmpProcBckgrnd(String mainPhoto, String thumbPhoto, int pos ) {
        data = mainPhoto;
        dataThumb = thumbPhoto;
        this.itspos = pos;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        Bitmap processedBtmp = null;
        try {
           processedBtmp = getOrientation(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] tempString = new String[3];
        tempString[0] = "P";
        tempString[1] = dataThumb;
        tempString[2] = dataThumb;
        EventsModel eventsModel =  new EventsModel();
        eventsModel.setEventsID(String.valueOf(itspos));
        eventsModel.setType("P");
        eventsModel.setThumb(dataThumb);
        eventsModel.setUrl(data);
        MainActivity.database.insertRecordB(eventsModel);
        MainActivity.todayArray = MainActivity.database.getTodaysRecords(String.valueOf(itspos));
        MainActivity.daysL.get(MainActivity.todayObj).allObj.add(tempString);
        Log.i("this it a val", MainActivity.todayArray.get(MainActivity.todayArray.size()-1).getThumb());
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(dataThumb);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        processedBtmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
        try {
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        DBModel dbModel = new DBModel();
        dbModel.setYear(String.valueOf(MainActivity.daysL.get(MainActivity.todayObj).yearID));
        dbModel.setMonth(MainActivity.daysL.get(MainActivity.todayObj).monthID);
        dbModel.setDate(String.valueOf(MainActivity.daysL.get(MainActivity.todayObj).dateID));
        dbModel.setID("photo");
        dbModel.setUrl(data);
        dbModel.setThumb(dataThumb);
        MainActivity.database.insertRecord(dbModel);
        */
        return processedBtmp;
    }
    @Override
    protected void onPostExecute(Bitmap bitmap) {
       ArrayList<DBModel> arrayList = MainActivity.database.getAllRecords();
        for(int i = 0; i < arrayList.size();i++){
            Log.i("***THIS IS A MSG", arrayList.get(i).getMonth());
        }
        DetailFrag.thisAdapter.notifyDataSetChanged();
    }
    public Bitmap getOrientation(String myPhoto) throws IOException {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(myPhoto);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);
        Log.i("orientation", myPhoto + " " + String.valueOf(orientation));
        Bitmap greyBitmap = null;
        Bitmap scaledBitmap;
        Bitmap bitmapPre;
        Bitmap bitmap = BitmapFactory.decodeFile(myPhoto);
        int heightMod;
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                bitmap = rotateImage(bitmap, 90);
                Log.i("bitmap height", String.valueOf(bitmap.getHeight()));
                heightMod = (Integer) bitmap.getHeight() / (bitmap.getWidth() / MainActivity.w);
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, MainActivity.w, heightMod, false);
                Log.i("bitmap height", String.valueOf(scaledBitmap.getHeight()));
                bitmapPre = Bitmap.createBitmap(scaledBitmap, 0, scaledBitmap.getHeight() / 2 + 63, scaledBitmap.getWidth(), 125);
                greyBitmap = toGrayscale(bitmapPre);
                break;



            case ExifInterface.ORIENTATION_ROTATE_180:
                bitmap = rotateImage(bitmap, 180);
                Log.i("bitmap height", String.valueOf(bitmap.getHeight()));
                heightMod = (Integer) bitmap.getHeight() / (bitmap.getWidth() / MainActivity.w);
                // height should match views
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, MainActivity.w, heightMod, false);
                Log.i("bitmap height", String.valueOf(scaledBitmap.getHeight()));
                bitmapPre = Bitmap.createBitmap(scaledBitmap, 0, scaledBitmap.getHeight() / 2 + 63, scaledBitmap.getWidth(), 125);
                greyBitmap = toGrayscale(bitmapPre);
                break;


            case ExifInterface.ORIENTATION_ROTATE_270:
                bitmap = rotateImage(bitmap, 270);
                Log.i("bitmap height", String.valueOf(bitmap.getHeight()));
                heightMod = (Integer) bitmap.getHeight() / (bitmap.getWidth() / MainActivity.w);
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, MainActivity.w, heightMod, false);
                Log.i("bitmap height", String.valueOf(scaledBitmap.getHeight()));
                bitmapPre = Bitmap.createBitmap(scaledBitmap, 0, scaledBitmap.getHeight() / 2 + 63, scaledBitmap.getWidth(), 125);
                greyBitmap = toGrayscale(bitmapPre);
                break;




        }

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

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        Log.i("this is it", dataThumb.toString());
        return bmpGrayscale;
    }
}
