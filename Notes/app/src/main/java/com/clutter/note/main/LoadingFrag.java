package com.clutter.note.main;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by csimcik on 2/26/2018.
 */
public class LoadingFrag extends Fragment {
    public static LoadScreen loadScreen;
    Boolean canAdd = true, canDisplay =true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.loading_screen,container,false);
        loadScreen=(LoadScreen)view.findViewById(R.id.loading_view);
        loadScreen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
               switch(event.getAction()) {
                   case MotionEvent.ACTION_DOWN:
                       if(canAdd) {
                           canAdd = false;
                           LoadScreen.boxList.clear();
                       }
                       break;
                   case MotionEvent.ACTION_UP:
                       canAdd = true;
               }
                return true;
            }
        });
        return view;
    }
    public void quitSurface(Boolean setBoolean){
        loadScreen.setPlaying(setBoolean);
    }
    public void setColor(){
        loadScreen.setBackgroundColor(Color.parseColor("#FFFAFA"));
    }
}