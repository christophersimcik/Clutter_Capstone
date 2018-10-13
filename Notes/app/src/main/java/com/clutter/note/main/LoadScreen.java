package com.clutter.note.main;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by csimcik on 2/26/2018.
 */
public class LoadScreen extends SurfaceView implements Runnable {
    public static Thread thread;
    SurfaceHolder holder;
    public static ArrayList<Box> boxList;
    public Canvas canvas;
    public Paint paint;
    public static Boolean playing, canDrawField = true;
    public static int gridSize = 18, distRef;
    private long time, startTime = 0, tempTime = 0, fps;
    public static ArrayList<Point> NW, SW, NE, SE;
    public static ArrayList<Point> boxes;
    public static Point screenCenter;
    public int hyp, colorTime = 0, colorLast = 0, textTime = 0, textCol = 0;


    public LoadScreen(Context context, AttributeSet attr) {
        super(context, attr);
        holder = getHolder();
        paint = new Paint();
        canvas = new Canvas();
        playing = true;
        NW = new ArrayList<>();
        SW = new ArrayList<>();
        NE = new ArrayList<>();
        SE = new ArrayList<>();
        boxes = new ArrayList<>();
        hyp = (int) Math.sqrt(Math.pow(Math.pow(gridSize, 2), 2) + (Math.pow(Math.pow(gridSize, 2), 2)));
        thread = new Thread(this);
        thread.start();
        boxList = new ArrayList<>();
    }

    @Override
    public void run() {
       while(playing){
            // Capture the current time in milliseconds in startFrameTime
            if (startTime == 0) {
                startTime = System.currentTimeMillis();
            }
            ;

            // Update the frame
            update();

            // Draw the frame
            draw();
            // Calculate the fps this frame
            // We can then use the result to
            // time animations and more.
            time = System.currentTimeMillis() - startTime;
            if (time - tempTime > 100) {
                if (boxes.size() > 0) {
                    randomGen();
                    Log.i("box created", "timer working");
                    tempTime = time;
                }
            }
            if (time > 0) {
                fps = 1000 / time;
            }
            if (time % 200 == 0) {
                textTime += 1;
                textCol += 1;
            }
            if (textTime > 6) {
                textTime = 0;
            }
            if (textCol > 2) {
                textCol = 0;
            }
        }
            Log.i("loadscreen playing = ", playing.toString());
canvas.drawColor(Color.WHITE);
        }



    public void update() {
        if(colorTime-colorLast > 10){
            if(textTime<6){
                textTime +=1;
            }else{
                textTime = 0;
            }
            if(textCol < 2){
                textCol +=1;
            }else{
                textCol = 0;
            }
            colorLast = colorTime;
        }
    }
    public void randomGen() {
        if (boxList.size() < 45) {
            int chance;
            int secChance;
            Random randomA = new Random(), randomB = new Random();
            chance = randomA.nextInt(100);
            secChance = randomB.nextInt(100);
            if (chance % 10 == 0) {
                addBox();

                if (secChance % 50 == 0) {
                    addBox();
                }
            }
        }
    }


    public void draw() {

        colorTime += 1;
        if (holder.getSurface().isValid()) {
            // Lock the canvas ready to draw
            // Make the drawing surface our canvas object
            canvas = holder.lockCanvas();
            screenCenter = new Point(canvas.getWidth() / 2, canvas.getHeight() / 2);
            if (canDrawField) {
                drawField();
            }
            canvas.drawColor(Color.parseColor("#FAFAFA"));
            paint.setTextSize(50);
            loadText(paint, textTime);
            paint.setColor(Color.YELLOW);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            for (int i = 0; i < boxList.size(); i++) {
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.WHITE);
                boxList.get(i).drawBot(canvas, paint);
            }
            holder.unlockCanvasAndPost(canvas);
        }

    }

    public void drawField() {
        Point fieldT, fieldB, fieldR, fieldL;
        Path field = new Path();
        fieldT = new Point(screenCenter.x, screenCenter.y);
        fieldB = new Point(screenCenter.x, screenCenter.y + (hyp * 2));
        fieldL = new Point(screenCenter.x - hyp, screenCenter.y + hyp);
        fieldR = new Point(screenCenter.x + hyp, screenCenter.y + hyp);
        field.moveTo(fieldT.x, fieldT.y);
        field.lineTo(fieldR.x, fieldR.y);
        field.lineTo(fieldB.x, fieldB.y);
        field.lineTo(fieldL.x, fieldL.y);
        field.close();
        getNW(fieldT, fieldL);
        getSE(fieldR, fieldB);
        getSW(fieldL, fieldB);
        getNE(fieldT, fieldR);
        if(canDrawField) {
            createBoxCenters(fieldT, fieldL);
            canDrawField = false;
        }

    }

    public void getNW(Point pointA, Point pointB) {
        float distA, distB, xVal, yVal;
        distA = (float) Math.sqrt((Math.pow(pointB.x - pointA.x, 2)) + (Math.pow(pointB.y - pointA.y, 2)));
        distRef = (int) distA;
        for (int i = 0; i < gridSize; i++) {
            distB = (distA / gridSize) * i;
            xVal = pointA.x - ((distB * (pointA.x - pointB.x)) / distA);
            yVal = pointA.y - ((distB * (pointA.y - pointB.y)) / distA);
            NW.add(new Point((int) xVal, (int) yVal));
        }
    }

    public void getSE(Point pointA, Point pointB) {
        float distA, distB, xVal, yVal;
        distA = (float) Math.sqrt((Math.pow(pointB.x - pointA.x, 2)) + (Math.pow(pointB.y - pointA.y, 2)));
        for (int i = 0; i < gridSize; i++) {
            distB = (distA / gridSize) * i;
            xVal = pointA.x - ((distB * (pointA.x - pointB.x)) / distA);
            yVal = pointA.y - ((distB * (pointA.y - pointB.y)) / distA);
            SE.add(new Point((int) xVal, (int) yVal));
        }
    }

    public void getSW(Point pointA, Point pointB) {
        float distA, distB, xVal, yVal;
        distA = (float) Math.sqrt((Math.pow(pointB.x - pointA.x, 2)) + (Math.pow(pointB.y - pointA.y, 2)));
        for (int i = 0; i < gridSize; i++) {
            distB = (distA / gridSize) * i;
            xVal = pointA.x - ((distB * (pointA.x - pointB.x)) / distA);
            yVal = pointA.y - ((distB * (pointA.y - pointB.y)) / distA);
            SW.add(new Point((int) xVal, (int) yVal));
        }
    }

    public void getNE(Point pointA, Point pointB) {
        float distA, distB, xVal, yVal;
        distA = (float) Math.sqrt((Math.pow(pointB.x - pointA.x, 2)) + (Math.pow(pointB.y - pointA.y, 2)));
        for (int i = 0; i < gridSize; i++) {
            distB = (distA / gridSize) * i;
            xVal = pointA.x - ((distB * (pointA.x - pointB.x)) / distA);
            yVal = pointA.y - ((distB * (pointA.y - pointB.y)) / distA);
            NE.add(new Point((int) xVal, (int) yVal));
        }
    }

    public void createBoxCenters(Point pointA, Point pointB) {
        distRef = (distRef / gridSize);
        double hypotenuse = Math.sqrt((Math.pow(gridSize, 2)) + (Math.pow(gridSize, 2)));
        int newY;
        int newX;
        double factor;
        // top half + mid
        for (int i = gridSize - 1; i > 0; i--) {
            newY = NW.get(i).y;
            factor = hypotenuse;
            for (int j = i; j > 0; j--) {
                newX = NW.get(i).x + ((int) factor);
                boxes.add(new Point(newX, newY));
                factor += Math.round((hypotenuse * 2));
            }
        }
    }

    public class Box implements Comparable<Box> {
        Boolean canFall = true;
        Random randomPlace = new Random(), randomHeight = new Random(), randomColor = new Random();
        Path top, base, left, right, shadow;
        Point baseF, baseB, baseL, baseR, topF, topB, topL, topR, center, shadowF, shadowB, shadowL, shadowR;
        int height, limit, hypo, shadValue, myShad, colorselect, myCount;
        int place;
        double gravity;
        int[][] colors = new int[3][3];
        public Box() {
            super();
            myShad = 255;

            //blues
            colors[0][0] = Color.parseColor("#eaf7f3");
            colors[0][1] = Color.parseColor("#d5ebe5");
            colors[0][2] = Color.parseColor("#869e97");
            //reds
            colors[1][0] = Color.parseColor("#f4eea4");
            colors[1][1] = Color.parseColor("#eae17f");
            colors[1][2] = Color.parseColor("#d1c75c");
            //yellows
            colors[2][0] = Color.parseColor("#f2a57b");
            colors[2][1] = Color.parseColor("#e3763b");
            colors[2][2] = Color.parseColor("#e27e48");
            colorselect = randomColor.nextInt(3);
            hypo = (int) Math.round(Math.sqrt((Math.pow(gridSize, 2)) + (Math.pow(gridSize, 2))));
            height = randomHeight.nextInt(64) + gridSize;
            place = findSpot();
            center = boxes.get(place);
            boxes.remove(place);
            limit = center.y;
            gravity = 0;
            center.y = -100;
            shadowF = new Point(center.x, limit + (hypo));
            shadowB = new Point(center.x, limit - (hypo));
            shadowL = new Point(center.x - (hypo), limit);
            shadowR = new Point(center.x + (hypo), limit);
            shadow = new Path();
            shadow.moveTo(shadowF.x, shadowF.y);
            shadow.lineTo(shadowR.x, shadowR.y);
            shadow.lineTo(shadowB.x, shadowB.y);
            shadow.lineTo(shadowL.x, shadowL.y);
            shadow.close();
            myShad = 255;
            myCount = boxList.size();
        }

        public void drawBot(Canvas aCanvas, Paint aPaint) {
                if (canFall && center.y < limit) {
                    center.y += 1 * gravity;
                } else {
                    canFall = false;
                    center.y = limit;
                }

            baseF = new Point(center.x, center.y + hypo);
            baseB = new Point(center.x, center.y - hypo);
            baseL = new Point(center.x - hypo, center.y);
            baseR = new Point(center.x + hypo, center.y);
            topF = new Point(baseF.x, baseF.y - height);
            topB = new Point(baseF.x, baseF.y - (height + (hypo * 2)));
            topL = new Point(baseF.x - hypo, baseF.y - (height + hypo));
            topR = new Point(baseF.x + hypo, baseF.y - (height + hypo));
            base = new Path();
            base.moveTo(baseF.x, baseF.y);
            base.lineTo(baseL.x, baseL.y);
            base.lineTo(baseB.x, baseB.y);
            base.lineTo(baseR.x, baseR.y);
            base.close();
            top = new Path();
            top.moveTo(topF.x, topF.y);
            top.lineTo(topL.x, topL.y);
            top.lineTo(topB.x, topB.y);
            top.lineTo(topR.x, topR.y);
            top.close();
            left = new Path();
            left.moveTo(topL.x, topL.y);
            left.lineTo(topF.x, topF.y);
            left.lineTo(baseF.x, baseF.y);
            left.lineTo(baseL.x, baseL.y);
            left.close();
            right = new Path();
            right.moveTo(topR.x, topR.y);
            right.lineTo(topF.x, topF.y);
            right.lineTo(baseF.x, baseF.y);
            right.lineTo(baseR.x, baseR.y);
            right.close();
            if (center.y > 0) {
                myShad = (int) mapColors(0, limit, center.y);
            }
            if (center.y < limit) {
                aPaint.setColor(Color.argb(255, myShad, myShad, myShad));
                aCanvas.drawPath(shadow, aPaint);
            }
            aPaint.setStyle(Paint.Style.FILL);
            aPaint.setColor(colors[colorselect][0]);
            aCanvas.drawPath(top, aPaint);
            aPaint.setColor(colors[colorselect][1]);
            aCanvas.drawPath(left, aPaint);
            aPaint.setColor(colors[colorselect][2]);
            aCanvas.drawPath(right, aPaint);
            gravity += .5;
        }

        public float mapColors(float inMin, float inMax, float in) {
            float output;
            float input = in;
            float output_end = 100;
            float output_start = 255;
            float input_start = inMin;
            float input_end = inMax;
            output = output_start + ((output_end - output_start) / (input_end - input_start)) * (input - input_start);
            return output;
        }



        @Override
        public int compareTo(Box another) {
            if(limit < another.limit) {
                return -1;
            }else if(limit > another.limit){
                return 1;
            }else{
                return 0;
            }
        }
        public int findSpot(){
            int thisPlace;
                thisPlace = randomPlace.nextInt(boxes.size());
            return thisPlace;
        }
        public double getGravity(){
            return this.gravity;
        }
    }


    public void addBox() {
        boxList.add(new Box());
        swapSpot();



    }

    public void loadText(Paint myPaint, int spanTimer) {
        int next, prev = 0;
        int[] letterCols = new int[3];
        letterCols[0] = Color.parseColor("#d5ebe5");
        letterCols[1] = Color.parseColor("#eae17f");
        letterCols[2] = Color.parseColor("#e3763b");
        ForegroundColorSpan[] fgSpans;
        RelativeSizeSpan[]sizeSpan;
        //"LOADING change back
        SpannableString spannableString = new SpannableString("LOADING");
        float xStart, xEnd;
        xStart = screenCenter.x - (myPaint.measureText(spannableString,0,spannableString.length())/2);
        if (spanTimer == 0) {
            spannableString.setSpan(new ForegroundColorSpan(letterCols[textCol]), spanTimer, spanTimer + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new RelativeSizeSpan(1.025f), spanTimer, spanTimer + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new RelativeSizeSpan(1f), spanTimer+1, spannableString.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(Color.GRAY), spanTimer+1, spannableString.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            fgSpans = spannableString.getSpans(0, spannableString.length(), ForegroundColorSpan.class);
            sizeSpan = spannableString.getSpans(0,spannableString.length(),RelativeSizeSpan.class);
            for (int i = 0; i < fgSpans.length; i++) {
                myPaint.setColor(fgSpans[i].getForegroundColor());
                myPaint.setTextSize(sizeSpan[i].getSizeChange()*50);
                canvas.drawText(spannableString, spannableString.getSpanStart(fgSpans[i]), spannableString.getSpanEnd(fgSpans[i]), xStart, screenCenter.y-150, myPaint);
                xStart = xStart + (myPaint.measureText(spannableString,spannableString.getSpanStart(fgSpans[i]), spannableString.getSpanEnd(fgSpans[i])));
            }

            Log.i("spantimer = ", "0");
        } else if (spanTimer == spannableString.length() - 1) {
            spannableString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, spanTimer, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(letterCols[textCol]), spanTimer, spanTimer + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new RelativeSizeSpan(1f), 0, spanTimer, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new RelativeSizeSpan(1.025f), spanTimer, spanTimer + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            fgSpans = spannableString.getSpans(0, spannableString.length(), ForegroundColorSpan.class);
            sizeSpan = spannableString.getSpans(0,spannableString.length(),RelativeSizeSpan.class);
            for (int i = 0; i < fgSpans.length; i++) {
                myPaint.setColor(fgSpans[i].getForegroundColor());
                myPaint.setTextSize(sizeSpan[i].getSizeChange()*50);
                canvas.drawText(spannableString, spannableString.getSpanStart(fgSpans[i]), spannableString.getSpanEnd(fgSpans[i]), xStart, screenCenter.y-150, myPaint);
                xStart = xStart + (myPaint.measureText(spannableString,spannableString.getSpanStart(fgSpans[i]), spannableString.getSpanEnd(fgSpans[i])));
            }
            Log.i("spantimer = ", "last");
        } else {
            spannableString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, spanTimer, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(letterCols[textCol]), spanTimer, spanTimer + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(Color.GRAY), spanTimer+1, spannableString.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            spannableString.setSpan(new RelativeSizeSpan(1f), 0, spanTimer, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new RelativeSizeSpan(1.025f), spanTimer, spanTimer + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new RelativeSizeSpan(1f), spanTimer+1, spannableString.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            fgSpans = spannableString.getSpans(0, spannableString.length(), ForegroundColorSpan.class);
            sizeSpan = spannableString.getSpans(0,spannableString.length(),RelativeSizeSpan.class);
            for (int i = 0; i < fgSpans.length; i++) {
                myPaint.setColor(fgSpans[i].getForegroundColor());
                myPaint.setTextSize(sizeSpan[i].getSizeChange()*50);
                canvas.drawText(spannableString, spannableString.getSpanStart(fgSpans[i]), spannableString.getSpanEnd(fgSpans[i]), xStart, screenCenter.y-150, myPaint);
                xStart = xStart + (myPaint.measureText(spannableString,spannableString.getSpanStart(fgSpans[i]), spannableString.getSpanEnd(fgSpans[i])));
            }
            Log.i("spantimer = ", "anything else");
        }
    }




    public void swapSpot() {
        Collections.sort(boxList);
        // Log.i("boxList", String.valueOf(boxList.size()));
    }
    public void setPlaying(Boolean playBoolean) {
        playing = playBoolean;
    }
}
