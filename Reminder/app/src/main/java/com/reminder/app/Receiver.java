package com.reminder.app;

/**
 * Created by jordanvega on 7/16/14.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class Receiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent service1 = new Intent(context, AlarmService.class);
        Bundle extras = intent.getExtras();
        String message = extras.getString("Reminder Title");
        int ID = extras.getInt("ID");
        service1.putExtra("Reminder Title", message);
        service1.putExtra("ID", ID);
        context.startService(service1);

    }
}