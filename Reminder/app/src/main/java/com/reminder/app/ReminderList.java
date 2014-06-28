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

/**
 * Created by Kylie Williamson on 6/15/14.
 */
public class ReminderList extends Fragment implements View.OnClickListener {
    private ListView reminderList;
    private Button addButton;
    protected ReminderListAdapter reminderAdapter;
    protected ArrayList<Reminder> reminderArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.default_page, container, false);
        initLayout(rootView);
        return rootView;
    }

    private void initLayout(View v) {
        reminderList = (ListView)v.findViewById(R.id.reminderList);
        addButton = (Button)v.findViewById(R.id.newReminderButton);
        addButton.setOnClickListener(this);
        // temporary List of reminders to work with - replaced when we get add functionality and local storage
        Resources object = this.getResources();
        String[] reminderStrings = object.getStringArray(R.array.reminderList);
        // fill the ListArray
        reminderArray = new ArrayList<Reminder>();
        for (String item: reminderStrings){
            Reminder current = new Reminder(item);
            reminderArray.add(current);
        }
        reminderAdapter = new ReminderListAdapter(getActivity(), reminderArray);
        reminderList.setAdapter(reminderAdapter);
    }


    // Not entirely sure this is needed for the final , this'll probably get taken car of on another page
    protected void addReminder(String reminder) {
        Reminder newReminder = new Reminder(reminder);
        reminderArray.add(newReminder);
        reminderAdapter.notifyDataSetChanged();
    }

    public void onClick(View view) {
        if (view.getId() == R.id.newReminderButton)
        {
            //TODO Kylie: can go to add reminder page BUT we can have it be part of the action bar with a + button
        }
    }
}
