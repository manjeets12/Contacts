package com.contacts;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by ManjeetSingh on 4/20/2017.
 */

public class Utils {
    public static final String LOG_TAG = Utils.class.getSimpleName();
    public static final String BASE_API_URL ="https://rest.nexmo.com/sms/json";

    public static String fetchSentMsgResponse(String params) {

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(params, "POST");
        }catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        String result = extractInfoFromJson(jsonResponse);
        return result;
    }

    private static String extractInfoFromJson(String jsonResponse) {
        String result=null;
        if(TextUtils.isEmpty(jsonResponse)){
            return null;
        }
        try {
            JSONObject baseJsonObject = new JSONObject(jsonResponse);
            JSONArray messagesArray = baseJsonObject.getJSONArray("messages");
            if(messagesArray.length() !=0){
                JSONObject messageObj = (JSONObject)messagesArray.get(0);
                result = messageObj.getString("status");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String makeHttpRequest(String params, String type) throws IOException {
        URL url = createUrl(BASE_API_URL);
        String response = null;
        if(url == null) return response;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection= (HttpURLConnection)url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod(type);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            if(type =="POST" && params !=null){
                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream ());
                wr.writeBytes(params);
                Log.e("JSON Input", params);
                wr.flush();
                wr.close();
            }
            urlConnection.connect();
            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                response = readFromStream(inputStream);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
        }finally {
            if(urlConnection !=null){
                urlConnection.disconnect();
            }
            if(inputStream !=null){
                inputStream.close();
            }
        }
        return response;
        
    }

    private static String readFromStream(InputStream inputStream) {
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("utf-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = null;
            try {
                line = reader.readLine();
                while (line !=null){
                    output.append(line);
                    line = reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d(LOG_TAG, output.toString());
        return output.toString();
    }

    private static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }
}
