package com.clutter.note.main;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by csimcik on 11/29/2017.
 */
public class DatesAdapter extends RecyclerView.Adapter <DatesAdapter.ViewHolder> {
    public static int currentPos;
    Context context;
    ArrayList<DBModel> datesList;
    public DatesAdapter(Context mContext, ArrayList<DBModel> mDatesList) {
        context = mContext;
        datesList = mDatesList;
    }

    @Override
    public DatesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.dates_horz, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DatesAdapter.ViewHolder holder, int position) {
       String thisText = holder.textSet(MainActivity.datesListings.get(position));
        holder.dateView.setTextSize(24);
        holder.dateView.setText(thisText);

    }


    @Override
    public int getItemCount() {
        return MainActivity.datesListings.size();
    }


    //View holder class, where all view components are defined
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView dateView;
        public ViewHolder(final View itemView) {
            super(itemView);
            dateView = (TextView) itemView.findViewById(R.id.experiemental);

            itemView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                 //   AsyncDayEvent asyncDayEvent = (AsyncDayEvent) new AsyncDayEvent(currentPos).execute();
                }

            });

        }

        @Override
        public void onClick(View v) {

        }
        public String textSet(DBModel myDay){
            String myDate = myDay.getDay()+", "+myDay.getMonth()+" "+myDay.getDate()+" "+myDay.getYear();
            return myDate;
        }

    }

}

