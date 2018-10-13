package com.clutter.note.main;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivityNext extends AppCompatActivity {
    public int dyPrev;
    public static int yearSelected;
    public static String monthSelected;
    public static String daySelected;
    public static int hilite,backgrnd,white,black;
    public static int dateSelected;
    public static int modeSelect = 1;
    public static Database database;
    DetailFrag detailFrag;
    CalendarFrag calendarFrag;
    ObjectAnimator objectAnimator;
    ObjectAnimator animator;
    // 2D Array of days to lighten adater load
    public static ArrayList<Day> holdingcache;
    public static ArrayList dataSet = new ArrayList();
    public static ArrayList todaysDate = new ArrayList();
    public static DayAdapter calAdapter;
    public static TextView tryView;
    public static Day todayObj;
    public static Day yesterdayObj;
    public static TextView mnthFld;
    public static TextView yrFld;
    public String todays;
    public static DayAdapterGrid gridAdapter;
    public static GridLayoutManager mainGridLayoutMan;
    public static GridLayoutManager aGridLayoutMan;
    public static LinearLayoutManager mainLinearLayoutManager;
    public static RecyclerView calView;
    public static FrameLayout fragmentWindow;
    public TextView myText;
    public String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
   // public String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    public String[] weekDays = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    public Integer[] leapSelect;
    public Integer[] monthNoLeap = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    public Integer[] monthLeap = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    static public ArrayList<Day> daysL = new ArrayList();
    static public ArrayList<Day> daysSlct = new ArrayList();
    static public ArrayList<String> monthsSlct = new ArrayList();
    static public ArrayList<Integer> yearsSlct = new ArrayList();
    static public ArrayList<ArrayList<Day>> monthofYear = new ArrayList<>();
    public static FragmentManager manager;
    public static FragmentTransaction transaction;
    PropertyValuesHolder pvhX;
    PropertyValuesHolder pvhY;
    PropertyValuesHolder pvhA;
    public static int w;
    public static int h;
    public int firstPos;
    public int lastPos;
    static int cntrPos;
    //scroll to pos marker
    public int posMark = 0;
    public int posMarkprev = 0;
    public int year = 2017;
    public int max = 10;
    int counter = 5;
    public static class Day {
        String dayID;
        int dateID;
        String monthID;
        int yearID;
        ArrayList<String> notesObj = new ArrayList<>();
        ArrayList<String> photoObj = new ArrayList<>();
        ArrayList<String> movieObj = new ArrayList<>();
        ArrayList<String> voiceObj = new ArrayList<>();
        ArrayList<String> termsObj = new ArrayList<>();
        ArrayList<String[]> allObj = new ArrayList<>();
        public Day(String dayid, int dateid, String monthid, int yearid, long id ) {
            dayID = dayid;
            dateID = dateid;
            monthID = monthid;
            yearID = yearid;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new Database(this);
        // trying out 2D arrayList
        for(int i = 0; i < daysL.size(); i++){

        }

        hilite = ContextCompat.getColor(this, R.color.colorHilite);
        backgrnd = ContextCompat.getColor(this, R.color.datefield);
        white = ContextCompat.getColor(this, R.color.whitefield);
        black = ContextCompat.getColor(this, R.color.blcktxt);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        w = metrics.widthPixels;
        h = metrics.heightPixels;
        setContentView(R.layout.activity_main);
        fragmentWindow = (FrameLayout)findViewById(R.id.workspace);
        for (int i = 2017; i < year + max; i++) {
            yearsSlct.add(i);
        }
        for (int i = 0; i < months.length; i++) {
            monthsSlct.add(months[i]);
        }
        for (int i = 0; i < max; i++) {
            if (year % 4 == 0) {
                leapSelect = monthLeap;
            } else {
                leapSelect = monthNoLeap;
            }
            for (int j = 0; j < months.length; j++) {

                for (int k = 0; k < leapSelect[j]; k++) {
                    counter++;
                    if (counter > 6) {
                        counter = 0;
                    }
                    daysL.add(new Day(weekDays[counter], k + 1, months[j], year,i));
                }
            }
            year += 1;
        }
        int prevDay = 0;
        int dec = 31;
        int initYear = daysL.get(0).yearID-1;
        while (! daysL.get(0).dayID.equals("Mon")) {

            for (int i = 0; i < weekDays.length; i++) {
                if (weekDays[i].equals(daysL.get(0).dayID)) {
                    prevDay = i - 1;
                    break;
                }
            }
                daysL.add(0, new Day(weekDays[prevDay], dec, months[months.length - 1],initYear,daysL.size()+1));
                Log.i("PREVIOUS DAYS", String.valueOf(daysL.get(0).dateID + " " + String.valueOf(daysL.get(0).yearID))+ " "+ daysL.get(0).dayID);
                dec -= 1;
            }
            todays = String.valueOf(Calendar.getInstance().getTime());
            StringBuilder myString = new StringBuilder();
            //iterate through date and build array of date comps.
            for (int i = 0; i < todays.length(); i++) {
                if (i + 1 >= todays.length()) {
                    myString.append(todays.charAt(i));
                    todaysDate.add(myString.toString());
                }
                if (todays.charAt(i) != ' ') {
                    myString.append(todays.charAt(i));
                } else {
                    todaysDate.add(myString.toString());
                    myString = new StringBuilder();
                }
            }
            for (int i = 0; i < todaysDate.size(); i++) {
                Log.i("this is the Date", " " + todaysDate.toString());
            }
        int span = 42;
        int arrayCntr = 0;
        int monthsReset = 0;
        monthofYear.add(new ArrayList<Day>());
        int var = 0;
        int varSwitch = 0;
        while(var < daysL.size()-1) {
        if(daysL.get(var).monthID.equals(daysL.get(var+1).monthID)){
            monthofYear.get(arrayCntr).add(daysL.get(var));

        }else{
            monthofYear.get(arrayCntr).add(daysL.get(var));
            int arraySizer = monthofYear.get(arrayCntr).size();
            if(! monthofYear.get(arrayCntr).get(0).dayID.equals("Mon")){
                for(int i = 0; i < 7; i++){
                    monthofYear.get(arrayCntr).add(0,daysL.get(var - (arraySizer+i)));

                    if(daysL.get(var - (arraySizer+i)).dayID.equals("Mon")){
                        break;

                    }
                }
            }
            int tempCnt = 1;
           for(int j= monthofYear.get(arrayCntr).size(); j < 42; j++){
               if(var+j < daysL.size()){
                   monthofYear.get(arrayCntr).add(daysL.get(var + tempCnt));
               }
               tempCnt +=1;
           }
            arrayCntr +=1;
            monthofYear.add(new ArrayList<Day>());
        }
            var ++;
        }

        for(int i =0 ; i< monthofYear.size(); i++){
           for(int j = 0; j < monthofYear.get(i).size(); j ++){
               Log.i("THIS ** ", monthofYear.get(i).get(j).monthID + " " +  monthofYear.get(i).get(j).dayID + " " + String.valueOf(monthofYear.get(i).get(j).dateID));
           }
            Log.i("", "*************************************************************");
        }
        calView = (RecyclerView) findViewById(R.id.calendarList);
        tryView =(TextView) findViewById(R.id.experiemental);
        pvhX = PropertyValuesHolder.ofFloat("scaleX",.98f);
        pvhY = PropertyValuesHolder.ofFloat("scaleY",.98f);
        pvhA = PropertyValuesHolder.ofFloat("alpha",0.25f);
        animator = ObjectAnimator.ofPropertyValuesHolder(tryView, pvhX, pvhY, pvhA);
        animator.setRepeatMode(ObjectAnimator.REVERSE);
        animator.setRepeatCount(1);
        tryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animator.setDuration(250).start();
                for (int i = 0; i < MainActivityNext.monthofYear.size(); i++) {
                    if (CalendarFrag.gridView != null) {
                        if (todaysDate.get(1).equals(MainActivityNext.monthofYear.get(i).get(15).monthID) && Integer.parseInt(MainActivityNext.todaysDate.get(5).toString()) == (MainActivityNext.monthofYear.get(i).get(15).yearID)) {
                            CalendarFrag.gridView.scrollToPosition(i);
                            break;
                        }
                    }
                }
            }
        });

        getToday();
        mainLinearLayoutManager = new LinearLayoutManager(this);
        mainGridLayoutMan = new GridLayoutManager(this, 7);
        aGridLayoutMan = new GridLayoutManager(this, 7);
        calView.setLayoutManager(mainGridLayoutMan);
        calAdapter = new DayAdapter(this);
        calView.setAdapter(calAdapter);
        calView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener());
        calView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    firstPos = mainLinearLayoutManager.findFirstVisibleItemPosition();
                    lastPos = mainLinearLayoutManager.findLastVisibleItemPosition();
                    cntrPos = firstPos + (Math.abs(firstPos - lastPos) / 2);
                    calAdapter.notifyDataSetChanged();
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                }

            });


        }
    public void setupList(){

        calView = (RecyclerView) findViewById(R.id.calendarList);
        getToday();
        mainLinearLayoutManager = new LinearLayoutManager(this);
        mainGridLayoutMan = new GridLayoutManager(this,7);
        aGridLayoutMan = new GridLayoutManager(this,7);
        //calView.setLayoutManager(mainLinearLayoutManager);
        calView.setLayoutManager(mainGridLayoutMan);
        calAdapter = new DayAdapter(this);
        calView.setAdapter(calAdapter);
        calView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener());
        calView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                firstPos = mainLinearLayoutManager.findFirstVisibleItemPosition();
                lastPos = mainLinearLayoutManager.findLastVisibleItemPosition();
                cntrPos = firstPos + (Math.abs(firstPos - lastPos) / 2);
                calAdapter.notifyDataSetChanged();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        DetailFrag detailFrag = new DetailFrag();
        transaction.add(R.id.workspace,detailFrag);
        transaction.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);//Menu Resource, Menu

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.today:
                fragmentWindow.invalidate();
                manager = getFragmentManager();
                transaction = manager.beginTransaction();
                detailFrag = new DetailFrag();
                transaction.replace(R.id.workspace,detailFrag);
                transaction.commit();
                getToday();
                break;
            case R.id.yesterday:
                manager = getFragmentManager();
                transaction = manager.beginTransaction();
                detailFrag = new DetailFrag();
                transaction.replace(R.id.workspace,detailFrag);
                transaction.commit();
                getYesterday();
                break;
            case R.id.list:
                manager = getFragmentManager();
                transaction = manager.beginTransaction();
                calendarFrag = new CalendarFrag();
                transaction.replace(R.id.workspace,calendarFrag);
                transaction.commit();
                getToday();
        }

        return super.onOptionsItemSelected(item);
    }
public static void findList(int yr, String mnth ){
    for(int i = 0; i < daysL.size(); i++){
        if(daysL.get(i).yearID == yr && daysL.get(i).monthID.equals(mnth)){
            daysSlct.add(daysL.get(i));
        }
    }

}
    public static void getYesterday(){
        yearSelected = Integer.parseInt(String.valueOf(yesterdayObj.yearID));
        monthSelected = yesterdayObj.monthID;
        daySelected = String.valueOf(yesterdayObj.dayID);
        dateSelected = yesterdayObj.dateID;
        if (todayObj != null) {
            tryView.setTextSize(24);
            tryView.setText(yesterdayObj.dayID+", "+yesterdayObj.monthID+" "+yesterdayObj.dateID+" "+yesterdayObj.yearID);

        }
        if (todayObj != null) {
            Log.i("Today's Date: ", todayObj.dayID+" "+todayObj.dateID+" "+todayObj.monthID+" "+todayObj.yearID);
            Log.i("Yesterday's Date: ", yesterdayObj.dayID+" "+yesterdayObj.dateID+" "+yesterdayObj.monthID+" "+yesterdayObj.yearID);
        }
    }
    public static void getToday() {
        yearSelected = Integer.parseInt(todaysDate.get(5).toString());
        monthSelected = String.valueOf(todaysDate.get(1));
        daySelected = String.valueOf(todaysDate.get(0));
        dateSelected = Integer.parseInt(todaysDate.get(2).toString());
        for (int i = 0; i < daysL.size(); i++) {
            if (daysL.get(i).monthID.equals(monthSelected) && daysL.get(i).dayID.equals(daySelected) && daysL.get(i).yearID == yearSelected && daysL.get(i).dateID == dateSelected) {
                todayObj = daysL.get(i);
                yesterdayObj = daysL.get(i - 1);
            }
        }
        if (todayObj != null) {
            tryView.setTextSize(24);
            tryView.setText(todayObj.dayID+", "+todayObj.monthID+" "+todayObj.dateID+" "+todayObj.yearID);

        }
        if (todayObj != null) {
            Log.i("Today's Date: ", todayObj.dayID+" "+todayObj.dateID+" "+todayObj.monthID+" "+todayObj.yearID);
            Log.i("Yesterday's Date: ", yesterdayObj.dayID+" "+yesterdayObj.dateID+" "+yesterdayObj.monthID+" "+yesterdayObj.yearID);
        }

    }

}




