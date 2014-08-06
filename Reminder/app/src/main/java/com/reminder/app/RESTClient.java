package com.reminder.app;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

public class RESTClient {
    private static final String REMINDER_URL = "https://flash-energy-585.appspot.com/_ah/api/reminders/v1/reminders/";
    private static final String CREATE_REMINDER_URL = "createreminder";
    private static final String DELETE_REMINDER_URL = "delete/";
    private static final String LISTDONE_REMINDER_URL = "listdone";
    private static final String LISTUPCOMING_REMINDER_URL = "listupcoming";
    private static final String LIST_REMINDER_URL = "list";
    private static final String DISTANCE_MATRIX_KEY = "AIzaSyCg2Pjekd0kFzFuFjmyZZsPkxlHaUDatBg";
    private static final String DISTANCE_MATRIX_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?";

    private static AsyncHttpClient client = new AsyncHttpClient();


    public static void newReminder( Context context, String username, String title, String[] reminderList, double latitude, double longitude, int urgency, Calendar cal) {
        JSONObject jsonParams = new JSONObject();
        JSONArray list = null;
        if(reminderList != null) {
            list = new JSONArray(Arrays.asList(reminderList));
        }
        StringEntity entity;
        try {
            jsonParams.put("username", username);
            jsonParams.put("title", title);
            jsonParams.put("latitude", latitude);
            jsonParams.put("longitude", longitude);
            jsonParams.put("reminder", list);
            jsonParams.put("urgency", urgency);
            entity = new StringEntity(jsonParams.toString());
            client.post(context, REMINDER_URL+ CREATE_REMINDER_URL, entity, "application/json", new JsonHttpResponseHandler() { });
            if(cal != null) {
                makeDateGoAvailable(cal);
            }
        }
        catch (Exception e) {
            System.out.print(e.toString());
        }

    }

    private static String makeDateGoAvailable(Calendar cal) {
        TimeZone t = cal.getTimeZone();
        String offset = String.valueOf(t.getRawOffset()/3600);
        offset = offset.substring(0,1) + "0" + offset.substring(1,offset.length()-1);
        return  offset;
    }

    public static void deleteReminder (Context context, String ID) {
        Log.i("testing", ID);
        client.delete(context, REMINDER_URL + DELETE_REMINDER_URL + ID, new JsonHttpResponseHandler(){ });
    }

    public static void listReminders(Context context, String user, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("username",user);
        client.get(context, REMINDER_URL + LIST_REMINDER_URL, params, handler);
    }

    public static void listDoneReminders(Context context, String user, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("username",user);
        client.get(context, REMINDER_URL + LISTDONE_REMINDER_URL, params, handler);
    }

    public static void listUpcomingReminders(Context context, String user, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("username",user);
        client.get(context, REMINDER_URL + LISTUPCOMING_REMINDER_URL, params, handler);
    }

    public static void getDrivingTime(Context context, String origin, String destination, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("origins", origin);
        params.put("destinations", destination);
        params.put("mode", "driving");
        params.put("language", "en");
        params.put("key", DISTANCE_MATRIX_KEY);
        client.get(context, DISTANCE_MATRIX_URL,params,handler);
    }

}

