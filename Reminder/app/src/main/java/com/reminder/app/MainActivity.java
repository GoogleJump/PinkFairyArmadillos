package com.reminder.app;

 import android.app.Activity;
 import android.content.IntentSender;
 import android.os.Bundle;
 import android.util.Log;
 import android.view.Menu;
 import android.view.MenuItem;
 import android.content.Context;
 import android.accounts.*;
 import com.google.android.gms.common.api.GoogleApiClient.*;
 import com.google.android.gms.common.api.GoogleApiClient;
 import com.google.android.gms.plus.*;
 import com.google.android.gms.common.ConnectionResult;
 import android.content.Intent;
 import android.content.IntentSender.SendIntentException;
 import android.view.*;
 import android.widget.EditText;
 import android.widget.NumberPicker;
 import android.widget.Toast;
 import com.google.android.gms.plus.model.people.Person;



public class MainActivity extends Activity implements
        ConnectionCallbacks, OnConnectionFailedListener, View.OnClickListener{
    private Context context;
    private EditText title;
    private EditText reminder;
    private NumberPicker lat;
    private NumberPicker lng;


    private Person currentPerson;
    private String personName;
    private String personGooglePlusProfile;
    private String email;

    private static final int RC_SIGN_IN = 0;
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;

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


        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.button).setOnClickListener(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();

        //RESTClient.listReminders(context, email);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
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

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }
    /* A helper method to resolve the current ConnectionResult error. */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    public void onConnectionFailed(ConnectionResult result) {
        if (!mIntentInProgress) {
            // Store the ConnectionResult so that we can use it later when the user clicks
            // 'sign-in'.
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }
    public void onClick(View view) {
        if (view.getId() == R.id.sign_in_button
                && !mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
        else if (view.getId() == R.id.button){
            String[] list = new String[1];
            list[0] = reminder.getText().toString();
            String reminderTitle = title.getText().toString();
            double latitude = lat.getValue();
            double longitude = lng.getValue();
            RESTClient rest = new RESTClient();
            rest.newReminder(context,email,reminderTitle,list,latitude,longitude);
            Thread.currentThread().setContextClassLoader(rest.getClass().getClassLoader());
            Toast.makeText(this,"New Reminder Created! Check API explorer for confirmation.",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onConnected(Bundle connectionHint) {
        mSignInClicked = false;
        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            personName = currentPerson.getDisplayName();
            //String personPhoto = currentPerson.getImage(); getting image
            personGooglePlusProfile = currentPerson.getUrl();
            email = Plus.AccountApi.getAccountName(mGoogleApiClient);
            Toast.makeText(this,"Welcome "+email,Toast.LENGTH_SHORT).show();
        }
    }
}
