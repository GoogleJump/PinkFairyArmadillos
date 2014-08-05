package com.reminder.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People.LoadPeopleResult;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends Activity implements ConnectionCallbacks, OnConnectionFailedListener, View.OnClickListener, ResultCallback<LoadPeopleResult> {
    private Context context;
    private static final int RC_SIGN_IN = 0;
    private static final int PROFILE_PIC_SIZE = 150;
    private static final String FONT = "Roboto-Thin.ttf";
    private static final String TAG = "testing";
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    private Person currentPerson;
    private String personName;
    private String personGooglePlusProfile;
    private String email;
    private String personPhotoURL;
    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView lastClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this.getApplicationContext();
        lastClicked = null;
        createNavigationDrawer();
        getLoggedInUser();
        ArrayList<Reminder> reminders = new ArrayList<Reminder>();
        //selectItem(1);
        //getTime();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sign_out_settings) {
            signOutUser();
        }
        else if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createNavigationDrawer() {
        mTitle  = getTitle();
        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        NavItem[] drawerItem = new NavItem[4];
        drawerItem[0] = new NavItem("Reminders");
        drawerItem[1] = new NavItem("Upcoming");
        drawerItem[2] = new NavItem("Done");
        drawerItem[3] = new NavItem("Sign Out");
        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.row_nav_item, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_navigation_drawer,
                R.string.drawer_open,
                R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        View header = (View)getLayoutInflater().inflate(R.layout.nav_header, null);
        mDrawerList.addHeaderView(header);
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.action_bar_layout, null);
        actionBar.setCustomView(mCustomView);
        actionBar.setDisplayShowCustomEnabled(true);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    public void getLoggedInUser() {
        mGoogleApiClient  = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
    }

    public boolean signOutUser()
    {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
            Intent logoutScreen = new Intent(this.context, LoginActivity.class);
            startActivity(logoutScreen);
            return true;
        }
        return false;
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
            }
            catch (IntentSender.SendIntentException e) {
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
    @Override
    public void onConnected(Bundle connectionHint) {
        mSignInClicked = false;
        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            currentPerson = getPerson();
            personName = getPersonGooglePlusName();
            email = getPersonEmail();
            String ID = currentPerson.getId();
            personPhotoURL = getPhotoURL();
            setProfilePicture(personPhotoURL);
            Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(this);
        }
    }

    public Person getPerson() {
        return Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
    }

    public String getPersonGooglePlusName() {
        return currentPerson.getDisplayName().toString();
    }

    public String getPersonEmail() {
       return Plus.AccountApi.getAccountName(mGoogleApiClient);
    }

    public void setProfilePicture(String URL) {
        CircularImageView myImage = (CircularImageView) this.findViewById(R.id.profilePicture);
        myImage.setImageUrl(URL);
    }

    public String getPhotoURL() {
        String URL = currentPerson.getImage().getUrl();
        String URLWithSize = URL.substring(0, URL.length() - 2) + PROFILE_PIC_SIZE;
        return URLWithSize;
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            handleClicksForBoldFont(position,view);
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        bundle.putString("username", email);
        bundle.putInt("type", position);
        //1 -> list all reminders 2->list upcoming reminders 3-> list done reminders
        switch (position) {
            case 1:
                fragment = new ReminderList();
                fragment.setArguments(bundle);
                break;
            case 2:
                fragment = new ReminderList();
                fragment.setArguments(bundle);
                break;
            case 3:
                fragment = new ReminderList();
                fragment.setArguments(bundle);
                break;
            case 4:
                signOutUser();
                break;
            default:
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    public void onClick (View v) {
        Fragment fragment = null;
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }
    }

    public void handleClicksForBoldFont(int position, View view) {
        if(position!=0) {
            View row = view;
            TextView rowName = (TextView) row.findViewById(R.id.drawerItem);
            rowName.setTypeface(null, Typeface.BOLD);
            if(lastClicked!=null)
            {
                Typeface font = Typeface.createFromAsset(context.getAssets(), FONT);
                lastClicked.setTypeface(font,Typeface.NORMAL);
            }
            lastClicked = rowName;
        }
    }

    public void getTime() {
        RESTClient.getDrivingTime(context, "Healdsburg, CA", "San Francisco, CA" ,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray returnedItems = (JSONArray) response.get("rows");
                    JSONObject item = (JSONObject)returnedItems.get(0);
                    JSONArray distance = (JSONArray)item.get("elements");
                    JSONObject obj = (JSONObject)distance.get(0);
                    JSONObject duration = (JSONObject)obj.get("duration");
                    String drivingTime = duration.getString("text");
                } catch (JSONException e) {
                    Log.i("JSON Exception: ", e.toString());
                }
            }
            @Override
            public void onFailure(Throwable e, JSONObject errorResponse) {
                String msg = "Object *" + e.toString() + "*" + errorResponse.toString();
                Log.i("testing", "onFailure: " + msg);
            }
        });
    }
    @Override
    public void onResult(LoadPeopleResult peopleData) {
        if (peopleData.getStatus().getStatusCode() == ConnectionResult.SUCCESS) {
            PersonBuffer personBuffer = peopleData.getPersonBuffer();
            try {
                int count = personBuffer.getCount();
                for (int i = 0; i < count; i++) {
                    Log.d(TAG, "Display name: " + personBuffer.get(i).getDisplayName() + " " + personBuffer.get(i).getImage().getUrl());
                }
            } finally {
                personBuffer.close();
            }
        } else {
            Log.e(TAG, "Error requesting people data: " + peopleData.getStatus());
        }
    }

}