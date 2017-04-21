package com.contacts.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.contacts.data.ContactContract.ContactEntry;
import com.contacts.data.MessageContract.MessageEntry;

/**
 * Created by ManjeetSingh on 4/19/2017.
 */

public class AppProvider extends ContentProvider{
    public static final String LOG_TAG = AppProvider.class.getSimpleName();
    private AppDbHelper mAppDbHelper;
    private static final int CONTACTS = 100;
    private static final int CONTACTS_ID =101;
    private static final int MESSAGES = 200;
    private static final int MESSAGES_ID =201;
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static
    {
        sURIMatcher.addURI(ContactContract.CONTENT_AUTHORITY, ContactContract.PATH_CONTACTS, CONTACTS);
        sURIMatcher.addURI(ContactContract.CONTENT_AUTHORITY, ContactContract.PATH_CONTACTS+"/#", CONTACTS_ID);
        sURIMatcher.addURI(ContactContract.CONTENT_AUTHORITY, MessageContract.PATH_MESSAGES, MESSAGES);
        sURIMatcher.addURI(ContactContract.CONTENT_AUTHORITY, MessageContract.PATH_MESSAGES+"/#", MESSAGES_ID);
    }

    @Override
    public boolean onCreate() {
        mAppDbHelper = new AppDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final int match = sURIMatcher.match(uri);   //will check for sinlge or multiple item requirement
        Cursor cursor =null;
        switch (match){
            case CONTACTS:
                cursor = getContacts(projection, selection, selectionArgs, sortOrder);
                break;
            case CONTACTS_ID:
                selection = ContactEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = getContacts(projection, selection, selectionArgs, sortOrder);
                break;
            case MESSAGES:
                cursor = getMessages(projection, selection, selectionArgs, sortOrder);
                break;
            case MESSAGES_ID:
                selection = ContactEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = getMessages(projection, selection, selectionArgs, sortOrder);
                break;
        }
        if(cursor !=null){
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    private Cursor getContacts(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mAppDbHelper.getReadableDatabase();
        Cursor cursor = db.query(ContactEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        return cursor;
    }
    private Cursor getMessages(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mAppDbHelper.getReadableDatabase();
        Cursor cursor = db.query(MessageEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sURIMatcher.match(uri);
        switch (match){
            case CONTACTS:
                return ContactEntry.CONTENT_LIST_TYPE;
            case CONTACTS_ID:
                return ContactEntry.CONTENT_ITEM_TYPE;
            case MESSAGES:
                return MessageEntry.CONTENT_LIST_TYPE;
            case MESSAGES_ID:
                return MessageEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sURIMatcher.match(uri);
        Uri result = null;
        switch (match){
            case CONTACTS:
                result=insertContact(uri, contentValues);
                break;
            case MESSAGES:
                result=insertMessage(uri, contentValues);
                break;
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
        return result;
    }

    private Uri insertMessage(Uri uri, ContentValues contentValues) {
        String name = contentValues.getAsString(MessageEntry.COLUMN_USER_NAME);
        String message = contentValues.getAsString(MessageEntry.COLUMN_MESSAGE);
        String time = contentValues.getAsString(MessageEntry.COLUMN_TIME);

        if(name == null){
            throw new IllegalArgumentException("Name is not valid");
        }
        if(message == null){
            throw new IllegalArgumentException("Message is not valid");
        }
        if(time == null){
            throw new IllegalArgumentException("time is not valid");
        }
        SQLiteDatabase db = mAppDbHelper.getWritableDatabase();
        long id = db.insert(MessageEntry.TABLE_NAME,null,contentValues);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertContact(Uri uri, ContentValues contentValues) {
        String firstName = contentValues.getAsString(ContactEntry.FIRST_NAME);
        String lastName = contentValues.getAsString(ContactEntry.LAST_NAME);
        String avatar = contentValues.getAsString(ContactEntry.AVATAR_URL);
        String mobileNumber = contentValues.getAsString(ContactEntry.MOBILE_NUMBER);

        if(firstName == null){
            throw new IllegalArgumentException("Name is not valid");
        }
        if(mobileNumber == null || !ContactEntry.isValidMobile(mobileNumber)){
            throw new IllegalArgumentException("mobile number is not valid");
        }

        SQLiteDatabase db = mAppDbHelper.getWritableDatabase();
        long id = db.insert(ContactEntry.TABLE_NAME,null,contentValues);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}

