package com.contacts;



import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.contacts.data.ContactContract.ContactEntry;

import static android.R.attr.data;

public class EditContactActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int EXISTING_CONTACT_LOADER = 0;
    /** EditText field to enter the pet's name */
    private EditText mFirstNameEditText;

    /** EditText field to enter the pet's breed */
    private EditText mLastNameEditText;

    /** EditText field to enter the pet's weight */
    private EditText mMobileNumberEditText;

    ContactCursorAdapter mCursorAdapter;
    Uri mCurrentContactUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        mFirstNameEditText = (EditText)findViewById(R.id.edit_first_name);
        mLastNameEditText = (EditText)findViewById(R.id.edit_last_name);
        mMobileNumberEditText = (EditText)findViewById(R.id.edit_mobile_number);
        Button saveButton = (Button)findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveContact();
            }
        });
        Intent intent= getIntent();
        mCurrentContactUri = intent.getData();
        if(mCurrentContactUri == null){
            setTitle("Add Contact");
        }

    }

    private void saveContact() {
        String strFirstName = mFirstNameEditText.getText().toString().trim();
        String strLastName = mLastNameEditText.getText().toString().trim();
        String strMobileNumber = mMobileNumberEditText.getText().toString().trim();
        ContentValues values = new ContentValues();
        values.put(ContactEntry.FIRST_NAME, strFirstName);
        values.put(ContactEntry.LAST_NAME, strLastName);
        values.put(ContactEntry.MOBILE_NUMBER,strMobileNumber);

        if(mCurrentContactUri !=null){
            int rowsAffected = getContentResolver().update(mCurrentContactUri, values, null, null);
            if(rowsAffected <1){
                Toast.makeText(EditContactActivity.this, R.string.editor_insert_contact_failed, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(EditContactActivity.this, R.string.editor_insert_contact_successful, Toast.LENGTH_SHORT).show();
            }
        }else {
            Uri uri = getContentResolver().insert(ContactEntry.CONTENT_URI,values);
            if(uri==null){
                Toast.makeText(EditContactActivity.this, R.string.editor_insert_contact_failed, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(EditContactActivity.this, R.string.editor_insert_contact_successful, Toast.LENGTH_SHORT).show();
            }
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
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFirstNameEditText.setText("");
        mLastNameEditText.setText("");
        mMobileNumberEditText.setText("");
    }
}
