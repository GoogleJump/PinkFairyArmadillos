package com.reminder.app;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient.*;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.*;
import com.google.android.gms.common.ConnectionResult;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.view.*;
import android.view.animation.*;
import android.widget.ProgressBar;

public class LoginActivity extends Activity implements ConnectionCallbacks, OnConnectionFailedListener, View.OnClickListener {
    private static final int RC_SIGN_IN = 0;
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setUpLogin();
    }

    public void setUpLogin() {
        SignInButton signInButton = (SignInButton)findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);
        addAnimation(signInButton);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
    }

    public void addAnimation(SignInButton button) {
        Animation slide = AnimationUtils.loadAnimation(this, R.anim.abc_slide_in_bottom);
        button.startAnimation(slide);
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
            ProgressBar signingIn = (ProgressBar)findViewById(R.id.progressBar);
            signingIn.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onConnected(Bundle connectionHint) {
        mSignInClicked = false;
        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            Intent mainActivityPage = new Intent(this,MainActivity.class);
            startActivity(mainActivityPage);
        }
    }
}
