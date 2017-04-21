package com.contacts.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import static android.text.style.TtsSpan.GENDER_FEMALE;

/**
 * Created by ManjeetSingh on 4/19/2017.
 *
 *
 */

public class ContactContract {

    private ContactContract() {}
    public static final String CONTENT_AUTHORITY = "com.contacts";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_CONTACTS = "contacts";

    public static class ContactEntry implements BaseColumns{
        public static final Uri CONTENT_URI =Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CONTACTS);

        //This is the Android platform's base MIME type for a content: URI containing a Cursor of zero or more items.
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"+ CONTENT_AUTHORITY +"/"+ PATH_CONTACTS;

        //This is the Android platform's base MIME type for a content: URI containing a Cursor of a single item
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"+ CONTENT_AUTHORITY +"/"+ PATH_CONTACTS;

        public final static String TABLE_NAME = "contacts";
        public final static String _ID = BaseColumns._ID;
        public final static String FIRST_NAME ="firtsName";
        public final static String LAST_NAME ="lastName";
        public final static String AVATAR_URL ="avatar";
        public final static String MOBILE_NUMBER ="mobileNumber";


        public static boolean isValidMobile(String mobileNumber) {
            int size = mobileNumber.length();
            return  (size < 10)?false:true;
        }

    }
}



