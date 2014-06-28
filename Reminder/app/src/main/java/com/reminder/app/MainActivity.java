package com.reminder.app;

 import android.content.IntentSender;
 import android.graphics.Typeface;
 import android.os.Bundle;
 import android.content.Context;
 import android.content.Intent;
 import android.view.*;
 import com.google.android.gms.common.ConnectionResult;
 import com.google.android.gms.common.api.GoogleApiClient.*;
 import com.google.android.gms.common.api.GoogleApiClient;
 import com.google.android.gms.plus.*;
 import com.google.android.gms.plus.model.people.Person;
 import android.support.v4.widget.*;
 import android.support.v4.app.ActionBarDrawerToggle;
 import android.widget.*;
 import android.app.*;
 import android.app.ActionBar;

 import java.security.PolicySpi;

public class MainActivity extends Activity implements ConnectionCallbacks, OnConnectionFailedListener {
    private Context context;
    private static final int RC_SIGN_IN = 0;
    private static final int PROFILE_PIC_SIZE = 100;
    private static final String FONT = "Roboto-Thin.ttf";
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
        selectItem(1);
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
            personPhotoURL = getPhotoURL();
            setProfilePicture(personPhotoURL);
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
        switch (position) {
            case 1:
                fragment = new ReminderList();
                break;
            case 2:
                fragment = new ReminderList();
                break;
            case 3:
                fragment = new ReminderList();
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
}