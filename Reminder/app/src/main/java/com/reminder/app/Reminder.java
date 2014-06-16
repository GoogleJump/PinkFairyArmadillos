package com.reminder.app;

/**
 * Created by Kylie Williamson on 6/15/14.
 */
public class Reminder {
    private int id;
    protected int urgency;
    protected int longitude;
    protected int latitude;
    protected String reminderText;

    Reminder(String reminder){
        reminderText = reminder;
    }

    public int getUrgency() {
        return urgency;
    }

    public void setUrgency(int urgency) {
        this.urgency = urgency;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLongitude() {
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
}
