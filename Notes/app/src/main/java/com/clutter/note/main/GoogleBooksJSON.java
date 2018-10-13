package com.clutter.note.main;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GoogleBooksJSON extends AsyncTask<String,Void,String>  {
    public static int titles = 0,subtitles = 1, authors = 2,publishers = 3, pages = 4,languages = 5, descriptions = 6, images = 7;
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            Log.i("url strings",urlString);

        GetBooksJson jsonSearch = new GetBooksJson();
         stream = jsonSearch.GetHTTPData(urlString);

            // Return the data from specified url
            return stream;
        }

        protected void onPostExecute(String stream){
           // TextView tv = (TextView) findViewById(R.id.tv);
            //tv.setText(stream);

            /*
                Important in JSON DATA
                -------------------------
                * Square bracket ([) represents a JSON array
                * Curly bracket ({) represents a JSON object
                * JSON object contains key/value pairs
                * Each key is a String and value may be different data types
             */

            //..........Process JSON DATA................
            if(stream !=null){
                try{
                    // Get the full HTTP Data as JSONObject
                    JSONObject reader= new JSONObject(stream);
                    JSONArray array = reader.getJSONArray("items");
                    GoogleBooks.list = new ArrayList<>();
                    for(int i = 0 ; i < array.length() ; i++) {
                            String[] strings = new String[8];
                        JSONObject jsonObject = null;
                        jsonObject = array.getJSONObject(i).getJSONObject("volumeInfo");
                        if(jsonObject.has("title")) {
                                strings[titles] = jsonObject.getString("title");
                        }
                        if(jsonObject.has("subtitle")) {
                                strings[subtitles] = jsonObject.getString("subtitle");
                        }

                        if(jsonObject.has("authors")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("authors");
                            String[] authorStrings = new String[jsonArray.length()];
                            String authorList = "";
                            for (int j = 0; j < jsonArray.length(); j++) {
                                authorStrings[j] = jsonArray.get(j).toString();
                            }
                            if (authorStrings.length > 1) {
                                for (int h = 0; h < authorStrings.length; h++) {
                                    if (h+1 < authorStrings.length) {
                                        authorList += authorStrings[h] + ", ";
                                    }else{
                                        authorList += authorStrings[h];
                                    }
                                }
                            }
                                strings[authors] = authorList;
                            }

                        String publisher = "n/a", publishDate = "n/a";
                        if(jsonObject.has("publisher")) {
                             publisher = jsonObject.getString("publisher");
                        }
                        if(jsonObject.has("publishedDate")) {
                            publishDate = jsonObject.getString("publishedDate");
                        }
                            strings[publishers] = publisher + "," + " " + publishDate;
                        if(jsonObject.has("pageCount")) {
                            strings[pages] = jsonObject.getString("pageCount");
                        }
                        if(jsonObject.has("language")) {
                            strings[languages] = jsonObject.getString("language");
                        }
                        if(jsonObject.has("description")) {
                            strings[descriptions] = jsonObject.getString("description");
                        }
                            JSONObject jsonObject1 = jsonObject.getJSONObject("imageLinks");
                        if(jsonObject1.has("thumbnail")) {
                            strings[images] = jsonObject1.getString("thumbnail");
                        }
                            GoogleBooks.list.add(strings);
                        }
                        GoogleBooks.gBooksAdapter.notifyDataSetChanged();
                    for(int i = 0; i < GoogleBooks.list.size();i++){
                        Log.i("JSONs = ", GoogleBooks.list.get(i)[0].toString());
                        Log.i("pics= ", GoogleBooks.list.get(i)[images].toString());
                    }




                    // process other data as this way..............

                }catch(JSONException e){
                    e.printStackTrace();
           //    } catch (JSONException e) {
                    e.printStackTrace();
                }

            } // if statement end
        } // onPostExecute() end
    } // ProcessJSON class end

