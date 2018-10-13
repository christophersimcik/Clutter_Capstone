package com.clutter.note.main;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;


/**
 * Created by csimcik on 1/20/2018.
 */
public class Eraser extends View {
    int width,height,counter = 0, unitWidth;
    private Handler handler = new Handler();
    boolean checkSize = false, active = false, erasing = false;
    Bitmap trashIcon;
    Paint paint = new Paint(), bitmapPaint = new Paint(), paintCirc = new Paint();
    //vectors?
    ArrayList<Rect> rects = new ArrayList<>();
    public Eraser(Context context, AttributeSet sets) {
        super(context, sets);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(),R.drawable.trash,null);
        trashIcon = ((BitmapDrawable)drawable).getBitmap();

    }
    @Override
    public void onDraw(Canvas canvas) {
        if(active) {
            canvas.drawColor(Color.TRANSPARENT);
            width = getWidth();
            height = getHeight();
            if (!checkSize) {
                figureSize();
            }
            paint.setColor(Color.parseColor("#fafafa"));
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(2);
            paint.setAntiAlias(true);
            paintCirc.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            paintCirc.setStyle(Paint.Style.FILL);;
            paintCirc.setAntiAlias(true);
            bitmapPaint.setAntiAlias(true);
            bitmapPaint.setFilterBitmap(true);
            for (int i = 0; i < rects.size(); i++) {
                int sizer = 255 / rects.size() / 2;
                if (255 - i * sizer < 255) {
                    paint.setAlpha(255 - i * sizer);
                } else {
                    paint.setAlpha(255);
                }
                canvas.drawRect(rects.get(i), paint);
            }
            if (counter / 2 < 225) {
                bitmapPaint.setAlpha(counter / 4);
            } else {
                bitmapPaint.setAlpha(225);
            }
            canvas.drawCircle(width-62,((height/2)+2),45,paintCirc);
            canvas.drawBitmap(trashIcon, width - 100, 25, bitmapPaint);
        }else{
            canvas.drawColor(Color.TRANSPARENT);
            handler.removeCallbacks(erase);
            counter = 0;
            rects.clear();
        }
        super.onDraw(canvas);
    }
    private Runnable erase = new Runnable() {

        @Override
        public void run() {
        if(counter < width){
       rects.add( new Rect(counter,0,counter+unitWidth,height));
            counter += unitWidth;
            handler.post(this);
            invalidate();
            Log.i("running ", String.valueOf(counter));
        }else{
            erasing = true;
        }

            }

    };
    public void deleting(){
        handler.postDelayed(erase,1);
    }
    private void figureSize(){
        if(width%10 == 0){
            unitWidth = 10;
        }else if(width% 9 == 0 ){
            unitWidth = 9;
        }else if(width% 8 == 0 ){
            unitWidth = 8;
        }else if(width% 7 == 0 ){
            unitWidth = 7;
        }else if(width% 6 == 0 ){
            unitWidth = 6;
        }else if(width% 5 == 0 ){
            unitWidth = 5;
        }else if(width% 4 == 0 ){
            unitWidth = 4;
        }else if(width% 3 == 0 ){
            unitWidth = 3;
        } else if(width% 2 == 0 ){
        unitWidth = 2;
    }
        trashIcon = Bitmap.createScaledBitmap(trashIcon,74,74,true);
        checkSize = true;
    }
}
