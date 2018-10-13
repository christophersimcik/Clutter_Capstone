package com.clutter.note.main;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.LinearLayoutCompat;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by csimcik on 10/4/2017.
 */
public class GridAdapterMonth extends BaseAdapter {
    private final Context mContext;
    private final String myMonth;
    private final int monthPos;
    private ArrayList<MainActivity.Day> thisList = new ArrayList<>();

    // 1
    public GridAdapterMonth(Context context, ArrayList<MainActivity.Day> aList, String mnth, int monthPos) {
        this.mContext = context;
        this.thisList = aList;
        this.myMonth = mnth;
        this.monthPos = monthPos;
    }

    @Override
    public int getCount() {
        return 7 * 6;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final CircleText theView = new CircleText(mContext);
        FrameLayout frameLayout = new FrameLayout(mContext);
        DataMarkerView dataMarkerView = new DataMarkerView(mContext,position,monthPos);
        dataMarkerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT));
        final TextView aView = new TextView(mContext);
        frameLayout.addView(aView);
        frameLayout.addView(dataMarkerView);
        dataMarkerView.setData();
        if (thisList.get(position).dateID == MainActivity.daysL.get(MainActivity.todayObj).dateID && thisList.get(position).monthID.equals(MainActivity.daysL.get(MainActivity.todayObj).monthID) && thisList.get(position).yearID == (MainActivity.daysL.get(MainActivity.todayObj).yearID)) {
            theView.setWidth(MainActivity.w/7);
            theView.setHeight(MainActivity.w/7);
            if(myMonth.equals(MainActivity.daysL.get(MainActivity.todayObj).monthID)) {
                theView.setSolidColor("#80bdff");
                Log.i("Month: ", CalendarFrag.mnthFld + " My Month: " + thisList.get(position).monthID );
                theView.setTextColor(Color.parseColor("#ffffff"));
            }else{
                theView.setSolidColor("#f2f3f4");
                theView.setTextColor(Color.parseColor("#808080"));
            }
            theView.setText(String.valueOf(thisList.get(position).dateID));
            theView.setGravity(Gravity.CENTER);
            theView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ValueAnimator valueAnimator =  ValueAnimator.ofFloat(0.0f,1.0f).setDuration(125);
                    final ValueAnimator valueAnimatorText =  ValueAnimator.ofFloat(14f,26f).setDuration(125);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float fractionAnim = (float) valueAnimator.getAnimatedValue();
                            theView.setIntColor(ColorUtils.blendARGB(Color.parseColor("#80bdff")
                                    , Color.parseColor("#e34f00")
                                    , fractionAnim));
                        }
                    });
                    Log.i("this is today > ", String.valueOf(theView.getText()));
                    valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
                    valueAnimator.setRepeatCount(1);
                    valueAnimator.start();
                    valueAnimator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            MainActivity.transaction = MainActivity.manager.beginTransaction();
                            DetailFrag detailFrag = new DetailFrag();
                            MainActivity.transaction.replace(R.id.workspace,detailFrag);
                            MainActivity.transaction.commit();
                            Log.i("gridAdapterMonth","117");
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    for (int i = 0; i < MainActivity.daysL.size(); i++) {
                        if (MainActivity.daysL.get(i).monthID.equals(String.valueOf(thisList.get(position).dayID)) && MainActivity.daysL.get(i).dayID.equals(String.valueOf(thisList.get(position).dayID)) && String.valueOf(MainActivity.daysL.get(i).yearID).equals(String.valueOf(thisList.get(position).yearID)) && String.valueOf(MainActivity.daysL.get(i).dateID).equals(String.valueOf(thisList.get(position).dateID))) {
                            MainActivity.todayObj = i;
                            MainActivity.datesViewer.scrollToPosition(i);
                            break;
                        }
                    }

                }
            });
            return theView;
        } else {
            aView.setWidth(MainActivity.w / 7);
            aView.setHeight(MainActivity.w / 7);
            aView.setGravity(Gravity.CENTER);
            if (String.valueOf(thisList.get(position).monthID).equals(myMonth)) {
                aView.setTextColor(Color.parseColor("#554b4b"));
            } else {
                aView.setTextColor(Color.parseColor("#e6e1e1"));
            }
            aView.setText(String.valueOf(thisList.get(position).dateID));
            aView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    aView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final int colorInt = aView.getSolidColor();
                            Log.i("this is a color",String.valueOf(colorInt));
                          final ValueAnimator valueAnimator =  ValueAnimator.ofFloat(0.0f,1.0f).setDuration(125);
                            final ValueAnimator valueAnimatorText =  ValueAnimator.ofFloat(14f,26f).setDuration(125);
                            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    float fractionAnim = (float) valueAnimator.getAnimatedValue();
                                    aView.setTextColor(ColorUtils.blendARGB(Color.parseColor("#1a0000")
                                            , Color.parseColor("#e34f00")
                                            , fractionAnim));
                                }
                            });
                            valueAnimatorText.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    aView.setTextSize((Float) valueAnimatorText.getAnimatedValue());
                                }
                            });
                            valueAnimator.addListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    MainActivity.transaction = MainActivity.manager.beginTransaction();
                                    DetailFrag detailFrag = new DetailFrag();
                                    MainActivity.transaction.replace(R.id.workspace,detailFrag);
                                    MainActivity.transaction.commit();
                                    Log.i("gridAdapterMonth","188");
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });
                            valueAnimatorText.setRepeatMode(ValueAnimator.REVERSE);
                            valueAnimatorText.setRepeatCount(1);
                            valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
                            valueAnimator.setRepeatCount(1);
                            valueAnimator.start();
                            valueAnimatorText.start();
                            for (int i = 0; i < MainActivity.daysL.size(); i++) {
                                if (MainActivity.daysL.get(i).monthID.equals(String.valueOf(thisList.get(position).monthID)) && MainActivity.daysL.get(i).dayID.equals(String.valueOf(thisList.get(position).dayID)) && String.valueOf(MainActivity.daysL.get(i).yearID).equals(String.valueOf(thisList.get(position).yearID)) && String.valueOf(MainActivity.daysL.get(i).dateID).equals(String.valueOf(thisList.get(position).dateID))) {
                                    MainActivity.todayObj = i;
                                    MainActivity.datesViewer.scrollToPosition(i);
                                    break;
                                }
                            }

                        }
                    });
                }
            });
            //return aView;
            return frameLayout;
        }


    }

}
