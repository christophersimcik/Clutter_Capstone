package com.clutter.note.main;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetBooksJson {
        public static String stream = null;

        public GetBooksJson(){
        }

        public String GetHTTPData(String urlString){
            try{
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                // Check the connection status
                if(urlConnection.getResponseCode() == 200)
                {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader r = new BufferedReader(new InputStreamReader(in));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        sb.append(line);
                    }
                    stream = sb.toString();
                    // End reading...............

                    // Disconnect the HttpURLConnection
                    urlConnection.disconnect();
                }
                else
                {
                }
            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }finally {

            }
            return stream;
        }
    }
