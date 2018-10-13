package com.clutter.note.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Created by csimcik on 3/22/2018.
 */
public class DataMarkerView extends View {
Paint dataPaint;
    Boolean pho = false, mov = false, snd = false, not = false, voc = false;
    RectF phoRect,movRect,sndRect,notRect,vocRect;
    int position,mnthPos;
    ArrayList<EventsModel> myData = new ArrayList();
    public DataMarkerView(Context context, AttributeSet attrs, int defStyleAttr, int myPos,int mnthPos) {
        super(context, attrs, defStyleAttr);
        dataPaint = new Paint();
        position = myPos;
        this.mnthPos = mnthPos;
    }
    public DataMarkerView(Context context, AttributeSet attrs,int myPos,int mnthPos) {
        super(context, attrs);
        dataPaint = new Paint();
        position = myPos;
        this.mnthPos = mnthPos;
    }
    public DataMarkerView(Context context, int myPos,int mnthPos) {
        super(context);
        dataPaint = new Paint();
        position = myPos;
        this.mnthPos = mnthPos;
    }
 @Override
    public void onDraw(Canvas canvas){
     int segment = (getWidth()/5);
     int segHalf = segment/2;
     phoRect = new RectF(segHalf-2,15,segHalf+2,getHeight()-15);
     movRect = new RectF(segment+segHalf-2,15,segment+segHalf+2,getHeight()-15);
     vocRect = new RectF(segment*2+segHalf-2,15,segment*2+segHalf+2,getHeight()-15);
     sndRect = new RectF(segment*3+segHalf-2,15,segment*3+segHalf+2,getHeight()-15);
     notRect = new RectF(segment*4+segHalf-2,15,segment*4+segHalf+2,getHeight()-15);
         dataPaint.setStyle(Paint.Style.FILL_AND_STROKE);
for(int i = 0; i < myData.size(); i ++) {
        switch (myData.get(i).getType()) {
            case "P":
                pho = true;
                break;
            case "M":
                mov = true;
                break;
            case "V":
                voc = true;
                break;
            case "S":
                snd = true;
                break;
            case "N":
                not = true;
                break;
            case "B":
                not = true;
                break;


        }
}
     if(pho){
         dataPaint.setColor(Color.parseColor("#E3763B"));
         dataPaint.setAlpha(100);
         canvas.drawRect(phoRect,dataPaint);
     }
     if(mov){
         dataPaint.setColor(Color.parseColor("#BFB9A4"));
         dataPaint.setAlpha(100);
         canvas.drawRect(movRect,dataPaint);
     }
     if(voc){
         dataPaint.setColor(Color.parseColor("#EAE17F"));
         dataPaint.setAlpha(100);
         canvas.drawRect(vocRect,dataPaint);
     }
    if(snd) {
        dataPaint.setColor(Color.parseColor("#63db41"));
        dataPaint.setAlpha(100);
        canvas.drawRect(sndRect,dataPaint);
    }
     if(not){
         dataPaint.setColor(Color.parseColor("#D5EBE5"));
         dataPaint.setAlpha(100);
         canvas.drawRect(notRect,dataPaint);
     }

     }

    public void setData(){
myData = MainActivity.database.getTodaysRecords(String.valueOf(MainActivity.monthofYear.get(mnthPos).get(position).id-1));
    }

    public void setWidth(int width,int height){
        setMeasuredDimension(width,height);
        this.invalidate();
    }
}
