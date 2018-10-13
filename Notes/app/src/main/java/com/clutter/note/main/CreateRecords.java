package com.clutter.note.main;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by csimcik on 12/1/2017.
 */
public class CreateRecords extends AsyncTask<ArrayList,Void,String> {
    ArrayList<MainActivity.Day> dayList;
    Context mContext;
    Activity mActivity;

    public CreateRecords(ArrayList<MainActivity.Day> mdayList,Context context,Activity activity) {
        this.dayList = mdayList;
        this.mContext = context;

    }

    @Override
    protected String doInBackground(ArrayList... arrayLists) {
        dayList = arrayLists[0];
        Log.i("creating recs", "im making them");
        for (int i = 0; i < dayList.size(); i++) {
            DBModel dbModel = new DBModel();
            dbModel.setYear(String.valueOf(MainActivity.daysL.get(i).yearID));
            dbModel.setMonth(MainActivity.daysL.get(i).monthID);
            dbModel.setDate(String.valueOf(MainActivity.daysL.get(i).dateID));
            dbModel.setDay(String.valueOf(MainActivity.daysL.get(i).dayID));
            MainActivity.database.insertRecord(dbModel);
            Log.i("value of i", String.valueOf(i));
          /*  EventsModel eventsModel =  new EventsModel();
            eventsModel.setType("B");
            eventsModel.setEventsID(String.valueOf(i+1));
            MainActivity.database.insertRecordB(eventsModel);
            */
        }
        String myString = "test";

        return myString;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("it fired", s);
        //ensure smooth trans
        MainActivity.loadingFrag.setColor();
        // set Runnable flag to false
        MainActivity.loadingFrag.quitSurface(false);
        // init detailfrag
        DetailFrag detailFrag = new DetailFrag();
        LoadingFrag loadingFrag = (LoadingFrag) MainActivity.manager.findFragmentByTag("loadingFragment");
        MainActivity.transaction = MainActivity.manager.beginTransaction();
        MainActivity.transaction.replace(R.id.workspace,detailFrag,"detailFragment");
        MainActivity.transaction.commit();
        MainActivity.todayDate();
        MainActivity.datesListings = MainActivity.database.getAllRecords();
        Log.i("dates adapt = ", String.valueOf(MainActivity.datesListings.size()));
        MainActivity.datesAdapter.notifyDataSetChanged();
        MainActivity.getToday(MainActivity.loaderManager,MainActivity.loaderCallbacks);
        Log.i("dates adapt = ", String.valueOf(MainActivity.datesAdapter.getItemCount()));
       MainActivity.datesViewer.setAdapter(MainActivity.datesAdapter);


    }
}
