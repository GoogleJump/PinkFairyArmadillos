package com.reminder.app;

 import android.app.Activity;
 import android.os.Bundle;
 import android.view.Menu;
 import android.view.MenuItem;
 import android.content.Context;
 import android.content.Intent;
 import android.view.*;
 import android.widget.EditText;
 import android.widget.NumberPicker;
 import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {
    private Context context;
    private EditText title;
    private EditText reminder;
    private NumberPicker lat;
    private NumberPicker lng;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this.getApplicationContext();
        title = (EditText)findViewById(R.id.ReminderTitle);
        reminder = (EditText)findViewById(R.id.Reminder);
        lat = (NumberPicker)findViewById(R.id.lat);
        lng = (NumberPicker)findViewById(R.id.lang);
        lat.setMinValue(1);
        lat.setMaxValue(100);
        lng.setMaxValue(1);
        lng.setMaxValue(100);
        findViewById(R.id.button).setOnClickListener(this);
        Intent now = getIntent();
        email = now.getStringExtra("email");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onClick(View view) {
        if (view.getId() == R.id.button){
            String[] list = new String[1];
            list[0] = reminder.getText().toString();
            String reminderTitle = title.getText().toString();
            double latitude = lat.getValue();
            double longitude = lng.getValue();
            int urgencyHardCoded = 18;
            RESTClient rest = new RESTClient();
            rest.newReminder(context,email,reminderTitle,list,latitude,longitude,urgencyHardCoded);
            Thread.currentThread().setContextClassLoader(rest.getClass().getClassLoader());
            Toast.makeText(this,"New Reminder Created! Check API explorer for confirmation. Chrome may not work.",Toast.LENGTH_LONG).show();
        }
    }
}
