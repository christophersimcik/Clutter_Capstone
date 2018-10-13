package com.clutter.note.main;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

/**
 * Created by csimcik on 9/9/2017.
 */
public class DayAdapter extends RecyclerView.Adapter <DayAdapter.ViewHolder> {
    Context context;

    int itemInt;
    String itemString;
    MainActivity.Day itemDay;
    int itemPosition;
    int scaleFactor = 0;
    int first;
    int last;
    int cntr;
    String yearAdapter;
    public static String monthAdapter;
    public DayAdapter(Context mContextA) {
        context = mContextA;

    }

    @Override
    public DayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.calendar_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DayAdapter.ViewHolder holder, int position) {
        switch (MainActivity.modeSelect) {
            case 1:
                MainActivity.dataSet = MainActivity.yearsSlct;
                yearAdapter =String.valueOf(MainActivity.dataSet.get(position));
                holder.setYear(yearAdapter);
                break;
            case 2:
                MainActivity.dataSet = MainActivity.monthsSlct;
                monthAdapter = MainActivity.monthsSlct.get(position);
               /* if (MainActivity.cntrPos == position) {
                    holder.scaleCntr();
                }*/
                holder.setMonth(monthAdapter);
                break;
            case 3:
                MainActivity.dataSet = MainActivity.daysSlct;
                MainActivity.Day today = MainActivity.daysSlct.get(position);

                Log.i("check it out", String.valueOf(holder.getLayoutPosition()));
                holder.setDay(today.dayID, today.dateID);

              /* if (MainActivity.cntrPos == position) {
                    holder.scaleCntr();
                }*/
                break;
            case 4:

        }


    }

    @Override
    public int getItemCount() {
      return MainActivity.dataSet.size();
    }


    //View holder class, where all view components are defined
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView dayView, dateView, monthView, yearView;
        View rowView;


        public ViewHolder(final View itemView) {
            super(itemView);
            dayView = (TextView) itemView.findViewById(R.id.day);
            dateView = (TextView) itemView.findViewById(R.id.date);
            monthView = (TextView) itemView.findViewById(R.id.month);
            yearView = (TextView) itemView.findViewById(R.id.year);
            itemView.setOnClickListener(this);
            itemView.setOnTouchListener(new View.OnTouchListener(){

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                if(event.getActionMasked() == MotionEvent.ACTION_DOWN){
                        v.setVisibility(View.INVISIBLE);
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
                    if (itemView.getHeight() == 931) {
                        scaleCntr();
                    }
                }

            });
        }

        public void setYear(String year) {
            dayView.setText((year));
            dateView.setVisibility(View.INVISIBLE);
        }

        public void setMonth(String month) {
            dayView.setText(month);
            dateView.setVisibility(View.INVISIBLE);
        }

        public void setDay(String day, int date) {

            dayView.setText(day + "," + " ");
            dateView.setVisibility(View.VISIBLE);
            dateView.setText(String.valueOf(date) + " ");

        }

        public void scaleCntr() {
          //  rowView.setBackgroundColor(hilite);
            dayView.setTextSize(20);
            dateView.setTextSize(20);
            monthView.setTextSize(20);
            yearView.setTextSize(20);
        }

        public void removeAt(int position) {

        }
        @Override
        public void onClick(View v) {


        }
    }
}