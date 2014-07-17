package com.reminder.app;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by Kylie Williamson on 6/15/14.
 */
public class Reminder {
    private int id;
    protected int urgency;
    protected double longitude;
    protected double latitude;
    protected String[] reminders;
    protected String title;
    protected Calendar dueDate;

    Reminder(String reminder) {
        title = reminder;
    }

    Reminder(int id, String title, int urgency, Calendar dueDate, double longitude, double latitude, String[] reminders) {
        this.id = id;
        this.urgency = urgency;
        this.longitude = longitude;
        this.latitude = latitude;
        this.reminders = reminders;
        this.title = title;
        this.dueDate = dueDate;
    }

    Reminder(int id, String title, int urgency, Calendar dueDate, double longitude, double latitude) {
        this.id = id;
        this.urgency = urgency;
        this.longitude = longitude;
        this.latitude = latitude;
        this.title = title;
        this.dueDate = dueDate;
    }

    public int getUrgency() {
        return urgency;
    }

    public void setUrgency(int urgency) {
        this.urgency = urgency;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getReminders() {
        return reminders;
    }

    public Calendar getDueDate() {
        return dueDate;
    }
}


