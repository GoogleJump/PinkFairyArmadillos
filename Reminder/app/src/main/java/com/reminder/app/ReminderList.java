package com.reminder.app;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Kylie Williamson on 6/15/14.
 */
public class ReminderList extends Activity {
    private ListView reminderList;
    private Button addButton;
    protected ReminderListAdapter reminderAdapter;
    protected ArrayList<Reminder> reminderArray;

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
    }


    // Not entirely sure this is needed for the final , this'll probably get taken car of on another page
    protected void addReminder(String reminder) {
        Reminder newReminder = new Reminder(reminder);
        reminderArray.add(newReminder);
        reminderAdapter.notifyDataSetChanged();
    }

    // various action listeners for the widgets
    protected void addActionListeners() {
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //TODO(Kylie): Button click moves to add reminder page
            }
        });
    }
}
