package com.clutter.note.main;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

public class ContentProv extends ContentProvider {
    private Database dbProv= new Database(getContext());
    private static final String AUTHORITY = "com.clutter.note.main.ContentProv";
    private static final String BASE_PATH = "EVENTS";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );
    private static final int ALL_EVENTS = 1;
    private static final int AN_EVENT = 0;
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, BASE_PATH, ALL_EVENTS);
        uriMatcher.addURI(AUTHORITY, BASE_PATH+"/#", AN_EVENT);
    }

    @Override
    public boolean onCreate() {
        Log.i("database",new Database(getContext()).getDatabaseName());
        dbProv = new Database(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Log.i("database",new Database(getContext()).getDatabaseName());
            SQLiteDatabase db = dbProv.getWritableDatabase();
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
            queryBuilder.setTables(Database.TABLE_NAMEB);
            Cursor cursor;
            switch (uriMatcher.match(uri)) {
                case ALL_EVENTS:
                    Log.i("uri_matcher ", "ALL_EVENTS");

                    break;
                case AN_EVENT:
                    Log.i("uri_matcher ", "AN_EVENT");
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported URI: " + uri);
            }
        String todayValue = String.valueOf(MainActivity.todayObj);
        String query = "SELECT * FROM " + Database.TABLE_NAMEB + " WHERE " + Database.COLUMN_EVENT_ID +" = '" + todayValue +"'";
        cursor = db.rawQuery(query, null);
        Log.i("cursor"," fired" + String.valueOf(cursor.getCount()));


            //Cursor cursor = queryBuilder.query(db,strings,s,
                    //strings1, null, null, s1);
            return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ALL_EVENTS:
                return "vnd.android.cursor.dir/vnd."+AUTHORITY;
            case AN_EVENT:
                return "vnd.android.cursor.item/vnd."+AUTHORITY;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = dbProv.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_EVENTS:
                //do nothing
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        long id = db.insert(Database.TABLE_NAMEB, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(CONTENT_URI + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db = dbProv.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_EVENTS:
                //do nothing
                break;
            case AN_EVENT:
                String id = uri.getPathSegments().get(1);
                s = Database.COLUMN_ID_B + "=" + id
                        + (!TextUtils.isEmpty(s) ?
                        " AND (" + s + ')' : "");
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int deleteCount = db.delete(Database.TABLE_NAMEB, s, strings);
        getContext().getContentResolver().notifyChange(uri, null);
        return deleteCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db = dbProv.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_EVENTS:
                //do nothing
                break;
            case AN_EVENT:
                String id = uri.getPathSegments().get(1);
                s = Database.COLUMN_ID_B + "=" + id
                        + (!TextUtils.isEmpty(s) ?
                        " AND (" + s + ')' : "");
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int updateCount = db.update(Database.COLUMN_ID_B, values, s, strings);
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;

    }
}
