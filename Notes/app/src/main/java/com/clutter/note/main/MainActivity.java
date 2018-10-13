package com.clutter.note.main;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.Path;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.io.File;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
import static android.widget.NumberPicker.OnScrollListener.SCROLL_STATE_FLING;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>  {
    public static int dayPos;
    public static int yearSelected;
    public static String[] termsList;
    public static String monthSelected;
    public static String daySelected;
    public static int hilite, backgrnd, white, black, widgetSize;
    public static int dateSelected;
    public static int modeSelect = 1;
    public static Database database;
    public static DetailFrag detailFrag;
    CalendarFrag calendarFrag;
    LoadingFrag loadScreen;
    // 2D Array of days to lighten adater load
    public static ArrayList dataSet = new ArrayList();
    public static ArrayList todaysDate = new ArrayList();
    public static DayAdapter calAdapter;
    LinearLayoutManager datesLLM;
    public static DatesAdapter datesAdapter;
    public static int todayObj;
    public static int yesterdayObj;
    public static TextView dateDisplay;
    public static ViewSwitcher viewSwitcher;
    public static String todays;
    public static DayAdapterGrid gridAdapter;
    public static GridLayoutManager mainGridLayoutMan;
    public static GridLayoutManager aGridLayoutMan;
    public static LinearLayoutManager mainLinearLayoutManager;
    public static RecyclerView datesViewer;
    public static FrameLayout fragmentWindow;
    public static Boolean flower, initiated = false, viewReady = false;
    public static LoadingFrag loadingFrag;
    public TextView myText;
    public String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    public String[] weekDays = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    public Integer[] leapSelect;
    public Integer[] monthNoLeap = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    public Integer[] monthLeap = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    static public ArrayList<Day> daysL = new ArrayList();
    static public ArrayList<DBModel> datesListings = new ArrayList();
    static public ArrayList<Day> daysSlct = new ArrayList();
    static public ArrayList<String> monthsSlct = new ArrayList();
    static public ArrayList<Integer> yearsSlct = new ArrayList();
    static public ArrayList<ArrayList<Day>> monthofYear = new ArrayList<>();
    static public ArrayList<EventsModel> todayArray;
    public static FragmentManager manager;
    public static FragmentTransaction transaction;
    public static final String TAG_RETAINED_FRAGMENT = "retainedFragment";
    public static int w, tempSize;
    public static int h;
    public int firstPos;
    public int lastPos;
    public int year = 2017;
    public static LoaderManager loaderManager;
    public static LoaderManager.LoaderCallbacks loaderCallbacks;
    private Handler handler;
    public int max = 10;
    int counter = 5;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor prefEditor;
    // bool hack calendar broke
    Boolean calendarMode = false;


    public static class Day {
        String dayID;
        int dateID;
        String monthID;
        int yearID;
        int id;
        ArrayList<String> notesObj = new ArrayList<>();
        ArrayList<String> photoObj = new ArrayList<>();
        ArrayList<String> movieObj = new ArrayList<>();
        ArrayList<String> voiceObj = new ArrayList<>();
        ArrayList<String> termsObj = new ArrayList<>();
        ArrayList<String[]> allObj = new ArrayList<>();

        public Day(String dayid, int dateid, String monthid, int yearid, int id) {
            dayID = dayid;
            dateID = dateid;
            monthID = monthid;
            yearID = yearid;
            this.id = id;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        prefEditor = sharedPreferences.edit();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this.deleteDatabase(Database.DATABASE_NAME);
        // trying out 2D arrayList
        database = new Database(this);
       loaderManager = getSupportLoaderManager();
       loaderCallbacks = this;
       loaderManager.initLoader(0,null,loaderCallbacks);
        Log.i("******INIT******", " init completed");
        hilite = ContextCompat.getColor(this, R.color.colorHilite);
        backgrnd = ContextCompat.getColor(this, R.color.datefield);
        white = ContextCompat.getColor(this, R.color.whitefield);
        black = ContextCompat.getColor(this, R.color.blcktxt);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        w = metrics.widthPixels;
        h = metrics.heightPixels;
        todayArray = new ArrayList<>();
        fragmentWindow = (FrameLayout) findViewById(R.id.workspace);
        loadingFrag = new LoadingFrag();
        manager = this.getFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.workspace, loadingFrag,"loadingFragment");
        transaction.commit();
        datesLLM = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        datesViewer = (RecyclerView) findViewById(R.id.date_scroll);
        datesViewer.setLayoutManager(datesLLM);
        datesAdapter = new DatesAdapter(this,datesListings);
        initDatabase();
        todayDate();
        getToday(loaderManager,loaderCallbacks);
        dateDisplay = (TextView) findViewById(R.id.dateViewAlt);
        dateDisplay.invalidate();
        viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher3);
        mainLinearLayoutManager = new LinearLayoutManager(this);

        mainGridLayoutMan = new GridLayoutManager(this, 7);
        aGridLayoutMan = new GridLayoutManager(this, 7);
        // datesViewer.setAdapter(datesAdapter);
        if (datesViewer != null) {
            datesViewer.scrollToPosition(todayObj);
        }
        widgetSize = sharedPreferences.getInt("list_size",5);
        datesViewer.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener());
        datesViewer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                firstPos = mainLinearLayoutManager.findFirstVisibleItemPosition();
                lastPos = mainLinearLayoutManager.findLastVisibleItemPosition();
            }
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == SCROLL_STATE_FLING) {
                    Log.i("flingign", "ok");
                    todayArray.clear();
                    DetailFrag.thisAdapter.notifyDataSetChanged();
                    loaderManager.destroyLoader(0);
                }
                if (newState == SCROLL_STATE_IDLE) {
                    loaderManager.restartLoader(0,null,loaderCallbacks);
                    todayArray = database.getTodaysRecords(String.valueOf(todayObj));
                    DetailFrag.thisAdapter.notifyDataSetChanged();
                }
            }

        });

    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        CursorLoader cursorLoader = new CursorLoader(this,ContentProv.CONTENT_URI,null,null,null,null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data = getContentResolver().query(ContentProv.CONTENT_URI,null,null,null,null);
        ArrayList<EventsModel> contacts = new ArrayList<EventsModel>();
        EventsModel eventsModel;
        if (data.getCount() > 0) {
            for (int i = 0; i < data.getCount(); i++) {
                data.moveToNext();
                eventsModel = new EventsModel();
                eventsModel.setEventsID(data.getString(3));
                //Log.i("eventsid",data.getString(3));
                eventsModel.setThumb(data.getString(2));
               // Log.i("thumb",data.getString(2));
                eventsModel.setType(data.getString(0));
              //  Log.i("type",data.getString(0));
                eventsModel.setUrl(data.getString(1));
              //  Log.i("url",data.getString(1));
                contacts.add(eventsModel);

            }
        }
if(initiated && !calendarMode) {
    if (manager.findFragmentByTag("detailFragment").isVisible()) {
        detailFrag = new DetailFrag();
        manager = this.getFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.workspace, detailFrag, "detailFragment");
        transaction.commit();

    }
}
        MainActivity.todayArray = contacts;
        //DetailFrag.thisAdapter.notifyDataSetChanged();
        Log.i("data info", String.valueOf(contacts.size()));


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        manager = getFragmentManager();
               getSupportLoaderManager().initLoader(0,null,this);
        fragmentWindow.invalidate();
        if (manager != null) {
            android.app.Fragment fragment = manager.findFragmentByTag(TAG_RETAINED_FRAGMENT);
            if (fragment != null) {
                Log.i("Fragments ", fragment.toString());
                manager.beginTransaction().replace(R.id.workspace, fragment);
            } else {
                if (initiated) {
                    transaction = manager.beginTransaction();
                    DetailFrag detailFrag = new DetailFrag();
                    transaction.replace(R.id.workspace, detailFrag, "detailFragment");
                    transaction.commit();
                    Log.i("on resume ", "detail_frag_firing");
                }
            }
          //  todayArray = database.getTodaysRecords(String.valueOf(todayObj));
        }
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
                calendarMode = false;
                fragmentWindow.invalidate();
                manager = getFragmentManager();
                transaction = manager.beginTransaction();
                detailFrag = new DetailFrag();
                transaction.replace(R.id.workspace,detailFrag,"detailFragment");
                transaction.commit();
                getToday(loaderManager,loaderCallbacks);
                break;
            case R.id.yesterday:
                calendarMode = false;
                fragmentWindow.invalidate();
                manager = getFragmentManager();
                transaction = manager.beginTransaction();
                detailFrag = new DetailFrag();
                transaction.replace(R.id.workspace,detailFrag,"detailFragment");
                transaction.commit();
                getYesterday();
                 break;
            case R.id.list:
                calendarMode = true;
                manager = getFragmentManager();
                transaction = manager.beginTransaction();
                calendarFrag = new CalendarFrag();
                transaction.replace(R.id.workspace,calendarFrag,"calendarFragment");
                transaction.commit();
                getToday(loaderManager,loaderCallbacks);
                break;
            case R.id.widget_size:
                LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popup = inflater.inflate(R.layout.slider_pop_up, null, false);
                final PopupWindow popupWindow = new PopupWindow(popup,-1,300);
                popupWindow.showAtLocation(this.findViewById(R.id.mainLineaLay), Gravity.CENTER,0,0);
                SeekBar seekBar = (SeekBar) popup.findViewById(R.id.seekBar);
                seekBar.setMax(100);
                final TextView textView = (TextView) popup.findViewById(R.id.trackballval);
                textView.setText(String.valueOf(widgetSize));
                seekBar.setProgress(widgetSize);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
textView.setText(String.valueOf(i));
tempSize = i;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        widgetSize = tempSize;
                        prefEditor.putInt("list_size",widgetSize);
                        prefEditor.commit();
                        AppWidgetList.refreshWidget(getApplicationContext());
                        popupWindow.dismiss();

                    }
                });
                break;

    }

        return super.onOptionsItemSelected(item);
    }
    public boolean checkDatabase(){
        File dbFile = new File(String.valueOf(this.getDatabasePath(Database.DATABASE_NAME)));
        Log.i("fileName > ", dbFile.toString());
        return  dbFile.exists();
    }
    public static void getYesterday(){
        if( datesViewer != null){
            Log.i("yesterdayObj", " "+ String.valueOf(yesterdayObj));
            todayObj = yesterdayObj;
            datesViewer.scrollToPosition(todayObj);
            dayPos = todayObj;
        }
    }
    public static void getToday(LoaderManager loaderManager, LoaderManager.LoaderCallbacks loaderCallbacks) {
        Bundle loaderBundle = new Bundle();
        yearSelected = Integer.parseInt(todaysDate.get(5).toString());
        monthSelected = String.valueOf(todaysDate.get(1));
        daySelected = String.valueOf(todaysDate.get(0));
        dateSelected = Integer.parseInt(todaysDate.get(2).toString());
        for (int i = 0; i < daysL.size(); i++) {
            if (daysL.get(i).monthID.equals(monthSelected) && daysL.get(i).dayID.equals(daySelected) && daysL.get(i).yearID == yearSelected && daysL.get(i).dateID == dateSelected) {
                todayObj = i;
                loaderBundle.putString("mnth",daysL.get(i).monthID);
                loaderBundle.putString("day",daysL.get(i).dayID);
                loaderBundle.putString("year",String.valueOf(daysL.get(i).yearID));
                loaderBundle.putString("date",String.valueOf(daysL.get(i).dateID));
                loaderManager.restartLoader(0,loaderBundle,loaderCallbacks);
                yesterdayObj = i-1;
                todayArray = database.getTodaysRecords(String.valueOf(i));
                break;
            }
        }
       if( datesViewer != null){
           datesViewer.scrollToPosition(todayObj);
           dayPos = todayObj;
       }
        Log.i("today object > ", String.valueOf(todayObj));
    }
    public static void todayDate(){
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
              //  Log.i("THIS ** ", monthofYear.get(i).get(j).monthID + " " +  monthofYear.get(i).get(j).dayID + " " + String.valueOf(monthofYear.get(i).get(j).dateID));
            }
        }
    }
    // grab data from database and put it into daysL arraylist
    public ArrayList<Day> convertLists(ArrayList<DBModel> dbMod){
 ArrayList<Day> arrayList = new ArrayList<>();
        for( int i = 0; i < dbMod.size(); i++){
            arrayList.add(new Day(

                    dbMod.get(i).getDay(),
                    Integer.parseInt(dbMod.get(i).getDate()),
                    dbMod.get(i).getMonth(),
                            Integer.parseInt(dbMod.get(i).getYear()),
                                    Integer.parseInt(dbMod.get(i).getID()))
            );

        }
        return arrayList;
    }
    public static void updateMain(Boolean check){
        flower = check;
        if(flower){
            //dateDisplay.setVisibility(View.INVISIBLE);
            //datesViewer.setVisibility(View.VISIBLE);
            datesViewer.invalidate();
            dateDisplay.invalidate();
            viewSwitcher.showNext();
        }else{
            dateDisplay.setTextSize(32);
            String myString = daysL.get(todayObj).dayID +", "+ daysL.get(todayObj).monthID+" "+daysL.get(todayObj).dateID+" "+ daysL.get(todayObj).yearID;
            dateDisplay.setText(myString);
            //datesViewer.setVisibility(View.INVISIBLE);
            //dateDisplay.setVisibility(View.VISIBLE);
            datesViewer.invalidate();
            dateDisplay.invalidate();
            viewSwitcher.showNext();
        }
    }
@Override
    public void onBackPressed(){
    updateMain(true);
    // update view to reflect submissions
    DetailFrag.activityList.invalidate();
    super.onBackPressed();
}
    public void initDatabase(){
        // check if there is a dtabase
        if (!checkDatabase()) {
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
                        daysL.add(new Day(weekDays[counter], k + 1, months[j], year, i));
                    }
                }
                year += 1;
            }
            int prevDay = 0;
            int dec = 31;
            int initYear = daysL.get(0).yearID - 1;
            while (!daysL.get(0).dayID.equals("Mon")) {

                for (int i = 0; i < weekDays.length; i++) {
                    if (weekDays[i].equals(daysL.get(0).dayID)) {
                        prevDay = i - 1;
                        break;
                    }
                }
                daysL.add(0, new Day(weekDays[prevDay], dec, months[months.length - 1], initYear, daysL.size() + 1));
                Log.i("PREVIOUS DAYS", String.valueOf(daysL.get(0).dateID + " " + String.valueOf(daysL.get(0).yearID)) + " " + daysL.get(0).dayID);
                dec -= 1;
            }


            Log.i("Database: db", "No database found, creating and populating new db");
           // database = new Database(this);
            final CreateRecords createRecords = new CreateRecords(daysL,this.getApplicationContext(),this);
            //createRecords.CreateRecords(daysL);
            createRecords.execute(daysL);
        } else {
            initiated = true;
          //  database = new Database(this);
            daysL = convertLists(database.getAllRecords());
            Log.i("any data",String.valueOf(daysL.size()));
            todayDate();
            Log.i("database", "database was found !");
            datesViewer.setAdapter(datesAdapter);
            datesAdapter.notifyDataSetChanged();
            datesViewer.invalidate();
            Log.i("whats up datesAdapter",String.valueOf(datesAdapter.datesList));
        }


        datesListings = database.getAllRecords();
        Log.i("number of listings",String.valueOf(datesListings.size()));


    }
    @Override
    public void onPause(){
        super.onPause();
        AppWidgetList.refreshWidget(this);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        AppWidgetList.refreshWidget(this);
    }
    private String getStudents(int classID) {
        String student = null;
        Cursor c = getContentResolver().query(
                ContentProv.CONTENT_URI,null,null,null,null);
        if(c != null) {
            if(c.moveToFirst()) {
                int i=0;
                do {
                    String firstName = c.getString(c.getColumnIndex(ContentProv.CONTENT_URI.toString()));
                    student = firstName;
                    i++;
                } while (c.moveToNext());
            }
            c.close();
        }
        return student;
    }

}




