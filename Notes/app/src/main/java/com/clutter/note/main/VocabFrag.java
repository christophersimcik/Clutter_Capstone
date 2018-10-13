package com.clutter.note.main;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by csimcik on 10/21/2017.
 */
public class VocabFrag extends Fragment {
    TextView termDisplay;
    EditText termEditField;
    CircleText termSubmit;
    SubmitView submitView;
    SubmitViewR submitViewR;
    TextWatcher textWatcher;
    ImageView googleBooks;
    static String[] keyList;
    int itsPos,letterCount, preCount = 0;
    Boolean backspaced = false, oneLetter = false, gBooks = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        keyList = new String[]{"a","b","c","d","e"};
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onPause(){
        super.onPause();
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.term_view,container,false);
        termDisplay = (TextView) view.findViewById(R.id.term_display);
        termEditField = (EditText)view.findViewById(R.id.term_field);
        termSubmit = (CircleText) view.findViewById(R.id.submit_button);
        termEditField.setImeOptions(EditorInfo.IME_ACTION_DONE);
        googleBooks = (ImageView)view.findViewById(R.id.google_book_select);
        googleBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!gBooks) {
                    googleBooks.setImageResource(R.drawable.book_icon_select);
                    gBooks = true;
                    Toast.makeText(getActivity(),"search google books",
                            Toast.LENGTH_SHORT).show();
                }else{
                    googleBooks.setImageResource(R.drawable.book_icon_deselect);
                    gBooks = false;
                    Toast.makeText(getActivity(),"search all internet",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        termEditField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch(actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        Log.i("check edit",checkEdit().toString());
                        if(checkEdit()) {
                            MainActivity.daysL.get(MainActivity.todayObj).termsObj.add(termDisplay.getText().toString());
                            String[] tempString = new String[2];
                            tempString[1] = termDisplay.getText().toString();
                            itsPos = MainActivity.todayObj;
                            EventsModel eventsModel = new EventsModel();
                            eventsModel.setEventsID(String.valueOf(itsPos));
                            if(!gBooks) {
                                eventsModel.setType("V");
                            }else{
                                eventsModel.setType("B");
                            }

                            eventsModel.setUrl(termDisplay.getText().toString());
                            MainActivity.database.insertRecordB(eventsModel);
                            MainActivity.daysL.get(MainActivity.todayObj).allObj.add(tempString);
                        }
                        InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
                        FragmentManager manager;
                        FragmentTransaction transaction;
                        manager = getFragmentManager();
                        transaction = manager.beginTransaction();
                        DetailFrag detailFrag = new DetailFrag();
                        transaction.replace(R.id.workspace,detailFrag);
                        transaction.commit();
                        break;
                }
                return true;
            }
        });
        textWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 20 && s.length() >= 1) {
                    // String.valueOf(s.charAt(0)).toUpperCase();
                    termDisplay.setText(s.subSequence(0, 1).toString().toUpperCase() + s.subSequence(1, s.length()));
                } else if (s.length() < 1) {
                    termDisplay.setText("N/A");
                } else if (s.length() >= 20) {
                    termDisplay.setText(s.subSequence(0, 1).toString().toUpperCase() + s.subSequence(1, 19) + "...");
                }
            }


            @Override
            public void afterTextChanged(Editable s) {

                }


        };
        termEditField.addTextChangedListener(textWatcher);
        termSubmit.setStrokeColor("#808080");
        termSubmit.setStrokeWidth(1);
        termSubmit.setSolidColor("#fafafa");
        termSubmit.setGravity(Gravity.CENTER);
        termSubmit.setText("Submit");
        submitView =(SubmitView)view.findViewById(R.id.submitter);
        submitViewR=(SubmitViewR)view.findViewById(R.id.submitter_r);
        PropertyValuesHolder xScale = PropertyValuesHolder.ofFloat("scaleX",.85f);
        PropertyValuesHolder yScale = PropertyValuesHolder.ofFloat("scaleY",.85f);
        final ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(termSubmit,xScale,yScale);
        final ValueAnimator valueAnimator = ValueAnimator.ofInt(submitView.getLayoutParams().width,30).setDuration(125);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value =  (int) animation.getAnimatedValue();
                submitView.getLayoutParams().width = value;
                submitViewR.getLayoutParams().width = value;
            }
        });
        termSubmit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final View viewed = v;
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    objectAnimator.addListener(new Animator.AnimatorListener() {
                                                   @Override
                                                   public void onAnimationStart(Animator animation) {

                                                   }

                                                   @Override
                                                   public void onAnimationEnd(Animator animation) {
                                                       if (termDisplay.getText().length() > 0) {
                                                           MainActivity.daysL.get(MainActivity.todayObj).termsObj.add(termDisplay.getText().toString());
                                                           String[] tempString = new String[2];
                                                           tempString[0] = "V";
                                                           tempString[1] = termDisplay.getText().toString();
                                                           itsPos = MainActivity.todayObj;
                                                           EventsModel eventsModel = new EventsModel();
                                                           eventsModel.setEventsID(String.valueOf(itsPos));
                                                           if(!gBooks) {
                                                               eventsModel.setType("V");
                                                           }else{
                                                               eventsModel.setType("B");
                                                           }

                                                           eventsModel.setUrl(termDisplay.getText().toString());
                                                           Log.i("this is the VAL", eventsModel.getUrl());
                                                           MainActivity.database.insertRecordB(eventsModel);
                                                           MainActivity.daysL.get(MainActivity.todayObj).allObj.add(tempString);
                                                       }
                                                       InputMethodManager imm = (InputMethodManager) viewed.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                                       imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                                                       FragmentManager manager;
                                                       FragmentTransaction transaction;
                                                       manager = getFragmentManager();
                                                       transaction = manager.beginTransaction();
                                                       DetailFrag detailFrag = new DetailFrag();
                                                       transaction.replace(R.id.workspace, detailFrag);
                                                       transaction.commit();
                                                       ArrayList<VocabModel> vocabModels = new ArrayList<VocabModel>();
                                                       vocabModels = MainActivity.database.getAllRecordsC();
                                                           VocabModel vocabModel = new VocabModel();
                                                           vocabModel.setData(termDisplay.getText().toString());
                                                           MainActivity.database.insertRecordC(vocabModel);
                                                           AppWidgetList.refreshWidget(getActivity());
                                                       Log.i("vocab models",String.valueOf(MainActivity.database.getAllRecordsC().size()));
                                                    AppWidgetList.refreshWidget(getActivity());
                                                    MainActivity.viewSwitcher.showNext();
                                                   }

                                                   @Override
                                                   public void onAnimationCancel(Animator animation) {

                                                   }

                                                   @Override
                                                   public void onAnimationRepeat(Animator animation) {

                                                   }
                                               });
                    valueAnimator.start();
                    objectAnimator.setDuration(125).start();
                }
                return false;
            }

        });


return view;
    }

    public Boolean checkEdit(){
        if(termDisplay.getText().length() > 0){
            return true;
        }else{
            return false;
        }

    }
}
