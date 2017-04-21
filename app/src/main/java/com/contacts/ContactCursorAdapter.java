package com.contacts;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.contacts.data.ContactContract.ContactEntry;
import com.squareup.picasso.Picasso;

import static android.R.attr.name;

/**
 * Created by ManjeetSingh on 4/19/2017.
 */

public class ContactCursorAdapter extends CursorAdapter {
    public ContactCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_contact, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView fullName = (TextView)view.findViewById(R.id.fullName);
        TextView mobileNumber = (TextView)view.findViewById(R.id.mobileNumber);
        ImageView avatarView = (ImageView)view.findViewById(R.id.avatar_image_view);
        String firstName = cursor.getString(cursor.getColumnIndexOrThrow(ContactEntry.FIRST_NAME));
        String lastName = cursor.getString(cursor.getColumnIndexOrThrow(ContactEntry.LAST_NAME));
        String mobile = cursor.getString(cursor.getColumnIndexOrThrow(ContactEntry.MOBILE_NUMBER));
        String avatar = cursor.getString(cursor.getColumnIndexOrThrow(ContactEntry.AVATAR_URL));
        String name= firstName + " " + lastName;
        fullName.setText(name);
        mobileNumber.setText(mobile);
        if(avatar !=null || avatar !=""){
            Picasso.with(context)
                    .load(avatar)
                    .placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar)
                    .into(avatarView);
            //avatarView.setImageURI(avatar);
        }
        //petBreed.setText(bred);
    }
}
