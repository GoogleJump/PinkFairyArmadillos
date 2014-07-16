package com.reminder.app;

import android.content.Context;
import com.loopj.android.http.*;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.Arrays;
import android.util.Log;

public class RESTClient {
    private static final String REMINDER_URL = "https://flash-energy-585.appspot.com/_ah/api/reminders/v1/reminders/";
    private static final String CREATE_REMINDER_URL = "createreminder";
    private static final String LIST_REMINDER_URL = "list";
    private static final String DISTANCE_MATRIX_KEY = "AIzaSyCg2Pjekd0kFzFuFjmyZZsPkxlHaUDatBg";
    private static final String DISTANCE_MATRIX_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?";

    private static AsyncHttpClient client = new AsyncHttpClient();


    public static void newReminder(Context context, String username, String title, String[] reminderList, double latitude, double longitude, int urgency) {
        JSONObject jsonParams = new JSONObject();
        JSONArray list = new JSONArray(Arrays.asList(reminderList));
        StringEntity entity;
        try {
            jsonParams.put("username", username);
            jsonParams.put("title", title);
            jsonParams.put("latitude", latitude);
            jsonParams.put("longitude", longitude);
            jsonParams.put("reminder",list);
            jsonParams.put("urgency", urgency);
            entity = new StringEntity(jsonParams.toString());
            client.post(context, REMINDER_URL+ CREATE_REMINDER_URL, entity, "application/json", new JsonHttpResponseHandler() { });
        }
        catch (Exception e) {
            System.out.print(e.toString());
        }

    }
    public static void listReminders(Context context, String user, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("username",user);
        client.get(context, REMINDER_URL + LIST_REMINDER_URL, params, handler);
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

