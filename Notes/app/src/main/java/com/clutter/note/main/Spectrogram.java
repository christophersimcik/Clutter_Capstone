package com.clutter.note.main;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by csimcik on 11/1/2017.
 */
public class Spectrogram extends View {
double datapoint;
    View view;
    double width;
    double height;
    double unitW;
    double unitH;
    public Spectrogram(Context context, Double data, View view) {
        super(context);
        this.view = view;
        this.datapoint = data;
        width = view.getWidth();
        height = view.getHeight();
        unitW = width/100;
        unitH = height/unitW;


    }

    @Override
    protected void onDraw(Canvas canvas){

    }
}
