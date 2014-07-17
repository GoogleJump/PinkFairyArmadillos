package com.reminder.app;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Kylie Williamson on 6/15/14.
 */
//Parcelable should be deleted when we get local storage
public class ReminderList extends Activity {
    private ListView reminderList;
    private Button addButton;
    protected ReminderListAdapter reminderAdapter;
    protected ArrayList<Reminder> reminderArray;

    protected static ReminderDataSource localStorage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        addActionListeners();
    }

    private void initLayout() {
        setContentView(R.layout.default_page);
        reminderList = (ListView)findViewById(R.id.reminderList);
        addButton = (Button)findViewById(R.id.newReminderButton);
        // temporary List of reminders to work with - replaced when we get add functionality and local storage
        Resources object = this.getResources();
        String[] reminderStrings = object.getStringArray(R.array.reminderList);
        // fill the ListArray
        reminderArray = new ArrayList<Reminder>();
        for (String item: reminderStrings){
            Reminder current = new Reminder(item);
            reminderArray.add(current);
        }
        reminderAdapter = new ReminderListAdapter(this, reminderArray);
        reminderList.setAdapter(reminderAdapter);

        /*localStorage = new ReminderDataSource(getApplicationContext());
        try {
            localStorage.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //reminderArray = (ArrayList<Reminder>)extras.getParcelableArrayList("arrayReminder");
        }

        reminderArray = localStorage.getAllReminders();
        localStorage.close();*/
    }

    // Not entirely sure this is needed for the final , this'll probably get taken care of in another page
    protected void addReminder(String reminder) {
        Reminder newReminder = new Reminder(reminder);
        reminderArray.add(newReminder);
        reminderAdapter.notifyDataSetChanged();
    }

    // various action listeners for the widgets
    protected void addActionListeners() {
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddReminder.class);
                startActivity(i);
            }
        });
    }

    /*//temp solution for saving and retreiving reminders until we get local storage or the such
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //savedInstanceState.put
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }*/
}
