package com.contacts;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.contacts.data.MessageContract.MessageEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Random;

import static android.R.attr.key;
import static com.contacts.Utils.BASE_API_URL;

public class ComposeMessgaeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String LOG_TAG = Utils.class.getSimpleName();
    private String mMobile;
    private String mName;
    private ContentValues mValues;
    private TextView mMessgaeToText;
    private TextView mOtpText;
    private Context mContext;
    Button btnSendMessage;
    Uri mCurrentMessageUri;

    private static final String API_KEY = "05684377";
    private static final String API_SECRET ="4213c5c6c9da185c";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_messgae);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        mContext=this;
        Intent intent= getIntent();
        mName = intent.getStringExtra("NAME");
        mMobile = intent.getStringExtra("MOBILE");
        mMessgaeToText =(TextView)findViewById(R.id.textTo);
        mOtpText =(TextView)findViewById(R.id.textMessgae) ;
        btnSendMessage  =(Button)findViewById(R.id.btnSendMessage);
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               sendMessage();
            }
        });
        setValues();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendMessage() {
        String name = mMessgaeToText.getText().toString().trim();
        String mMessage = mOtpText.getText().toString().trim();
        mValues = new ContentValues();
        mValues.put(MessageEntry.COLUMN_USER_NAME, name);
        mValues.put(MessageEntry.COLUMN_MESSAGE, mMessage);
        Calendar rightNow = Calendar.getInstance();
        long time = rightNow.getTimeInMillis();
        mValues.put(MessageEntry.COLUMN_TIME,time);
        JSONObject params = new JSONObject();
        try {
            params.put("api_key", API_KEY);
            params.put("api_secret", API_SECRET);
            params.put("type", "text");
            params.put("to", mMobile);
            params.put("from", "Manjeet Singh");
            params.put("text", mMessage);
            SendMessageAsyncTask task = new SendMessageAsyncTask();
            task.execute(params.toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
       /* String url = BASE_API_URL +
                "?api_key=" + API_KEY +
                "&api_secret=" +API_SECRET+
                "&to="+mMobile+
                "&from=Manjeet Singh"+
                "&text="+mMessage+
                "&type=text";*/


    }

    private void setValues() {
        Random rnd = new Random();
        int sixDigit = 100000 + rnd.nextInt(900000);
        if((mName != null || mName != "") && (mMobile!=null ||mMobile !="")){
            String name = mName + " ("+mMobile+")";
            String otp = "Hi. Your OTP is: "+sixDigit;
            mMessgaeToText.setText(name);
            mOtpText.setText(otp);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,mCurrentMessageUri,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor ==null || cursor.getCount()< 1){
            updateMessageData();
        }
    }

    private void updateMessageData() {
        if(mValues !=null){
            if(mCurrentMessageUri !=null){
                int rowsAffected = getContentResolver().update(mCurrentMessageUri, mValues, null, null);
                if(rowsAffected <1){
                    Toast.makeText(ComposeMessgaeActivity.this, R.string.message_sent_successful, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ComposeMessgaeActivity.this, R.string.message_sent_failed, Toast.LENGTH_SHORT).show();
                }
            }else {
                Uri uri = getContentResolver().insert(MessageEntry.CONTENT_URI,mValues);
                if(uri==null){
                    Toast.makeText(ComposeMessgaeActivity.this, R.string.message_sent_failed, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ComposeMessgaeActivity.this, R.string.message_sent_successful, Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMessgaeToText.setText("");
        mOtpText.setText("");
        mName =null;
        mMobile = null;
    }

    private class SendMessageAsyncTask extends AsyncTask<String, Void, String> {
        private ProgressDialog mProgressDialog;
        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setTitle("Please Wait");
            mProgressDialog.setMessage("Fetching Data...");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            if(params.length < 1 || params[0] == null){
                return null;
            }
            return Utils.fetchSentMsgResponse(params[0]);

        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if(data == null){
                return;
            }
            Log.d(LOG_TAG, "status- "+data);
            updateMessageData();
        }
    }
}
