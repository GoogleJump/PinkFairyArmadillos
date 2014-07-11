package com.reminder.app;

import android.content.res.Resources;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import android.util.*;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Kylie Williamson on 6/15/14.
 */
public class ReminderList extends Fragment {
    private ListView reminderList;
    protected ReminderListAdapter reminderAdapter;
    protected ArrayList<Reminder> reminderArray;
    protected String username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.default_page, container, false);
        username = getArguments().getString("username");
        initLayout(rootView);
        return rootView;
    }

    private void initLayout(View v) {
        reminderList = (ListView)v.findViewById(R.id.reminderList);
        getReminders();
    }

    // Not entirely sure this is needed for the final , this'll probably get taken car of on another page
    protected void addReminder(String reminder) {
        Reminder newReminder = new Reminder(reminder);
        reminderArray.add(newReminder);
        reminderAdapter.notifyDataSetChanged();
    }

    public void getReminders() {
        final ArrayList<Reminder> reminders = new ArrayList<Reminder>();
        Reminder r = new Reminder("First Entry test");
        reminders.add(r);
        RESTClient.listReminders(getActivity().getApplicationContext(), username ,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray returnedItems = (JSONArray) response.get("items");
                    for (int i = 0; i < returnedItems.length(); i++) {
                        JSONObject toBeReminder = (JSONObject) returnedItems.get(i);
                        reminders.add(new Reminder(i, toBeReminder.get("title").toString(), toBeReminder.getInt("urgency"), 9.1, 10.0, toBeReminder.get("reminder").toString()));
                    }
                    reminderArray = reminders;
                    reminderAdapter = new ReminderListAdapter(getActivity(), reminderArray);
                    reminderList.setAdapter(reminderAdapter);
                } catch (JSONException e) {
                    Log.i("JSON Exception: ", e.toString());
                }
            }
            @Override
            public void onFailure(Throwable e, JSONObject errorResponse) {
                String msg = "Object *" + e.toString() + "*" + errorResponse.toString();
                Log.i("testing", "onFailure: " + msg);
            }
        });
    }
}
