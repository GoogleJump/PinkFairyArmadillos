package com.reminder.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Kylie Williamson on 6/15/2014.
 */
public class ReminderView extends LinearLayout {
    private CheckBox completedCheck;
    private TextView reminderText;
    private Reminder reminder;

    public ReminderView(Context context, Reminder reminder) {
        super(context);
        // defines it by the XML
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.reminder, this, true);
        // find the correct items
        this.reminder = reminder;
        completedCheck = (CheckBox)findViewById(R.id.completedCheck);
        reminderText = (TextView)findViewById(R.id.reminderDisplay);

        setReminder(reminder);
    }

    public void setReminder(Reminder newReminder) {
        reminder = newReminder;
        reminderText.setText(reminder.getTitle());

        requestLayout();
    }
}
