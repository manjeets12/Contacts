package com.contacts;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.contacts.data.MessageContract;
import com.contacts.data.MessageContract.MessageEntry;

import static android.text.format.DateUtils.getRelativeTimeSpanString;
import static com.contacts.R.id.mobileNumber;

/**
 * Created by ManjeetSingh on 4/20/2017.
 */

public class MessageCursorAdapter extends CursorAdapter {
    public MessageCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_message, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView fullName = (TextView)view.findViewById(R.id.sentTo);
        TextView message = (TextView)view.findViewById(R.id.message);
        TextView timeOfMessage = (TextView)view.findViewById(R.id.timeOfMessage);
        String name = cursor.getString(cursor.getColumnIndexOrThrow(MessageEntry.COLUMN_USER_NAME));
        String messageText = cursor.getString(cursor.getColumnIndexOrThrow(MessageEntry.COLUMN_MESSAGE));
        String time = cursor.getString(cursor.getColumnIndexOrThrow(MessageEntry.COLUMN_TIME));
        fullName.setText(name);
        message.setText(messageText);
        timeOfMessage.setText(getRelativeTime(time));
    }

    private String getRelativeTime(String time) {
        long milliSeconds = Long.valueOf(time);
        return getRelativeTimeSpanString(milliSeconds).toString();
    }
}
