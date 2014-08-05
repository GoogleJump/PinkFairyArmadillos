package com.reminder.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.PowerManager;
import android.util.Log;
import android.os.PowerManager.WakeLock;

/**
 * Created by Kylie Williamson on 7/17/2014.
 */
public class ProximityIntentReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 1000;
    private NotificationManager mManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        String key = LocationManager.KEY_PROXIMITY_ENTERING;

        Boolean entering = intent.getBooleanExtra(key, false);

        if (entering) {
            Log.d("testing", "entering");
        }
        else {
            Log.d("testing", "exiting");
        }

        mManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(context,MainActivity.class);
        Notification notification = new Notification.Builder(context)
                .setContentTitle("Reminder!")
                .setContentText("Near place")
                .setSmallIcon(R.drawable.ic_launcher).build();

        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
        WakeLock screenOn = ((PowerManager)context.getSystemService(context.POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "example");
        screenOn.acquire();
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity( context, 0, intent1 ,PendingIntent.FLAG_UPDATE_CURRENT);
        mManager.notify(100, notification);
    }

    private Notification createNotification() {
        Notification notification = new Notification();

        //notification.icon?
       notification.when = System.currentTimeMillis();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;

        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_LIGHTS;

        notification.ledARGB = Color.WHITE;
        notification.ledOnMS = 1500;
        notification.ledOffMS = 1500;

        return notification;
    }
}
