package com.reminder.app;

/**
 * Created by Kylie Williamson on 6/15/14.
 */
public class Reminder {
    private int id;
    protected int urgency;
    protected double longitude;
    protected double latitude;
    protected String reminderText;
    protected String title;

    Reminder(String reminder) {
        title = reminder;
    }

    Reminder(int id, String title, int urgency, double longitude, double latitude, String reminderText) {
        this.id = id;
        this.urgency = urgency;
        this.longitude = longitude;
        this.latitude = latitude;
        this.reminderText = reminderText;
        this.title = title;
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

    public String getReminderText() {
        return reminderText;
    }

    public void setReminderText(String reminderText) {
        this.reminderText = reminderText;
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
}


