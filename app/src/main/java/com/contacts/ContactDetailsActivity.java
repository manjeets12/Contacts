package com.contacts;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.app.LoaderManager;
import android.content.Loader;
import android.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.contacts.data.ContactContract.ContactEntry;

import static android.R.attr.name;

public class ContactDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int EXISTING_CONTACT_LOADER = 0;
    private TextView mNameText;
    private TextView mMobileText;
    private Button mSendButton;

    ContactCursorAdapter mCursorAdapter;
    Uri mCurrentContactUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        Intent intent= getIntent();
        mCurrentContactUri = intent.getData();
        if(mCurrentContactUri !=null){
            getLoaderManager().initLoader(EXISTING_CONTACT_LOADER, null, this);
        }

        //find all relevent views here
        mNameText = (TextView) findViewById(R.id.textName);
        mMobileText = (TextView) findViewById(R.id.textMobile);
        mSendButton = (Button) findViewById(R.id.btnSendMessage);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSendMessageActivity();
            }
        });
        mCursorAdapter = new ContactCursorAdapter(this,null,EXISTING_CONTACT_LOADER);
    }

    private void openSendMessageActivity() {
        String name = mNameText.getText().toString().trim();
        String mobile = mMobileText.getText().toString().trim();
        if(mobile != null || mobile != ""){
            Intent intent = new Intent(this, ComposeMessgaeActivity.class);
            intent.putExtra("NAME", name);
            intent.putExtra("MOBILE", mobile);
            startActivity(intent);
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,mCurrentContactUri,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor ==null || cursor.getCount()< 1){
            return;
        }
        setCurrentContact(cursor);
    }

    private void setCurrentContact(Cursor cursor) {
        if(cursor.moveToFirst()){
            int idColumnIndex = cursor.getColumnIndex(ContactEntry._ID);
            int firstNameColumIndex = cursor.getColumnIndex(ContactEntry.FIRST_NAME);
            int lastNameColumIndex = cursor.getColumnIndex(ContactEntry.LAST_NAME);
            int avatarColumnIndex = cursor.getColumnIndex(ContactEntry.AVATAR_URL);
            int mobleNumberColumnIndex = cursor.getColumnIndex(ContactEntry.MOBILE_NUMBER);
            String firstName = cursor.getString(firstNameColumIndex);
            String lastName = cursor.getString(lastNameColumIndex);
            String avatar = cursor.getString(avatarColumnIndex);
            String mobileNumber = cursor.getString(mobleNumberColumnIndex);

            String name = firstName;
            if(lastName !=null){
                name = name + " "+lastName;
            }
            mNameText.setText(name.trim());
            mMobileText.setText(mobileNumber.trim());

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameText.setText("");
        mMobileText.setText("");
    }
}
