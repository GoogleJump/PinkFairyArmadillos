package com.reminder.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kylie Williamson on 7/10/2014.
 */
public class ReminderDataSource {

    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private String[] allColumns = { SQLiteHelper.COLUMN_ID,
        SQLiteHelper.COLUMN_TEXT, SQLiteHelper.COLUMN_TIME,
        SQLiteHelper.COLUMN_LONGITUDE, SQLiteHelper.COLUMN_LATITUDE,
        SQLiteHelper.COLUMN_URGENCY };

    public ReminderDataSource(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createReminder(Reminder reminder) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_ID, reminder.getId());
        values.put(SQLiteHelper.COLUMN_TEXT, reminder.getReminderText());
        values.put(SQLiteHelper.COLUMN_LATITUDE, reminder.getLatitude());
        values.put(SQLiteHelper.COLUMN_LONGITUDE, reminder.getLongitude());
        values.put(SQLiteHelper.COLUMN_URGENCY, reminder.getUrgency());
        values.put(SQLiteHelper.COLUMN_TIME, reminder.getReminderTime().toString());
    }

    public ArrayList<Reminder> getAllReminders() {
        ArrayList<Reminder> reminders = new ArrayList<Reminder>();
        Cursor cursor = database.query(SQLiteHelper.TABLE_REMINDER, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Reminder current = cursorToReminder(cursor);
            reminders.add(current);
            cursor.moveToNext();
        }
        cursor.close();
        return reminders;
    }

    private Reminder cursorToReminder(Cursor cursor) {
        Reminder current = new Reminder(cursor.getString(1));
        current.setId(cursor.getInt(0));
        current.setLatitude(cursor.getInt(2));
        current.setLongitude(cursor.getInt(3));
        current.setUrgency(cursor.getInt(4));
        //current.setReminderTime(cursor.getString(5));
        return current;
    }

    //TODO: (Kylie) add delete and implement in main code
}
