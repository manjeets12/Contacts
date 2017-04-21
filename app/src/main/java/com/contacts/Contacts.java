package com.contacts;



import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.contacts.data.ContactContract.ContactEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ManjeetSingh on 4/19/2017.
 */

public class Contacts extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    //private OnFragmentInteractionListener mListener;
    private ContactCursorAdapter mCursorAdapter;
    private static final int CONTATC_LOADER =0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View rootView =null;
        //if(container ==null){
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ListView contactListView= (ListView)rootView.findViewById(R.id.listview);
            View emptyView = rootView.findViewById(R.id.empty_view);
            contactListView.setEmptyView(emptyView);
            mCursorAdapter = new ContactCursorAdapter(getContext(), null, 0);
            contactListView.setAdapter(mCursorAdapter);
            contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    final Uri currentUri = ContentUris.withAppendedId(ContactEntry.CONTENT_URI,id);
                    Intent intent = new Intent(getContext(), ContactDetailsActivity.class);
                    intent.setData(currentUri);
                    startActivity(intent);
                }
            });
            getLoaderManager().initLoader(0, null, this);


        //}
        return rootView;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ContactEntry._ID,
                ContactEntry.FIRST_NAME,
                ContactEntry.LAST_NAME,
                ContactEntry.AVATAR_URL,
                ContactEntry.MOBILE_NUMBER
        };
        return new CursorLoader(getContext(), ContactEntry.CONTENT_URI,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
        if(cursor !=null && cursor.getCount()<1){
            addDummyContacts();
        }

    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    private void addDummyContacts() {
        String contactStr ="{\n  \"contacts\": [\n    {\n      \"id\": \"5\",\n      \"firstName\": \"Kisan\",\n      \"lastName\": \"Network\",\n      \"photo\": \"http://georgesjournal.files.wordpress.com/2012/02/007_at_50_ge_pierece_brosnan.jpg\",\n      \"mobile\": \"919971792703\"\n    },\n    {\n      \"id\": \"1\",\n      \"firstName\": \"Tom\",\n      \"lastName\": \"Cruise\",\n      \"photo\": \"http://cdn2.gossipcenter.com/sites/default/files/imagecache/story_header/photos/tom-cruise-020514sp.jpg\",\n      \"mobile\": \"918960243625\"\n    },\n    {\n      \"id\": \"2\",\n      \"firstName\": \"Maria\",\n      \"lastName\": \"Sharapova\",\n      \"photo\": \"http://thewallmachine.com/files/1363603040.jpg\",\n      \"mobile\": \"919012970979\"\n    },\n    {\n      \"id\": \"3\",\n      \"firstName\": \"James\",\n      \"lastName\": \"Bond\",\n      \"photo\": \"http://georgesjournal.files.wordpress.com/2012/02/007_at_50_ge_pierece_brosnan.jpg\",\n      \"mobile\": \"9181362715052\"\n    },\n    {\n      \"id\": \"4\",\n      \"firstName\": \"Kamaljeet\",\n      \"lastName\": \"Singh\",\n      \"photo\": \"http://georgesjournal.files.wordpress.com/2012/02/007_at_50_ge_pierece_brosnan.jpg\",\n      \"mobile\": \"919756214995\"\n    },\n    {\n      \"id\": \"5\",\n      \"firstName\": \"DUMMY\",\n      \"lastName\": \"NUMBER\",\n      \"photo\": \"http://georgesjournal.files.wordpress.com/2012/02/007_at_50_ge_pierece_brosnan.jpg\",\n      \"mobile\": \"919971792703\"\n    },\n    {\n      \"id\": \"6\",\n      \"firstName\": \"TEST1\",\n      \"lastName\": \"NUMBER\",\n      \"photo\": \"http://georgesjournal.files.wordpress.com/2012/02/007_at_50_ge_pierece_brosnan.jpg\",\n      \"mobile\": \"919971792704\"\n    },\n    {\n      \"id\": \"7\",\n      \"firstName\": \"TEST2\",\n      \"lastName\": \"NUMBER\",\n      \"photo\": \"http://georgesjournal.files.wordpress.com/2012/02/007_at_50_ge_pierece_brosnan.jpg\",\n      \"mobile\": \"919971792709\"\n    },\n    {\n      \"id\": \"8\",\n      \"firstName\": \"TEST1\",\n      \"lastName\": \"NUMBER\",\n      \"photo\": \"http://georgesjournal.files.wordpress.com/2012/02/007_at_50_ge_pierece_brosnan.jpg\",\n      \"mobile\": \"919971792704\"\n    },\n    {\n      \"id\": \"9\",\n      \"firstName\": \"TEST2\",\n      \"lastName\": \"NUMBER\",\n      \"photo\": \"http://georgesjournal.files.wordpress.com/2012/02/007_at_50_ge_pierece_brosnan.jpg\",\n      \"mobile\": \"919971792709\"\n    },\n    {\n      \"id\": \"10\",\n      \"firstName\": \"TEST1\",\n      \"lastName\": \"NUMBER\",\n      \"photo\": \"http://georgesjournal.files.wordpress.com/2012/02/007_at_50_ge_pierece_brosnan.jpg\",\n      \"mobile\": \"919971792704\"\n    },\n    {\n      \"id\": \"11\",\n      \"firstName\": \"TEST2\",\n      \"lastName\": \"NUMBER\",\n      \"photo\": \"http://georgesjournal.files.wordpress.com/2012/02/007_at_50_ge_pierece_brosnan.jpg\",\n      \"mobile\": \"919971792709\"\n    }\n  ]\n}";
        try {
            JSONObject baseJsonObject = new JSONObject(contactStr);
            JSONArray contactsArray = baseJsonObject.getJSONArray("contacts");
            if(contactsArray.length() !=0){
                for(int i=0; i<contactsArray.length(); i++){
                    JSONObject obj = contactsArray.getJSONObject(i);
                    ContentValues values = new ContentValues();
                    values.put(ContactEntry.FIRST_NAME, obj.getString("firstName"));
                    values.put(ContactEntry.LAST_NAME, obj.getString("lastName"));
                    values.put(ContactEntry.MOBILE_NUMBER,obj.getString("mobile"));
                    values.put(ContactEntry.AVATAR_URL, obj.getString("photo"));
                    getActivity().getApplicationContext().getContentResolver().insert(ContactEntry.CONTENT_URI,values); //inserting to database
                }
            }
           // db.close();
        } catch (JSONException e) {
           // db.close();
            e.printStackTrace();
        }
    }

}
