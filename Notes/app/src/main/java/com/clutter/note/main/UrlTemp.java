package com.clutter.note.main;

import android.os.AsyncTask;
import android.util.Log;

public class UrlTemp extends AsyncTask<String,Void,String> {
    @Override
    protected String doInBackground(String... strings) {
        String stream = null;
        String urlString = strings[0];

        GetBooksJson jsonSearch = new GetBooksJson();
         stream = jsonSearch.GetHTTPData(urlString);
        return stream;
    }

    @Override
    protected void onPostExecute(String stream) {
        Log.i("this is the json", "/n"+stream );
    }
}
