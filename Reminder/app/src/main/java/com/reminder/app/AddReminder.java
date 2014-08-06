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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AddReminder extends Activity {

    private Button addButton;
    private EditText reminderText;
    private SeekBar urgency;
    private Switch timeSwitch, locationSwitch;
    private TimePicker time;
    private DatePicker date;

    protected String username;

    private LocationManager locationManager;
    private static final String POINT_LATITUDE_KEY = "POINT_LATITUDE_KEY";
    private static final String POINT_LONGITUDE_KEY = "POINT_LONGITUDE_KEY";

    //private float LNG = (float) -122.095055;
    //private float LAT = (float) 37.42446;

    private static final long POINT_RADIUS = 100; // in Meters
    private static final long PROX_ALERT_EXPIRATION = -1;

    private static final String PROX_ALERT_INTENT =
            "com.reminder.app.AddReminder";

    private objectListener addListener = new objectListener();
    private onToggleClicked switchListener = new onToggleClicked();

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");

        initLayout();
        addActionListeners();
    }

    private void initLayout() {
        setContentView(R.layout.add_page);

        reminderText = (EditText)findViewById(R.id.ReminderTextField);
        addButton = (Button)findViewById(R.id.addButton);
        urgency = (SeekBar)findViewById(R.id.UrgencyBar);
        time = (TimePicker)findViewById(R.id.timePicker);
        date = (DatePicker)findViewById(R.id.datePicker);

        timeSwitch = (Switch)findViewById(R.id.TimeSwitch);
        locationSwitch = (Switch)findViewById(R.id.proximitySwitch);

        //location stuff
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    private class objectListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(v == addButton) {
                //TODO(Kylie): add if for non-populated location
                //saveProximityAlertPoint();
                String reminderInfo = reminderText.getText().toString();
                String[] reminderSubtasks = null;
                Calendar cal = null;
                if(timeSwitch.isChecked()){
                    int day = date.getDayOfMonth();
                    int month = date.getMonth();
                    int year =  date.getYear();

                    int hour = time.getCurrentHour();
                    int minute = time.getCurrentMinute();


                    cal = Calendar.getInstance();
                    cal.set(year, Calendar.JANUARY, day, hour, minute);
                    cal.set(Calendar.MONTH, month);

            }
                int urg = urgency.getProgress();
                if(reminderInfo.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter text for your reminder.", Toast.LENGTH_SHORT).show();
                }else {
                    //9999 is the "null" equivalent for longitude and latitude
                    RESTClient.newReminder(getApplicationContext(), username, reminderInfo, reminderSubtasks, 9999, 9999, urg, cal);
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
            }

        }
    }

    public class onToggleClicked implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            if(compoundButton == timeSwitch){
                if(timeSwitch.isChecked()){
                    time.setVisibility(View.VISIBLE);
                    date.setVisibility(View.VISIBLE);
                }else{
                    time.setVisibility(View.GONE);
                    date.setVisibility(View.GONE);
                }
            }
        }
    }

    private void addActionListeners() {
        addButton.setOnClickListener(addListener);
        timeSwitch.setOnCheckedChangeListener(switchListener);
        timeSwitch.setOnCheckedChangeListener(switchListener);
    }

    /*
    This next section deals with implementing the proximity alerts.
     */
    private void saveProximityAlertPoint() {
        //saveCoordinatesInPreferences(LAT,LNG);
        //addProximityAlert(LAT,LNG);
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

    /*private Calendar.Month getMonth(int month){
        switch(month){
            case 0:
                return Calendar.JANUARY;
            default:
                return 0;
        }

    }*/

}
