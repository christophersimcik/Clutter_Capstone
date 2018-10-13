package com.clutter.note.main;

import android.app.Fragment;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by csimcik on 11/27/2017.
 */
public class CalendarFrag extends Fragment {
    public static DayAdapterGrid gridAdapter;
    public static GridLayoutManager mainGridLayoutMan;
    public static GridLayoutManager aGridLayoutMan;
    public static LinearLayoutManager mainLinearLayoutManager;
    public static TextView mnthFld;
    public static TextView yrFld;
    public static RecyclerView gridView;
    public int first,last;
    public static int dominantPos;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grid_main,container, false);
        gridView = (RecyclerView)view.findViewById(R.id.gridcal);
        mainGridLayoutMan = new GridLayoutManager(getActivity(),7);
        aGridLayoutMan = new GridLayoutManager(getActivity(),7);
        mainLinearLayoutManager = new LinearLayoutManager(getActivity());
        gridView.setLayoutManager(mainLinearLayoutManager);
        gridView.setHasFixedSize(true);
        gridAdapter = new DayAdapterGrid(getActivity());
        gridView.setAdapter(gridAdapter);
        gridAdapter.hasStableIds();
        mnthFld = (TextView)view.findViewById(R.id.monthfield);
        yrFld = (TextView)view.findViewById(R.id.yearfield);
        gridAdapter.notifyDataSetChanged();
        for(int i = 0; i < MainActivity.monthofYear.size(); i ++){
            if(MainActivity.todaysDate.get(1).equals(MainActivity.monthofYear.get(i).get(15).monthID) && Integer.parseInt(MainActivity.todaysDate.get(5).toString()) == (MainActivity.monthofYear.get(i).get(15).yearID)){
                gridView.scrollToPosition(i);
                Log.i("MainActivty_todaydate",String.valueOf(MainActivity.todaysDate.get(1)));
                Log.i("monthofYear",String.valueOf(MainActivity.monthofYear.get(i).get(15).monthID));
                Log.i("MainActivty.todaydate",String.valueOf(MainActivity.todaysDate.get(5)));
                Log.i("MainActivty.todaydate",String.valueOf(MainActivity.monthofYear.get(i).get(15).yearID));
                break;
            }

        }


        gridView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                first = mainLinearLayoutManager.findFirstVisibleItemPosition();
                last = mainLinearLayoutManager.findLastVisibleItemPosition();
                Rect firstRect = new Rect(), lastRect = new Rect(), mainRect = new Rect();
                gridView.getGlobalVisibleRect(mainRect);
                int firstPercent, secondPercent;
                mainLinearLayoutManager.findViewByPosition(first).getGlobalVisibleRect(firstRect);
                mainLinearLayoutManager.findViewByPosition(last).getGlobalVisibleRect(lastRect);
                firstPercent = (int)(((double)firstRect.height())/(double)(mainRect.height())*100);
                secondPercent = (int)(((double)lastRect.height())/(double)(mainRect.height())*100);
                if(firstPercent > secondPercent){
                    dominantPos = first;
                }else{
                    dominantPos = last;
                }
                mnthFld.setText(MainActivity.monthofYear.get(dominantPos).get(15).monthID);
                Log.i("made it ","mnthFld");
                yrFld.setText(String.valueOf(MainActivity.monthofYear.get(dominantPos).get(15).yearID));
                Log.i("made it ","yrFld");
            }


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

        });


        return view;
    }

}
