package com.clutter.note.main;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by csimcik on 11/21/2017.
 */
public class Database extends SQLiteOpenHelper {
    public static final String TABLE_NAMEA = "DAYS";
    public static final String TABLE_NAMEB = "EVENTS";
    public static final String TABLE_NAMEC = "VOCAB";
    public static final String COLUMN_VOCAB ="WORD";
    public static final String COLUMN_ID_B ="ADDED";
    public static final String COLUMN_YEAR = "YEAR";
    public static final String COLUMN_MONTH = "MONTH";
    public static final String COLUMN_DAY = "DAY";
    public static final String COLUMN_DATE = "DATE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_EVENT_ID = "EVENTID";
    public static final String COLUMN_TYPE = "TYPE";
    public static final String COLUMN_URL = "URL";
    public static final String COLUMN_THUMB = "THUMB";
    private static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "SQLiteDatabase.db";
    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public final String tableA = "CREATE TABLE "+TABLE_NAMEA+" "+ "("+COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+" "+COLUMN_YEAR+ " VARCHAR,"+" "+COLUMN_MONTH+ " VARCHAR,"+" "+COLUMN_DATE+ " VARCHAR,"+" "+COLUMN_DAY+ " VARCHAR)";
    public final String tableB = "CREATE TABLE "+TABLE_NAMEB+" "+ "("+COLUMN_TYPE+ " VARCHAR,"+" "+COLUMN_URL+ " VARCHAR,"+" " + COLUMN_THUMB + " VARCHAR,"+" "+COLUMN_EVENT_ID+ " INTEGER, FOREIGN KEY ("+COLUMN_EVENT_ID+") REFERENCES "+TABLE_NAMEA+" ("+COLUMN_ID+"))";
    public final String tableC = "CREATE TABLE "+TABLE_NAMEC+" "+ "("+COLUMN_ID_B+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+" "+COLUMN_VOCAB+ " VARCHAR)";
    @Override
    public void onConfigure(SQLiteDatabase db){
        db.setForeignKeyConstraintsEnabled(true);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tableA);
        db.execSQL(tableB);
        db.execSQL(tableC);

        Log.i("Tbale string", tableA);
        Log.i("Tbale string 2", tableB);
        //db.setForeignKeyConstraintsEnabled(true);
       // db.execSQL("create table " + TABLE_NAMEA + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_YEAR + " VARCHAR," + COLUMN_MONTH + " VARCHAR," + COLUMN_DATE + " VARCHAR,");
       // db.execSQL("create table " + TABLE_NAMEB + " ( " + COLUMN_EVENT_ID + " INTEGER,"+ " FOREIGN KEY("+  COLUMN_EVENT_ID + ") REFERENCES"+ TABLE_NAMEA + "(ID)"+")"+ COLUMN_TYPE + " VARCHAR," + COLUMN_URL + " VARCHAR, " + COLUMN_THUMB + " VARCHAR);");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAMEA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAMEB);
        onCreate(db);
    }
    private SQLiteDatabase database;
    public void insertRecord(DBModel contact) {
        database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_YEAR, contact.getYear());
        contentValues.put(COLUMN_MONTH, contact.getMonth());
        contentValues.put(COLUMN_DATE, contact.getDate());
        contentValues.put(COLUMN_DAY, contact.getDay());
        database.insert(TABLE_NAMEA, null, contentValues);
        database.close();
    }
    public void insertRecordB(EventsModel contact) {
        database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_EVENT_ID,contact.getEventsID());
        contentValues.put(COLUMN_TYPE,contact.getType());
        contentValues.put(COLUMN_THUMB,contact.getThumb());
        contentValues.put(COLUMN_URL,contact.getUrl());
        database.insert(TABLE_NAMEB, null, contentValues);
        database.close();
    }
    public void insertRecordC(VocabModel vocab) {
        database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_VOCAB,vocab.getData());
        database.insert(TABLE_NAMEC, null, contentValues);
        database.close();
    }
    public void updateRecordC(VocabModel vocab) {
        database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_VOCAB, vocab.getData());
        database.update(TABLE_NAMEC, contentValues, COLUMN_ID_B+ " = ?", new String[]{vocab.getID()});
        database.close();
    }
    public void updateRecord(DBModel contact) {
        database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_YEAR, contact.getYear());
        contentValues.put(COLUMN_MONTH, contact.getMonth());
        contentValues.put(COLUMN_DATE, contact.getDate());
        contentValues.put(COLUMN_DAY, contact.getDay());
        database.update(TABLE_NAMEA, contentValues, COLUMN_ID + " = ?", new String[]{contact.getID()});
        database.close();
    }
    public void updateRecordB(EventsModel eventsModel, String[] oldUrl) {
        database = this.getReadableDatabase();
        String updateText = eventsModel.getUrl();
        String updateTitle = eventsModel.getThumb();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_URL, updateText);
        contentValues.put(COLUMN_THUMB, updateTitle);
        database.update(TABLE_NAMEB, contentValues, COLUMN_URL+" = ?",oldUrl);
        database.close();
    }

    public void deleteRecord(DBModel contact) {
        database = this.getReadableDatabase();
        database.delete(TABLE_NAMEA, COLUMN_ID + " = ?", new String[]{contact.getID()});
        database.close();
    }
    public void deleteRecordC(String vocabData) {
        database = this.getReadableDatabase();
        database.delete(TABLE_NAMEC, COLUMN_VOCAB + " = ?", new String[]{vocabData});
        database.close();
    }
    public void deleteRecordAlt(EventsModel contact) {
        database = this.getReadableDatabase();
        database.delete(TABLE_NAMEB, COLUMN_URL + " = ?", new String[]{contact.getUrl()});
        database.close();
    }
    public ArrayList<DBModel> getAllRecords() {
        database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAMEA, null, null, null, null, null, null);
        ArrayList<DBModel> contacts = new ArrayList<DBModel>();
        DBModel dbModel;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                dbModel = new DBModel();
                dbModel.setID(cursor.getString(0));
                dbModel.setYear(cursor.getString(1));
                dbModel.setMonth(cursor.getString(2));
                dbModel.setDate(cursor.getString(3));
                dbModel.setDay(cursor.getString(4));
                contacts.add(dbModel);
            }
        }
        cursor.close();
        database.close();
        return contacts;
    }
    public ArrayList<EventsModel> getAllRecordsB() {
        database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAMEB, null, null, null, null, null, null);
        ArrayList<EventsModel> events = new ArrayList<>();
        EventsModel eventsModel;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                eventsModel = new EventsModel();
                eventsModel.setEventsID(cursor.getString(3));
                eventsModel.setType(cursor.getString(0));
                eventsModel.setUrl(cursor.getString(1));
                eventsModel.setThumb(cursor.getString(2));
                events.add(eventsModel);
            }
        }
        cursor.close();
        database.close();
        return events;
    }
    public ArrayList<VocabModel> getAllRecordsC() {
        database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAMEC, null, null, null, null, null, null);
        ArrayList<VocabModel> vocabs = new ArrayList<>();
        VocabModel vocabModel;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                vocabModel = new VocabModel();
                vocabModel.setData(cursor.getString(1));
                vocabModel.setID(cursor.getString(0));
                vocabs.add(vocabModel);
            }
        }
        cursor.close();
        database.close();
        return vocabs;
    }
    public ArrayList<EventsModel> getTodaysRecords(String today){
        database = this.getReadableDatabase();
        EventsModel eventsModel;
        ArrayList<EventsModel> events = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAMEB + " WHERE " + COLUMN_EVENT_ID +" = '" + today +"'";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                eventsModel = new EventsModel();
                eventsModel.setEventsID(cursor.getString(3));
                eventsModel.setType(cursor.getString(0));
                eventsModel.setUrl(cursor.getString(1));
                eventsModel.setThumb(cursor.getString(2));
                events.add(eventsModel);
            }
        }
        cursor.close();
        database.close();
        return events;
    }

    public ArrayList<EventsModel> getDaysRecords(int pos) {
        database = this.getReadableDatabase();
        String today = "SELECT * FROM "+TABLE_NAMEB+" WHERE "+COLUMN_EVENT_ID+ " =" + String.valueOf(pos+1);
        Cursor cursor = database.rawQuery(today,null);
        ArrayList<EventsModel> events = new ArrayList<>();
        EventsModel eventsModel;
        if (cursor != null) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToFirst();
                eventsModel = new EventsModel();
                eventsModel.setEventsID(cursor.getString(0));
                eventsModel.setType(cursor.getString(1));
                eventsModel.setUrl(cursor.getString(2));
                eventsModel.setThumb(cursor.getString(2));
                events.add(eventsModel);
            }
        }
        cursor.close();
        database.close();
        return events;
    }

}
