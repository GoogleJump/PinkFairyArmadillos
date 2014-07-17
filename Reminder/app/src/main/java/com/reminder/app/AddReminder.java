package com.reminder.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TimePicker;

import java.util.ArrayList;

/**
 * Created by Kylie Williamson on 7/16/2014.
 */
public class AddReminder extends Activity {
    private EditText reminderText;
    private EditText longitude;
    private EditText latitude;
    private SeekBar urgency;
    private TimePicker reminderTime;
    private Button addButton;

    private objectListener addListener = new objectListener();

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        initLayout();
        addActionListeners();
    }

    private void initLayout() {
        setContentView(R.layout.add_page);

        reminderText = (EditText)findViewById(R.id.reminderText);
        longitude = (EditText)findViewById(R.id.longitude);
        latitude = (EditText)findViewById(R.id.latitude);
        urgency = (SeekBar)findViewById(R.id.urgencySlider);
        reminderTime = (TimePicker)findViewById(R.id.reminderTime);
        addButton = (Button)findViewById(R.id.addButton);
    }

    private class objectListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(v == addButton) {
                //Reminder current = new Reminder(reminderText.getText().toString());
                //current.setUrgency(urgency.getScrollX());
                //current.setLongitude(reminderText.getText());

                Intent i = new Intent(getApplicationContext(), ReminderList.class);
                //using this until we get local storage
                //i.putExtra("remindersBack", reminderText.getText().toString());
                startActivity(i);
            }
        }
    }
    private void addActionListeners() {
        addButton.setOnClickListener(addListener);
    }
}
