package com.clutter.note.main;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;


/**
 * Created by csimcik on 11/21/2017.
 */
public class WaitingView extends View {
    Paint paint;
    int width;
    int height;
    //Bitmap bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
    int spacer;
    int half;
    int counter;
    float radius;
    Handler handler;
    Boolean canGrow = true;
    Ball[] balls = new Ball[5];

    public WaitingView(Context context, AttributeSet sets) {
        super(context,sets);
        counter = 0;
        handler = new Handler();
        paint = new Paint();
        radius = 3;
        handler.postDelayed(animator,10);
        for(int i = 0;i < 5; i++){
            balls[i]= new Ball(spacer*(i+1),half,paint);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        width = getWidth();
        height = getHeight();
        spacer = width/6;
        half  =height/2;
        for(int i = 0 ; i< balls.length; i++){
            balls[i].x=spacer*(i+1);
            balls[i].y=half;
        }
        super.onDraw(canvas);
        Log.i("there is a thing", String.valueOf(getWidth()));
      for(int i = 0; i < 5; i++) {
          if(radius > 15){
              radius = 3;
          }
       if(counter >=0 && counter <10){
           if(i == 0){
               if(radius <15) {
                   radius += 1;
               }
               balls[i].drawIT(radius,canvas);
           }else{
               balls[i].drawIT(balls[i].aRadius,canvas);
           }
       }else if(counter >= 10 && counter <20){
           if(i == 1){
               if(radius <15) {
                   radius += 1;
               }
               balls[i].drawIT(radius,canvas);
           }else{
               balls[i].drawIT(balls[i].aRadius,canvas);
           }

       }else if(counter >= 20 && counter <30){
           if(i == 2){
               if(radius <15) {
                   radius += 1;
               }
               balls[i].drawIT(radius,canvas);
           }else{
               balls[i].drawIT(balls[i].aRadius,canvas);
           }

       }else if(counter >= 30 && counter <40){
           if(i == 3){
               if(radius <15) {
                   radius += 1;
               }
               balls[i].drawIT(radius,canvas);
           }else{
               balls[i].drawIT(balls[i].aRadius,canvas);
           }

       }else if(counter >= 40 && counter < 50){
           if(i == 4){
               if(radius <15) {
                   radius += 1;
               }
               balls[i].drawIT(radius,canvas);
           }else{
               balls[i].drawIT(balls[i].aRadius,canvas);
           }

       }
      }

        }

    private Runnable animator = new Runnable() {
        @Override
        public void run() {
                invalidate();
                handler.postDelayed(animator,10);
                if(counter <50) {
                    counter += 1;
                }else{
                    counter = 0;
                }
        }
    };
    public class Ball{
        int x;
        int y;
        float aRadius = 3;
        Paint aPaint;
        public Ball(int x,int y,Paint aPaint){
            this.x=x;
            this.y=y;
            this.aPaint = aPaint;
        }
        public void drawIT(float rad, Canvas canvas){
            if(rad > 3 ){
                rad -= .25;
            }
            aRadius =rad;

            paint.setColor(Color.parseColor("#808080"));
            paint.setAlpha(mapNumbs(5,15,(int) aRadius));
            paint.setFlags(Paint.ANTI_ALIAS_FLAG);
            canvas.drawCircle(x,y,rad,aPaint);
        }
        public int mapNumbs(int inMin, int inMax, int in){
          int output;
            int input = Math.round(in);
            int output_end = 255;
            int output_start = 100;
            int input_start = Math.round(inMin);
            int input_end = Math.round(inMax);
            output = output_start + ((output_end - output_start) / (input_end - input_start)) * (input - input_start);
            return output;
        }

    }
}
