package com.clutter.note.main;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

/**
 * Created by csimcik on 10/16/2017.
 */
public class NoteFrag extends Fragment {
    String notes,title,lastVal;
    int prev;
    CircleText noteSubmit;
    SubmitView submitL;
    SubmitViewR submitR;
    TextView titularView;
    TextWatcher titleWatcher;
    EditText noteEditor;
    EditText noteEdit;
    boolean edited = false;
    boolean titleActive = false;
    String[] txtArray;
    int itsPos;
    ViewSwitcher titleSwitch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if(bundle != null){
            notes = bundle.getString("url");
            title = bundle.getString("thumb");
            titleActive = true;
            txtArray = new String[1];
            txtArray[0] = notes;
            edited = bundle.getBoolean("edit");
        }

    }
    @Override
    public void onPause(){
        super.onPause();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View view = inflater.inflate(R.layout.note_view,container,false);
        titularView = (TextView)view.findViewById(R.id.title_note);
        final Boolean canUpdate = true;
        if(title != null){
            titularView.setText(title);
        }
        titleSwitch = (ViewSwitcher)view.findViewById(R.id.viewSwitcher);
titularView.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if( s.length() > 1){
            titleActive = true;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
});
        noteEditor = (EditText)view.findViewById(R.id.note_composer);
        noteEditor.setText(notes);
        noteEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("click reg", "this is clicked!");
                if( ! String.valueOf(titularView.getText().charAt(titularView.getText().length()-1)).equals(".")) {
                    if (titularView.getText().length() <= 17) {
                        title = String.valueOf(titularView.getText().charAt(0)).toUpperCase() + titularView.getText().subSequence(1, titularView.getText().length()) + "...";
                    } else {
                        title = String.valueOf(titularView.getText().charAt(0)).toUpperCase() + titularView.getText().subSequence(1, 17) + "...";
                    }
                    titularView.setText(title);
                }

            }
        });
        noteEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 20 && s.length() >= 1) {
                    // String.valueOf(s.charAt(0)).toUpperCase();
                    titularView.setText(s.subSequence(0, 1).toString().toUpperCase() + s.subSequence(1, s.length()));
                } else if (s.length() < 1) {
                    titularView.setText("TITLE");
                } else if (s.length() >= 20) {
                    titularView.setText(s.subSequence(0, 1).toString().toUpperCase() + s.subSequence(1, 17) + "...");
                }
                Log.i("title situ. > ", titularView.getText() + " " + title + " " + titleActive);
            }



            @Override
            public void afterTextChanged(Editable s) {

            }


        });
        PropertyValuesHolder xScale = PropertyValuesHolder.ofFloat("scaleX",.85f);
        PropertyValuesHolder yScale = PropertyValuesHolder.ofFloat("scaleY",.85f);
        submitL = (SubmitView)view.findViewById(R.id.submit_note);
        submitR = (SubmitViewR)view.findViewById(R.id.submit_b_note);
        noteSubmit =(CircleText)view.findViewById(R.id.submit_button);
        noteSubmit.setStrokeColor("#808080");
        noteSubmit.setStrokeWidth(1);
        noteSubmit.setSolidColor("#fafafa");
        noteSubmit.setGravity(Gravity.CENTER);
        noteSubmit.setText("Submit");
        final ValueAnimator valueAnimator = ValueAnimator.ofInt(noteSubmit.getLayoutParams().width,30).setDuration(125);
        final ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(noteSubmit,xScale,yScale);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value =  (int) animation.getAnimatedValue();
                submitL.getLayoutParams().width = value;
                submitR.getLayoutParams().width = value;
                Log.i("animator val > ", String.valueOf(animation.getAnimatedValue()));
            }
        });
        noteSubmit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final View viewed = v;
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if( ! String.valueOf(titularView.getText().charAt(titularView.getText().length()-1)).equals(".")) {
                        if (titularView.getText().length() <= 20) {
                            title = String.valueOf(titularView.getText().charAt(0)).toUpperCase() + titularView.getText().subSequence(1, titularView.getText().length()) + "...";
                        } else {
                            title = String.valueOf(titularView.getText().charAt(0)).toUpperCase() + titularView.getText().subSequence(1, 17) + "...";
                        }
                    }
                   //titularView.setText(title);
                    objectAnimator.setDuration(125).start();
                objectAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                            MainActivity.daysL.get(MainActivity.todayObj).notesObj.add(noteEditor.getText().toString());
                            String[] tempString = new String[2];
                            itsPos = MainActivity.todayObj;
                            tempString[0] = "N";
                            tempString[1] = titularView.getText().toString();
                            EventsModel eventsModel = new EventsModel();
                            eventsModel.setEventsID(String.valueOf(itsPos));
                            eventsModel.setType("N");
                            eventsModel.setThumb(titularView.getText().toString());
                            eventsModel.setUrl(noteEditor.getText().toString());
                            Log.i("this is the VAL", eventsModel.getUrl());
                            if (!edited) {
                                if(!titularView.getText().toString().equals(getResources().getString(R.string.title))) {
                                    MainActivity.database.insertRecordB(eventsModel);
                                    MainActivity.daysL.get(MainActivity.todayObj).allObj.add(tempString);
                                }
                            } else {
                                MainActivity.database.updateRecordB(eventsModel, txtArray);
                                MainActivity.daysL.get(MainActivity.todayObj).allObj.add(tempString);
                            }

                        DetailFrag detailFrag = new DetailFrag();
                        MainActivity.transaction.replace(MainActivity.fragmentWindow.getId(),detailFrag);
                        InputMethodManager imm = (InputMethodManager)viewed.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(MainActivity.fragmentWindow.getWindowToken(), 0);
                        getFragmentManager().popBackStack();
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


}
