package com.clutter.note.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by csimcik on 12/21/2017.
 */
public class PassClassEnd {
    ArrayList<short[]> data;
    ArrayList<Integer> mags;
    String file;
    String audioFile;
    String time;
    Context context;
    public PassClassEnd(ArrayList<Integer> mags,ArrayList<short[]> data, String fileName, String audioFile, String time){
        this.mags = mags;
        this.data = data;
        this.file = fileName;
        this.time = time;
        this.audioFile = audioFile;
    }
   public ArrayList<short[]> getData(){
        return data;
    }
    public String getFile(){
        return file;
    }
    public String getTimer(){
        return time;
    }
    public String getAudioFile(){
        return audioFile;
    }
}

