package com.clutter.note.main;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by csimcik on 12/3/2017.
 */
public class AsyncDayEvent extends AsyncTask<Void,Void,String> {
    static int myPos;
    public AsyncDayEvent (int pos){
        this.myPos = pos;
    }
    @Override
    protected String doInBackground(Void... params) {
        //MainActivity.todayArray = MainActivity.database.getTodaysRecords(String.valueOf(myPos));
        return null;
    }
    @Override
    protected void onPostExecute(String mystring){
       // DetailFrag.thisAdapter.notifyDataSetChanged();
    }
}
