package com.clutter.note.main;

import android.graphics.Bitmap;

/**
 * Created by csimcik on 12/21/2017.
 */
public class PassClass {
    short[] myData;
    Bitmap myVis;
    public PassClass(short[] data,Bitmap vis){
        this.myData = data;
        this.myVis = vis;
    }
   public short[] getData(){
        return myData;
    }
    public Bitmap getBitmap(){
        return myVis;
    }
}

