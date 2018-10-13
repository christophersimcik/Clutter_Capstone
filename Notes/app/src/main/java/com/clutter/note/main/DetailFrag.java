package com.clutter.note.main;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by csimcik on 10/5/2017.
 */
public class DetailFrag extends Fragment {
    ImageView photo, movie, voice, note, vocab;
    ImageView picTaken;
    public static RecyclerView activityList;
    public static ActivityAdapter thisAdapter;
    ArrayList<EventsModel> todayArray;
    FrameLayout workPlace;
    public static LinearLayout addBar;
    LinearLayout mmLayout;
    LinearLayout.LayoutParams params1;
    LinearLayout.LayoutParams params2;
    static String newVocabWord;
    static String newNotePass;
    SharedPreferences sPrefs;
    SharedPreferences.Editor editor;

    @Override
    public void onStart() {
        super.onStart();
        this.thisAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        sPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = sPrefs.edit();
        if (sPrefs.getInt("initiate", 0) != 1) {
            getPermissions();
            editor.putInt("initiate", 1);
            editor.commit();
        }
        View view = inflater.inflate(R.layout.detail_list, container, false);
        activityList = (RecyclerView) view.findViewById(R.id.daylist);
        LinearLayoutManager activityMan = new LinearLayoutManager(getActivity());
        addBar = (LinearLayout) view.findViewById(R.id.action_row);
        activityList.setLayoutManager(activityMan);
        thisAdapter = new ActivityAdapter(getActivity());
        activityList.setAdapter(thisAdapter);
        //imageviews
        photo = (ImageView) view.findViewById(R.id.pic);
        movie = (ImageView) view.findViewById(R.id.mov);
        voice = (ImageView) view.findViewById(R.id.voi);
        note = (ImageView) view.findViewById(R.id.not);
        vocab = (ImageView) view.findViewById(R.id.voc);

        //notes on-click listener
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                NoteFrag noteFrag = new NoteFrag();
                transaction.replace(MainActivity.fragmentWindow.getId(), noteFrag);
                transaction.addToBackStack(null);
                transaction.commit();
                addBar.setVisibility(View.INVISIBLE);
                MainActivity.updateMain(false);
            }
        });
        //photo on-click Listener
        photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                PhotoFrag photoFrag = new PhotoFrag();
                transaction.replace(MainActivity.fragmentWindow.getId(), photoFrag, MainActivity.TAG_RETAINED_FRAGMENT);
                transaction.addToBackStack(null);
                transaction.commit();
                addBar.setVisibility(View.INVISIBLE);
                MainActivity.updateMain(false);

            }
        });
        vocab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                VocabFrag vocabFrag = new VocabFrag();
                transaction.replace(MainActivity.fragmentWindow.getId(), vocabFrag);
                transaction.addToBackStack(null);
                transaction.commit();
                addBar.setVisibility(View.INVISIBLE);
                MainActivity.updateMain(false);
            }
        });
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                AudioFrag audioFrag = new AudioFrag();
                transaction.replace(MainActivity.fragmentWindow.getId(), audioFrag, MainActivity.TAG_RETAINED_FRAGMENT);
                transaction.addToBackStack(null);
                transaction.commit();
                addBar.setVisibility(View.INVISIBLE);
                MainActivity.updateMain(false);

            }
        });
        movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                MovieFrag movieFrag = new MovieFrag();
                transaction.replace(MainActivity.fragmentWindow.getId(), movieFrag, MainActivity.TAG_RETAINED_FRAGMENT);
                transaction.addToBackStack(null);
                transaction.commit();
                addBar.setVisibility(View.INVISIBLE);
                MainActivity.updateMain(false);

            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.todayArray = MainActivity.database.getTodaysRecords(String.valueOf(MainActivity.todayObj));
        thisAdapter.refreshed = true;
        this.thisAdapter.notifyDataSetChanged();

        super.onPause();
    }


    public void getPermissions() {
        // camera permission
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},
                1);

    }
}
