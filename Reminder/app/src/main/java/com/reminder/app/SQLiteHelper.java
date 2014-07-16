package com.reminder.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Kylie Williamson on 7/10/2014.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_REMINDER = "reminder";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_URGENCY = "urgency";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_TIME = "time";

    private static final String DATABASE_NAME = "reminder.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table "
            + TABLE_REMINDER + "(" + COLUMN_ID
            + " integer primary key," + COLUMN_URGENCY
            + " integer, " + COLUMN_LATITUDE
            + " integer, " + COLUMN_LONGITUDE
            + " integer, " + COLUMN_TEXT
            + " text not null, " + COLUMN_TIME
            + " text);";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        Log.w(SQLiteHelper.class.getName(),
                "Upgrading database from version " + i + " to "
                + i2 + ", which will destroy all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDER);
        onCreate(sqLiteDatabase);
    }
}
