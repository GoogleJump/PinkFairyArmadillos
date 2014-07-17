package com.reminder.app;

/**
 * Created by jordanvega on 7/16/14.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class Receiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent service1 = new Intent(context, AlarmService.class);
        Bundle extras = intent.getExtras();
        String message = extras.getString("Reminder Title");
        service1.putExtra("Reminder Title", message);
        context.startService(service1);

    }
}