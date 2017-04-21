package com.contacts.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.widget.Toast;

import com.contacts.EditContactActivity;
import com.contacts.R;
import com.contacts.data.ContactContract.ContactEntry;
import com.contacts.data.MessageContract.MessageEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileReader;

/**
 * Created by ManjeetSingh on 4/19/2017.
 */

public class AppDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = AppDbHelper.class.getSimpleName(); //for logs
    private static final String DATABASE_NAME = "tasks.db";     //sqlite database
    private static final int DATABASE_VERSION =1;

    // Table Create Statements
    // Contacts table create statement
    private static final String SQL_CREATE_CONTACTS_TABLE ="CREATE TABLE "+ ContactEntry.TABLE_NAME+"("
            +ContactEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +ContactEntry.FIRST_NAME+" TEXT NOT NULL, "
            +ContactEntry.LAST_NAME+" TEXT, "
            +ContactEntry.AVATAR_URL+" TEXT, "
            +ContactEntry.MOBILE_NUMBER+" TEXT NOT NULL);";

    // Messages table create statement
    String SQL_CREATE_MESSAGES_TABLE ="CREATE TABLE "+ MessageEntry.TABLE_NAME+"("
            +MessageEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +MessageEntry.COLUMN_USER_NAME+" TEXT NOT NULL, "
            +MessageEntry.COLUMN_MESSAGE+" TEXT NOT NULL, "
            +MessageEntry.COLUMN_TIME+" INT NOT NULL);";



    public AppDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }
    public AppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(SQL_CREATE_CONTACTS_TABLE);
        db.execSQL(SQL_CREATE_MESSAGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + MessageEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ContactEntry.TABLE_NAME);
        // create new tables
        onCreate(db);
    }
}
