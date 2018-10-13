package com.clutter.note.main;
import android.Manifest;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by csimcik on 9/9/2017.
 */
public class DayAdapterGrid extends RecyclerView.Adapter <DayAdapterGrid.ViewHolder> {
    Context context;
    public static int yrMnth;


    public DayAdapterGrid(Context mContextA) {
        context = mContextA;

    }

    @Override
    public DayAdapterGrid.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.calendar_list_view_grid_next, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(DayAdapterGrid.ViewHolder holder, int position) {
        int todaysDate = MainActivity.monthofYear.get(position).get(15).yearID;
        String myMonth = MainActivity.monthofYear.get(position).get(15).monthID;
        holder.setThisAdapter(position,myMonth);
        holder.setMonth(myMonth);
        holder.setYear(String.valueOf(todaysDate));
    }

    @Override
    public int getItemCount() {
      return MainActivity.monthofYear.size();
    }


    //View holder class, where all view components are defined
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView monthView, yearView, monV, tueV, wedV, thuV, friV, satV, sunV;
        GridView myGridView;
        int pos;


        public ViewHolder(final View itemView) {
            super(itemView);
            monthView = (TextView) itemView.findViewById(R.id.monthFld);
            yearView = (TextView) itemView.findViewById(R.id.yearFld);
            monV = (TextView) itemView.findViewById(R.id.monday);
            tueV = (TextView) itemView.findViewById(R.id.tuesday);
            wedV = (TextView) itemView.findViewById(R.id.wednesday);
            thuV =(TextView) itemView.findViewById(R.id.thursday);
            friV =(TextView) itemView.findViewById(R.id.friday);
            satV =(TextView) itemView.findViewById(R.id.saturday);
            sunV =(TextView) itemView.findViewById(R.id.sunday);
            monV.setWidth(MainActivity.w/7);
            tueV.setWidth(MainActivity.w/7);
            wedV.setWidth(MainActivity.w/7);
            thuV.setWidth(MainActivity.w/7);
            friV.setWidth(MainActivity.w/7);
            satV.setWidth(MainActivity.w/7);
            sunV.setWidth(MainActivity.w/7);
            myGridView = (GridView) itemView.findViewById(R.id.gridView);
            myGridView.setMinimumWidth(MainActivity.w);
            myGridView.setColumnWidth(MainActivity.w/7);
            itemView.setOnClickListener(this);
            itemView.setOnTouchListener(new View.OnTouchListener(){

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                if(event.getActionMasked() == MotionEvent.ACTION_DOWN){
                    {

                  }
                }
                    if(event.getActionMasked() == MotionEvent.ACTION_UP){
                        v.setVisibility(View.VISIBLE);
                    }
                    return false;
                }
            });
            itemView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                        int screenPos = CalendarFrag.mainLinearLayoutManager.findLastVisibleItemPosition();
                        String mnthDisplayed = MainActivity.monthofYear.get(screenPos).get(15).monthID;
                        String yearDisplayed = String.valueOf(MainActivity.monthofYear.get(screenPos).get(15).yearID);
                }

            });
        }




        public void setMonth(String monthS) {
            monthView.setTextSize(12);
            monthView.setText(monthS);
        }
        public void setYear(String monthS) {
            yearView.setTextSize(12);
            yearView.setText(monthS);
        }
        public void setThisAdapter(int myPos,String yrSelect){
            GridAdapterMonth myAdapter = new GridAdapterMonth(context, MainActivity.monthofYear.get(myPos),yrSelect,myPos);
            myGridView.setAdapter(myAdapter);

        }



        @Override
        public void onClick(View v) {


        }
    }
}