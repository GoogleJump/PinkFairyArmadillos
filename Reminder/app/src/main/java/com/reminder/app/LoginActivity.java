package com.reminder.app;

import android.app.Activity;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.content.Context;
import com.google.android.gms.common.api.GoogleApiClient.*;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.plus.*;
import com.google.android.gms.common.ConnectionResult;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.view.*;
import android.widget.Toast;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.maps.CameraUpdateFactory;

public class LoginActivity extends Activity implements ConnectionCallbacks, OnConnectionFailedListener, View.OnClickListener {
    private Context context;
    private Person currentPerson;
    private String personName;
    private String personGooglePlusProfile;
    private String email;
    private static final int RC_SIGN_IN = 0;
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        context = this.getApplicationContext();
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
        LatLng test = new LatLng(-33.867, 151.206);
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(test, 13));
        map.addMarker(new MarkerOptions()
                .title("test")
                .snippet("test")
                .position(test));
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
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
            Intent createReminderPage = new Intent(context,MainActivity.class);
            createReminderPage.putExtra("email", email);
            startActivity(createReminderPage);
        }
    }
}
