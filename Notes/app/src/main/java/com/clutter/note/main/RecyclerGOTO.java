package com.clutter.note.main;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by csimcik on 11/29/2017.
 */
public class RecyclerGOTO extends RecyclerView {
    public static LinearLayoutManager linearLayoutManager;
    public RecyclerGOTO(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerGOTO(Context context) {
        super(context);
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
    }

    public RecyclerGOTO(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public boolean fling(int velocityX, int velocityY) {
            linearLayoutManager = (LinearLayoutManager) getLayoutManager();

            int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

            // views on the screen
            int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
            Log.i("this thing that i do", String.valueOf(linearLayoutManager.getChildCount()));
            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
            View firstView = linearLayoutManager.findViewByPosition(firstVisibleItemPosition);
            View lastView = linearLayoutManager.findViewByPosition(lastVisibleItemPosition);
            Log.i("xVelocity is this > ", String.valueOf(velocityX));


            // distance we need to scroll
            int leftMargin = (screenWidth - lastView.getWidth()) / 2;
            int rightMargin = (screenWidth - lastView.getWidth()) / 2 + lastView.getWidth();
            int leftEdge = lastView.getLeft();
            int rightEdge = firstView.getRight();
            int scrollDistanceLeft = leftEdge - leftMargin;
            int scrollDistanceRight = rightMargin - rightEdge;
            if (Math.abs(velocityX) < 1000) {
                // The fling is slow -> stay at the current page if we are less than half through,
                // or go to the next page if more than half through
                if (leftEdge > screenWidth / 2) {
                    // go to next page
                    MainActivity.todayObj -= 1;

                    // MainActivity.loaderManager.restartLoader(0,null,MainActivity.loaderCallbacks);
                    Log.i("aHA", String.valueOf(MainActivity.todayObj) + " " + String.valueOf(MainActivity.todayArray.size()));
                    smoothScrollToPosition(MainActivity.todayObj);
                    //  new AsyncDayEvent(MainActivity.todayObj).execute();

                } else if (rightEdge < screenWidth / 2) {
                    // go to next page
                    MainActivity.todayObj += 1;
                    //   MainActivity.loaderManager.restartLoader(0,null,MainActivity.loaderCallbacks);
                    Log.i("aHA", String.valueOf(MainActivity.todayObj + " " + String.valueOf(MainActivity.todayArray.size())));
                    smoothScrollToPosition(MainActivity.todayObj);
                    //  new AsyncDayEvent(MainActivity.todayObj).execute();
                } else {
                    // stay at current page
                    if (velocityX > 0) {
                        //smoothScrollBy(-scrollDistanceRight, 0);
                        smoothScrollToPosition(MainActivity.todayObj);
                    } else {
                        //smoothScrollBy(scrollDistanceLeft, 0);
                        smoothScrollToPosition(MainActivity.todayObj);
                    }
                }
                return true;

            } else {
                // The fling is fast -> go to next page
                if (velocityX > 0) {
                    MainActivity.todayObj += 1;
                    Log.i("aHA", String.valueOf(MainActivity.todayObj) + " " + String.valueOf(MainActivity.todayArray.size()));
                    smoothScrollToPosition(MainActivity.todayObj);
                    // new AsyncDayEvent(MainActivity.todayObj).execute();
                } else {
                    MainActivity.todayObj -= 1;
                    Log.i("aHA", String.valueOf(MainActivity.todayObj));
                    smoothScrollToPosition(MainActivity.todayObj);
                    //  new AsyncDayEvent(MainActivity.todayObj).execute();
                }
                return true;

            }

    }
}
