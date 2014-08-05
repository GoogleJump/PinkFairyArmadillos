package com.reminder.app;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddReminder extends Activity {

    private Button addButton;

    private LocationManager locationManager;
    private static final String POINT_LATITUDE_KEY = "POINT_LATITUDE_KEY";
    private static final String POINT_LONGITUDE_KEY = "POINT_LONGITUDE_KEY";

    private float LNG = (float) -122.095055;
    private float LAT = (float) 37.42446;

    private static final long POINT_RADIUS = 100; // in Meters
    private static final long PROX_ALERT_EXPIRATION = -1;

    private static final String PROX_ALERT_INTENT =
            "com.reminder.app.AddReminder";

    private objectListener addListener = new objectListener();

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        initLayout();
        addActionListeners();
    }

    private void initLayout() {
        setContentView(R.layout.add_page);


        addButton = (Button)findViewById(R.id.addButton);

        //location stuff
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    private class objectListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(v == addButton) {
                //TODO(Kylie): add if for non-populated location
                saveProximityAlertPoint();
                //Reminder current = new Reminder(reminderText.getText().toString());
                //current.setUrgency(urgency.getScrollX());
                //current.setLongitude(reminderText.getText());

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                //using this until we get local storage
                //i.putExtra("remindersBack", reminderText.getText().toString());
                startActivity(i);
            }
        }
    }
    private void addActionListeners() {
        addButton.setOnClickListener(addListener);
    }

    /*
    This next section deals with implementing the proximity alerts.
     */
    private void saveProximityAlertPoint() {
        /*Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location==null) {
            Toast.makeText(this, "No last known location. Aborting...",
                    Toast.LENGTH_LONG).show();
            return;
        }*/
        saveCoordinatesInPreferences(LAT,LNG);
        addProximityAlert(LAT,LNG);
    }

    private void addProximityAlert(double latitude, double longitude) {
        Intent intent = new Intent(PROX_ALERT_INTENT);
        PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        locationManager.addProximityAlert(
                latitude, // the latitude of the central point of the alert region
                longitude, // the longitude of the central point of the alert region
                POINT_RADIUS, // the radius of the central point of the alert region, in meters
                PROX_ALERT_EXPIRATION, // time for this proximity alert, in milliseconds, or -1 to indicate no expiration
                proximityIntent // will be used to generate an Intent to fire when entry to or exit from the alert region is detected
        );
        IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
        registerReceiver(new ProximityIntentReceiver(), filter);
    }


    private void saveCoordinatesInPreferences(float latitude, float longitude) {
        SharedPreferences prefs =
        this.getSharedPreferences(getClass().getSimpleName(),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putFloat(POINT_LATITUDE_KEY, latitude);
        prefsEditor.putFloat(POINT_LONGITUDE_KEY, longitude);
        prefsEditor.commit();
    }

}
