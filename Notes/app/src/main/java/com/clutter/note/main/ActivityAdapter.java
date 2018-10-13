package com.clutter.note.main;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by csimcik on 9/9/2017.
 */
public class ActivityAdapter extends RecyclerView.Adapter <ActivityAdapter.ViewHolder> {
    Context context;
    File file;
    Uri uri;
    public static Boolean refreshed = true;

    public ActivityAdapter(Context mContextA) {
        context = mContextA;


    }

    @Override
    public ActivityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.activity_recycle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ActivityAdapter.ViewHolder holder, int position) {
        Log.i("value of todayArray", String.valueOf(MainActivity.todayArray.size()));
            switch (MainActivity.todayArray.get(position).getType()) {
                case "M":
                    Picasso.with(context)
                            .load(new File(MainActivity.todayArray.get(position).getThumb()))
                            .into(holder.images);
                    holder.indic.getLayoutParams().height = 125;
                    holder.indic.getLayoutParams().width = 125;
                    holder.indic.setImageBitmap(TagGen.drawHex("M", this.context, 0));
                    holder.dataType = "M";
                    holder.theLay.removeView(holder.linLay);
                    holder.stringData = (MainActivity.todayArray.get(position).getUrl());
                    holder.thumbData = (MainActivity.todayArray.get(position).getUrl());
                    holder.deleteId = position;

                    break;
                case "P":
                    if (MainActivity.todayArray.get(position).getThumb() != null) {
                        Bitmap photo;
                        Log.i("this it a val", MainActivity.todayArray.get(position).getThumb());
                        Picasso.with(context)
                                .load(new File(MainActivity.todayArray.get(position).getThumb()))
                                .into(holder.images);
                        holder.indic.getLayoutParams().height = 125;
                        holder.indic.getLayoutParams().width = 125;
                        holder.indic.setImageBitmap(TagGen.drawHex("P", this.context, 0));
                        holder.dataType = "P";
                        holder.theLay.removeView(holder.linLay);
                        holder.stringData = (MainActivity.todayArray.get(position).getUrl());
                        holder.thumbData = (MainActivity.todayArray.get(position).getThumb());
                        holder.deleteId = position;
                    }
                    break;
                case "V":
                    holder.setMyText(String.valueOf(MainActivity.todayArray.get(position).getUrl()));
                    holder.indic.getLayoutParams().height = 125;
                    holder.indic.getLayoutParams().width = 125;
                    holder.indic.setImageBitmap(TagGen.drawHex("V", this.context, 0));
                    holder.dataType = "V";
                    holder.stringData = MainActivity.todayArray.get(position).getUrl();
                    holder.thumbData = (MainActivity.todayArray.get(position).getThumb());
                    holder.deleteId = position;
                    break;
                case "B":
                    holder.setMyText(String.valueOf(MainActivity.todayArray.get(position).getUrl()));
                    holder.indic.getLayoutParams().height = 125;
                    holder.indic.getLayoutParams().width = 125;
                    holder.indic.setImageBitmap(TagGen.drawHex("B", this.context, 0));
                    holder.dataType = "B";
                    holder.stringData = MainActivity.todayArray.get(position).getUrl();
                    holder.thumbData = (MainActivity.todayArray.get(position).getThumb());
                    holder.deleteId = position;
                    break;
                case "N":
                    holder.setMyText(MainActivity.todayArray.get(position).getThumb());
                    holder.indic.getLayoutParams().height = 125;
                    holder.indic.getLayoutParams().width = 125;
                    holder.indic.setImageBitmap(TagGen.drawHex("N", this.context, 0));
                    holder.dataType = "N";
                    holder.stringData = MainActivity.todayArray.get(position).getUrl();
                    holder.thumbData = MainActivity.todayArray.get(position).getThumb();
                    holder.deleteId = position;
                    break;
                case "S":
                    Picasso.with(context)
                            .load(new File(MainActivity.todayArray.get(position).getThumb()))
                            .into(holder.images);
                    holder.indic.getLayoutParams().height = 125;
                    holder.indic.getLayoutParams().width = 125;
                    holder.indic.setImageBitmap(TagGen.drawHex("S", this.context, 0));
                    holder.dataType = "S";
                    holder.theLay.removeView(holder.linLay);
                    holder.stringData = MainActivity.todayArray.get(position).getUrl();
                    holder.thumbData = MainActivity.todayArray.get(position).getThumb();
                    holder.deleteId = position;
                    break;


            }

            holder.setIsRecyclable(false);
        }
    
    @Override
    public int getItemCount() {
        return MainActivity.todayArray.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {
        LinearLayout linLay = (LinearLayout) itemView.findViewById(R.id.lin_lay);
        LinearLayout theLay = (LinearLayout) itemView.findViewById(R.id.the_layout);
        ImageView images = (ImageView) itemView.findViewById(R.id.activty_img);
        ImageView indic = (ImageView) itemView.findViewById(R.id.indicator);
        TextView texts = (TextView) itemView.findViewById(R.id.activity_text);
        Eraser eraser = (Eraser) itemView.findViewById(R.id.eraser_view);
        WaitingView waitingView = (WaitingView) itemView.findViewById(R.id.custom_view);
        ViewSwitcher viewSwitcher = (ViewSwitcher) itemView.findViewById(R.id.viewSwitcher2);
        String dataType = null, stringData = null, thumbData = null;
        int deleteId, counter = 0;
        Boolean canRun = false;
        Handler handler = new Handler();

        public ViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnTouchListener(this);
            images.getLayoutParams().width = MainActivity.w;
            images.getLayoutParams().height = 125;
            images.setScaleType(ImageView.ScaleType.FIT_CENTER);
            indic.setScaleType(ImageView.ScaleType.FIT_CENTER);
            if (refreshed = false) {
                viewSwitcher.showNext();
                refreshed = true;
            }
        }

        public void setMyText(String textInput) {
            SpannableStringBuilder builder = new SpannableStringBuilder();
            SpannableString spanText = new SpannableString(textInput);
            spanText.setSpan(new ForegroundColorSpan(Color.LTGRAY), 1, textInput.length(), 0);
            spanText.setSpan(new RelativeSizeSpan(1.125f), 0, 1, 0);
            builder.append(spanText);
            texts.setGravity(Gravity.CENTER);
            texts.setTextSize(24);
            texts.setBackgroundColor(Color.parseColor("#4B4B4B"));
            texts.setTextColor(Color.parseColor("#ffffff"));
            texts.setWidth(MainActivity.w);
            texts.setHeight(125);
            texts.setText(builder, TextView.BufferType.SPANNABLE);
            waitingView.setBackgroundColor(Color.parseColor("#fafafa"));
            waitingView.getLayoutParams().width = 300;
            waitingView.getLayoutParams().height = 50;
        }

        @Override
        public void onClick(View v) {
            Log.i("Chcker", "clicked***");

        }


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.i("it's DOWN ", "ACTION_DOWN");
                counter = 0;
                eraser.active = true;
                canRun = true;
                eraser.invalidate();
                handler.postDelayed(timing,5);


        }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if(counter <= 10) {
                    counter = 0;
                    canRun = false;
                    eraser.active = false;
                    eraser.counter = 0;
                    eraser.invalidate();
                    Log.i("its going", "here!!!!!");
                    handler.removeCallbacksAndMessages(timing);
                    switch (dataType) {
                        case "V":
                            viewSwitcher.showNext();
                            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                            intent.putExtra(SearchManager.QUERY, stringData);
                            context.startActivity(intent);
                            break;
                        case "B":
                            viewSwitcher.showNext();
                           Intent bookIntent = new Intent(context.getApplicationContext(),GoogleBooks.class);
                           bookIntent.putExtra("url","https://www.googleapis.com/books/v1/volumes?q="+stringData+"+subject&download=epub&key=<YOUR API HERE>");
                           context.startActivity(bookIntent);
                            break;
                        case "N":
                            FragmentManager manager = MainActivity.manager;
                            FragmentTransaction transaction = manager.beginTransaction();
                            NoteFrag noteFrag = new NoteFrag();
                            Bundle bundle = new Bundle();
                            bundle.putString("url", stringData);
                            bundle.putString("thumb", thumbData);
                            bundle.putBoolean("edit", true);
                            noteFrag.setArguments(bundle);
                            // maybe put tag into bundle to indicate ts an update record vs insert record
                            transaction.replace(MainActivity.fragmentWindow.getId(), noteFrag);
                            transaction.addToBackStack(null);
                            transaction.commit();
                            DetailFrag.addBar.setVisibility(View.INVISIBLE);
                            break;
                        case "P":
                            Intent photoIntent = new Intent();
                            photoIntent.setAction(Intent.ACTION_VIEW);
                            file = new File(stringData);
                            uri =  FileProvider.getUriForFile(context, "com.clutter.android.fileprovider", file);
                            photoIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            photoIntent.setDataAndType(uri, "image/*");
                            context.startActivity(photoIntent);
                            break;
                        case "M":
                            Intent movieIntent = new Intent();
                            movieIntent.setAction(Intent.ACTION_VIEW);
                            file = new File(stringData);
                            uri =  FileProvider.getUriForFile(context, "com.clutter.android.fileprovider", file);
                            movieIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            movieIntent.setDataAndType(uri, "video/*");
                            context.startActivity(movieIntent);
                            break;
                        case "S":
                            Intent audioIntent = new Intent();
                            audioIntent.setAction(Intent.ACTION_VIEW);
                            file = new File(stringData);;
                            uri =  FileProvider.getUriForFile(context, "com.clutter.android.fileprovider", file);
                            audioIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            //audioIntent.setDataAndType(Uri.parse("file://" + stringData), "audio/*");
                            audioIntent.setDataAndType(uri,"audio/*");
                            context.startActivity(audioIntent);
                            break;


                    }
                }else{
                    eraser.active = false;
                    eraser.counter = 0;
                    eraser.invalidate();
                    counter = 0;
                    handler.removeCallbacksAndMessages(timing);
                }
            }
            // handle if event drags outside and fails to reg. action_up
            if(event.getAction() == 3){
                eraser.active = false;
                eraser.counter = 0;
                eraser.invalidate();
                counter = 0;
                canRun = false;
                handler.removeCallbacksAndMessages(timing);
            }

            return true;
        }

        private Runnable timing = new Runnable() {
            @Override
            public void run() {
                if(counter == 5){
                    eraser.deleting();
                }
                counter += 1;
                // if timeout delete file :)
                if(eraser.erasing) {
                    canRun = false;
                    switch (dataType) {
                        case "V":
                            eraser.setVisibility(View.VISIBLE);
                            Toast.makeText(context, "term deleted", Toast.LENGTH_SHORT).show();
                            MainActivity.database.deleteRecordAlt(MainActivity.todayArray.get(deleteId));
                            MainActivity.todayArray = MainActivity.database.getTodaysRecords(String.valueOf(MainActivity.todayObj));
                            DetailFrag.thisAdapter.notifyDataSetChanged();
                            MainActivity.database.deleteRecordC(texts.getText().toString());
                            AppWidgetList.refreshWidget(context);
                            break;
                        case "B":
                            eraser.setVisibility(View.VISIBLE);
                            Toast.makeText(context, "term deleted", Toast.LENGTH_SHORT).show();
                            MainActivity.database.deleteRecordAlt(MainActivity.todayArray.get(deleteId));
                            MainActivity.todayArray = MainActivity.database.getTodaysRecords(String.valueOf(MainActivity.todayObj));
                            DetailFrag.thisAdapter.notifyDataSetChanged();
                            MainActivity.database.deleteRecordC(texts.getText().toString());
                            AppWidgetList.refreshWidget(context);
                            break;
                        case "N":
                            eraser.setVisibility(View.VISIBLE);
                            Toast.makeText(context,"note deleted",Toast.LENGTH_SHORT).show();
                            MainActivity.database.deleteRecordAlt(MainActivity.todayArray.get(deleteId));
                            MainActivity.todayArray = MainActivity.database.getTodaysRecords(String.valueOf(MainActivity.todayObj));
                            DetailFrag.thisAdapter.notifyDataSetChanged();
                            break;
                        case "P":
                            Toast.makeText(context,"photo deleted",Toast.LENGTH_SHORT).show();
                            MainActivity.database.deleteRecordAlt(MainActivity.todayArray.get(deleteId));
                            File filePic = new File(stringData);
                            filePic.delete();
                            File fileThumb = new File(thumbData);
                            fileThumb.delete();
                            MainActivity.todayArray = MainActivity.database.getTodaysRecords(String.valueOf(MainActivity.todayObj));
                            DetailFrag.thisAdapter.notifyDataSetChanged();
                            break;
                        case "M":
                            Toast.makeText(context,"video deleted",Toast.LENGTH_SHORT).show();
                            MainActivity.database.deleteRecordAlt(MainActivity.todayArray.get(deleteId));
                            File fileVid = new File(stringData);
                            fileVid.delete();
                            File fileVidThumb = new File(thumbData);
                            fileVidThumb.delete();
                            MainActivity.todayArray = MainActivity.database.getTodaysRecords(String.valueOf(MainActivity.todayObj));
                            DetailFrag.thisAdapter.notifyDataSetChanged();
                            break;
                        case "S":
                            Toast.makeText(context,"audio deleted",Toast.LENGTH_SHORT).show();
                            MainActivity.database.deleteRecordAlt(MainActivity.todayArray.get(deleteId));
                            File fileSND = new File(stringData);
                            fileSND.delete();
                            File fileVIS = new File(thumbData);
                            fileVIS.delete();
                            MainActivity.todayArray = MainActivity.database.getTodaysRecords(String.valueOf(MainActivity.todayObj));
                            DetailFrag.thisAdapter.notifyDataSetChanged();
                            break;

                    }

                    handler.removeCallbacksAndMessages(timing);
                }
              if(canRun) {
                    handler.postDelayed(this,10);
                }
            }
        };

    }
}


