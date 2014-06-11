package com.reminder.app;

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
        JSONObject jsonParams = new JSONObject();
        JSONArray list = new JSONArray(Arrays.asList(reminderList));

        StringEntity entity;
        try {
            jsonParams.put("username", username);
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
    public static ArrayList listReminders(Context context, String user){
        RequestParams params = new RequestParams();
        params.put("username",user);
        client.get(context, REMINDER_URL + "list", params, new JsonHttpResponseHandler()  {
                @Override
                public void onSuccess(JSONObject response){
                        Log.i("testing", response.toString());
                        //urgency
                }
                @Override
                public void onFailure ( Throwable e, JSONObject errorResponse ) {
                    String msg = "Object *" + e.toString() + "*" + errorResponse.toString();
                    Log.i("testing","onFailure: " + msg);
                }
            });
        return null;
    }
}

