package com.reminderapp.app;

/**
 * Created by jordanvega on 5/24/14.
 */

import android.content.Context;

import com.loopj.android.http.*;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;


import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import android.util.Log;



public class RESTClient {
    private static final String REMINDER_URL = "https://flash-energy-585.appspot.com/_ah/api/reminders/v1/reminders/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void newReminder(Context context, String username, String title, String[] reminderList, double latitude, double longitude) {
        RequestParams p = new RequestParams();
        JSONObject jsonParams = new JSONObject();
        JSONArray list = new JSONArray(Arrays.asList(reminderList));

        StringEntity entity;
        try {
            jsonParams.put("user", username);
            jsonParams.put("title", title);
            jsonParams.put("latitude", latitude);
            jsonParams.put("longitude", longitude);
            jsonParams.put("reminder",list);
        }
        catch (JSONException e)
        {
            System.out.print(e.toString());
        }
        try{
            entity = new StringEntity(jsonParams.toString());

            client.post(context, REMINDER_URL+"createreminder" ,entity, "application/json", new JsonHttpResponseHandler() { });
        }
        catch (UnsupportedEncodingException e)
        {
            System.out.print(e.toString());
        }

    }
    public static ArrayList listReminders(Context context){
    ArrayList<String> list = new ArrayList<String>();
     client.get(context, REMINDER_URL + "list", new JsonHttpResponseHandler()  {
         @Override
         public void onSuccess(JSONObject response){
             try {
                JSONArray items = response.getJSONArray("items");
                 System.out.println(items.getJSONObject(1).get("reminder"));
             }
             catch (JSONException e){
                 System.out.println(e.toString());
             }
         }

         @Override
         public void onFailure ( Throwable e, JSONObject errorResponse ) {
             String msg = "Object *" + e.toString() + "*" + errorResponse.toString();
             Log.i("testing","onFailure: " + msg);
         }
     });
        return list;
    }
 }

