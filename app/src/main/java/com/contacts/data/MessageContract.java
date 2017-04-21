package com.contacts.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.contacts.data.ContactContract.PATH_CONTACTS;

/**
 * Created by ManjeetSingh on 4/19/2017.
 */

public class MessageContract {

    private MessageContract(){}

    public static final String CONTENT_AUTHORITY = "com.contacts";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MESSAGES = "messages";

    public static class MessageEntry implements BaseColumns {
        public static final Uri CONTENT_URI =Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MESSAGES);

        //This is the Android platform's base MIME type for a content: URI containing a Cursor of zero or more items.
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"+ CONTENT_AUTHORITY +"/"+ PATH_MESSAGES;

        //This is the Android platform's base MIME type for a content: URI containing a Cursor of a single item
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"+ CONTENT_AUTHORITY +"/"+ PATH_MESSAGES;

        public final static String TABLE_NAME = "messages";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_USER_NAME ="name";
        public final static String COLUMN_MESSAGE ="message";
        public final static String COLUMN_TIME ="time";
    }


}
