package com.clutter.note.main;

import android.app.LoaderManager;
import android.database.Cursor;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class GoogleBooks extends AppCompatActivity  {
public static List<String[]> list;
public static  GBooksAdapter gBooksAdapter;
private GoogleBooksJSON googleBooksJSON;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String myUrl;
        super.onCreate(savedInstanceState);
        list = new ArrayList<String[]>();
        myUrl = getIntent().getStringExtra("url");
        setContentView(R.layout.activity_google_books);
        googleBooksJSON = new GoogleBooksJSON();
        googleBooksJSON.execute(myUrl);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getApplicationContext());
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.books_recycler_view);
        gBooksAdapter = new GBooksAdapter(this.getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(gBooksAdapter);
        }
}
