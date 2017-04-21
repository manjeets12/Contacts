package com.contacts;


import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
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

import com.contacts.data.MessageContract.MessageEntry;

/**
 * Created by ManjeetSingh on 4/19/2017.
 */

public class Messages extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private MessageCursorAdapter mCursorAdapter;
    private static final int MESSAGE_LOADER =0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ListView listView= (ListView)rootView.findViewById(R.id.listview);
        View emptyView = rootView.findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);
        mCursorAdapter = new MessageCursorAdapter(getContext(), null, 0);
        listView.setAdapter(mCursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            }
        });
        getLoaderManager().initLoader(0, null, this);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                MessageEntry._ID,
                MessageEntry.COLUMN_USER_NAME,
                MessageEntry.COLUMN_MESSAGE,
                MessageEntry.COLUMN_TIME,
        };
        String sortOrder = MessageEntry.COLUMN_TIME + " DESC";
        return new CursorLoader(getContext(), MessageEntry.CONTENT_URI,projection,null,null,sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
