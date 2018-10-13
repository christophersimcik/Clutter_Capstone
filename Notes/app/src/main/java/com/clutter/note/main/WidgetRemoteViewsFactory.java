package com.clutter.note.main;

import android.app.LauncherActivity;
import android.app.SearchManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{
    public SharedPreferences sharedPreferences;
    private Context mContext;
    private int sizer = 0;
    private Database db;
    private String[] vocabList;
    private ArrayList<VocabModel> vocabModels;
    public WidgetRemoteViewsFactory(Context appContext, Intent intent){
        this.mContext = appContext;
    }
    @Override
    public void onCreate() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        sizer = sharedPreferences.getInt("list_size",0);
        Log.i("sizer ",String.valueOf(sizer));
        vocabModels = new ArrayList<VocabModel>();
        db = new Database(mContext);
        vocabModels = db.getAllRecordsC();
        if(sizer <= vocabModels.size()){
            vocabList = new String[sizer];
        }else{
            vocabList = new String[vocabModels.size()];
        }
        Log.i("sizes", String.valueOf(vocabModels.size()) + " " + String.valueOf(vocabList.length));
        Collections.reverse(vocabModels);
        for(int i = 0; i < vocabList.length; i++){
            vocabList[i]=vocabModels.get(i).getData();
        }




    }

    @Override
    public void onDataSetChanged() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        sizer = sharedPreferences.getInt("list_size",0);
        Log.i("sizer ",String.valueOf(sizer));
        vocabModels = new ArrayList<VocabModel>();
        db = new Database(mContext);
        vocabModels = db.getAllRecordsC();
        if(sizer <= vocabModels.size()){
            vocabList = new String[sizer];
        }else{
            vocabList = new String[vocabModels.size()];
        }
        Log.i("sizes", String.valueOf(vocabModels.size()) + " " + String.valueOf(vocabList.length));
        Collections.reverse(vocabModels);
        for(int i = 0; i < vocabList.length; i++){
            vocabList[i]=vocabModels.get(i).getData();
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
       return  vocabList.length;   }

    @Override
    public RemoteViews getViewAt(int i) {
        final RemoteViews remoteView = new RemoteViews(
                     mContext.getPackageName(),R.layout.appwidget_item);
        remoteView.setTextViewTextSize(R.id.list_text, TypedValue.COMPLEX_UNIT_SP,24);
        String itemText = vocabList[i];
        remoteView.setTextViewText(R.id.list_text,itemText);
        int alphaValue = findOpacity(i,vocabList.length);
        remoteView.setTextColor(R.id.list_text, Color.argb(alphaValue,245,245,245));
        Log.i("this is my alph",String.valueOf(alphaValue));
        Intent newSearchIntent = new Intent();
        newSearchIntent.putExtra(SearchManager.QUERY, itemText);
        remoteView.setOnClickFillInIntent(R.id.list_text,newSearchIntent);
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() { return 1; }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
public int findOpacity(int position, int max){
        int opacity = 255 + ((50 - 255) / (max- 0)) * (position - 0);
 return opacity;
}
}
