package com.reminder.app;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import java.util.ArrayList;
import android.util.*;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Kylie Williamson on 6/15/14.
 */
public class ReminderList extends Fragment {
    private ExpandableListView reminderList;
    protected ReminderListAdapter reminderAdapter;
    protected ArrayList<Reminder> reminderArray;
    protected String username;
    private PendingIntent pendingIntent;
    private Context context;
    private Activity activity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.default_page, container, false);
        username = getArguments().getString("username");
        activity = getActivity();
        context = activity.getApplicationContext();
        initLayout(rootView);
        return rootView;
    }

    private void initLayout(View v) {
        reminderList = (ExpandableListView)v.findViewById(R.id.reminderList);
        getReminders();
    }

    public void getReminders() {
        RESTClient.listReminders(getActivity().getApplicationContext(), username ,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray returnedItems = (JSONArray) response.get("items");
                    reminderArray = formatAndCreateRetrievedReminders(returnedItems);
                    createTimedNotification(reminderArray);
                    reminderAdapter = new ReminderListAdapter(activity, reminderArray);
                    reminderList.setAdapter(reminderAdapter);
                } catch (Exception e) {
                    Log.i("Exception: ", e.toString());
                }
            }
            @Override
            public void onFailure(Throwable e, JSONObject errorResponse) {
                String msg = "Object *" + e.toString() + "*" + errorResponse.toString();
                Log.i("testing", "onFailure: " + msg);
            }
        });
    }

    public ArrayList<Reminder> formatAndCreateRetrievedReminders(JSONArray items) {
        ArrayList<Reminder> reminders = new ArrayList<Reminder>();
        for (int i = 0; i < items.length(); i++) {
            try {
                JSONObject toBeReminder = (JSONObject) items.get(i);
                String unformattedDate = toBeReminder.get("datetime").toString();
                Calendar date = getReminderDateTimeFormatted(unformattedDate);

                if(!toBeReminder.isNull("reminder")) {
                    JSONArray remindersJSONArray = (JSONArray) toBeReminder.getJSONArray("reminder");
                    String[] remindersArray = new String[remindersJSONArray.length()];
                    for (int j = 0; j < remindersJSONArray.length(); j++) {
                        remindersArray[j] = remindersJSONArray.get(j).toString();
                    }
                    reminders.add(new Reminder(i, toBeReminder.get("title").toString(), toBeReminder.getInt("urgency"), date, 9.1, 10.0, remindersArray));
                }
                else {
                    reminders.add(new Reminder(i, toBeReminder.get("title").toString(), toBeReminder.getInt("urgency"), date, 9.1, 10.0));
                }
            }
            catch (Exception e) {
                Log.i("Exception:", e.toString());
            }
        }
        return reminders;
    }

    public Calendar getReminderDateTimeFormatted (String dateToFormat)
    {
        try {
            Calendar calendar = Calendar.getInstance();
            String unformattedDate = dateToFormat.replace("T", " ");
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH).parse(unformattedDate));
            return calendar;
        }
        catch (Exception e) {

        }
        return null;
    }

    public void createTimedNotification (ArrayList <Reminder> reminders) {
        Calendar calendar = Calendar.getInstance();
        for(Reminder reminder :reminders) {
            Calendar dueDateTime = reminder.getDueDate();
            calendar.set(Calendar.MONTH, dueDateTime.get(Calendar.MONTH) );
            calendar.set(Calendar.YEAR, dueDateTime.get(Calendar.YEAR));
            calendar.set(Calendar.DAY_OF_MONTH, dueDateTime.get(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.HOUR_OF_DAY, dueDateTime.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, dueDateTime.get(Calendar.MINUTE));
            Intent myIntent = new Intent(activity, Receiver.class);
            myIntent.putExtra("Reminder Title", reminder.getTitle());
            pendingIntent = PendingIntent.getBroadcast(activity, 0, myIntent, 0);
            AlarmManager alarmManager = (AlarmManager)activity.getSystemService(context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
        }
    }
}
