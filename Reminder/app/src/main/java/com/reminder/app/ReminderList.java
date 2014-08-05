package com.reminder.app;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.faizmalkani.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ReminderList extends Fragment implements GestureDetector.OnGestureListener, View.OnClickListener {
    private GestureDetector detector = new GestureDetector(this);
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private static final String FONT = "Roboto-Thin.ttf";
    private ExpandableListView reminderList;
    protected ReminderListAdapter reminderAdapter;
    protected ArrayList<Reminder> reminderArray;
    protected String username;
    private int typeOfList;
    private PendingIntent pendingIntent;
    private Context context;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.default_page, container, false);
        username = getArguments().getString("username");
        typeOfList = getArguments().getInt("type");
        activity = getActivity();
        context = activity.getApplicationContext();
        initLayout(rootView);
        return rootView;
    }

    private void initLayout(View v) {
        reminderList = (ExpandableListView) v.findViewById(R.id.reminderList);
        FloatingActionButton mFab = (FloatingActionButton)v.findViewById(R.id.fabbutton);
        Resources res = getResources();
        Drawable imageAdd = res.getDrawable(R.drawable.plus);
        int color = res.getColor(R.color.turquoise);
        mFab.setDrawable(imageAdd);
        mFab.setColor(color);
        mFab.setOnClickListener(this);
        getReminders();
    }

    public void getReminders() {
        if (typeOfList == 1) {
            RESTClient.listReminders(getActivity().getApplicationContext(), username, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONArray returnedItems = (JSONArray) response.get("items");
                        reminderArray = formatAndCreateRetrievedReminders(returnedItems);
                        reminderAdapter = new ReminderListAdapter(activity, reminderArray);
                        reminderList.setAdapter(reminderAdapter);
                        reminderList.setOnTouchListener(new OnTouchListener() {

                            public boolean onTouch(View view, MotionEvent e) {
                                detector.onTouchEvent(e);
                                return false;
                            }
                        });
                        createTimedNotification(reminderArray);
                    } catch (Exception e) {
                        Log.i("Exception: ", e.toString());
                    }
                }

                @Override
                public void onFailure(Throwable e, JSONObject errorResponse) {
                    String msg = "Object *" + e.toString() + "*" + errorResponse.toString();
                    Log.i("testing", "onFailure: " + msg);
                }
            });
        } else if (typeOfList == 2) {
            RESTClient.listUpcomingReminders(getActivity().getApplicationContext(), username, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {

                        JSONArray returnedItems = (JSONArray) response.get("items");
                        reminderArray = formatAndCreateRetrievedReminders(returnedItems);
                        reminderAdapter = new ReminderListAdapter(activity, reminderArray);
                        reminderList.setAdapter(reminderAdapter);
                        reminderList.setOnTouchListener(new OnTouchListener() {

                            public boolean onTouch(View view, MotionEvent e) {
                                detector.onTouchEvent(e);
                                return false;
                            }
                        });
                    } catch (Exception e) {
                        Log.i("Exception: ", e.toString());
                    }
                }

                @Override
                public void onFailure(Throwable e, JSONObject errorResponse) {
                    String msg = "Object *" + e.toString() + "*" + errorResponse.toString();
                    Log.i("testing", "onFailure: " + msg);
                }
            });
        } else if (typeOfList == 3) {
            RESTClient.listDoneReminders(getActivity().getApplicationContext(), username, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONArray returnedItems = (JSONArray) response.get("items");
                        reminderArray = formatAndCreateRetrievedReminders(returnedItems);
                        reminderAdapter = new ReminderListAdapter(activity, reminderArray);
                        reminderList.setAdapter(reminderAdapter);
                        reminderList.setOnTouchListener(new OnTouchListener() {

                            public boolean onTouch(View view, MotionEvent e) {
                                detector.onTouchEvent(e);
                                return false;
                            }
                        });
                    } catch (Exception e) {
                        Log.i("Exception: ", e.toString());
                    }
                }

                @Override
                public void onFailure(Throwable e, JSONObject errorResponse) {
                    String msg = "Object *" + e.toString() + "*" + errorResponse.toString();
                    Log.i("testing", "onFailure: " + msg);
                }
            });
        }
    }

    public ArrayList<Reminder> formatAndCreateRetrievedReminders(JSONArray items) {
        ArrayList<Reminder> reminders = new ArrayList<Reminder>();
        for (int i = 0; i < items.length(); i++) {
            try {
                JSONObject toBeReminder = (JSONObject) items.get(i);
                String unformattedDate = toBeReminder.get("datetime").toString();
                Calendar date = getReminderDateTimeFormatted(unformattedDate);

                if (!toBeReminder.isNull("reminder")) {
                    JSONArray remindersJSONArray = toBeReminder.getJSONArray("reminder");
                    String[] remindersArray = new String[remindersJSONArray.length()];
                    for (int j = 0; j < remindersJSONArray.length(); j++) {
                        remindersArray[j] = remindersJSONArray.get(j).toString();
                    }
                    Reminder reminder = new Reminder(toBeReminder.get("id").toString(), toBeReminder.get("title").toString(), toBeReminder.getInt("urgency"), date, 9.1, 10.0, remindersArray,i);
                    reminders.add(reminder);
                } else {
                    Reminder reminder = new Reminder(toBeReminder.get("id").toString(), toBeReminder.get("title").toString(), toBeReminder.getInt("urgency"), date, 9.1, 10.0,i);
                    reminders.add(reminder);
                }
            } catch (Exception e) {
                Log.i("Exception:", e.toString());
            }
        }
        return reminders;
    }

    public Calendar getReminderDateTimeFormatted(String dateToFormat) {
        try {
            Calendar calendar = Calendar.getInstance();
            String unformattedDate = dateToFormat.replace("T", " ");
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH).parse(unformattedDate));
            return calendar;
        } catch (Exception e) {

        }
        return null;
    }

    private void createTimedNotification(ArrayList<Reminder> reminders) {
        SharedPreferences prefs = activity.getSharedPreferences("Notifications", Context.MODE_PRIVATE);
        Calendar calendar = Calendar.getInstance();
        for (Reminder reminder : reminders) {
            Calendar dueDateTime = reminder.getDueDate();
            if(dueDateTime.compareTo(Calendar.getInstance()) >= 0) {
                //dueDateTime is in the future
                Log.i("testing", reminder.getTitle());
                calendar.set(Calendar.MONTH, dueDateTime.get(Calendar.MONTH));
                calendar.set(Calendar.YEAR, dueDateTime.get(Calendar.YEAR));
                calendar.set(Calendar.DAY_OF_MONTH, dueDateTime.get(Calendar.DAY_OF_MONTH));
                calendar.set(Calendar.HOUR_OF_DAY, dueDateTime.get(Calendar.HOUR_OF_DAY));
                calendar.set(Calendar.MINUTE, dueDateTime.get(Calendar.MINUTE));
                Intent myIntent = new Intent(activity, Receiver.class);
                myIntent.putExtra("Reminder Title", reminder.getTitle());
                myIntent.putExtra("ID", reminder.getNotificationID());
                pendingIntent = PendingIntent.getBroadcast(activity, reminder.getNotificationID(), myIntent, 0);
                AlarmManager alarmManager = (AlarmManager) activity.getSystemService(context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
            }
        }
    }

    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {

                return false;
            }
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                final int positionInList = reminderList.pointToPosition((int) e1.getX(), (int) e1.getY());
                Animation slideReminderLayout = AnimationUtils.loadAnimation(context, R.anim.fadeout);
                slideReminderLayout.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        Animation slideDeleteLayout = AnimationUtils.loadAnimation(context, R.anim.fadein);
                        slideDeleteLayout.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                String reminderToDelete = reminderAdapter.remove(positionInList);
                                RESTClient.deleteReminder(context,reminderToDelete);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        Typeface font = Typeface.createFromAsset(context.getAssets(), FONT);
                        TextView deleteRow = (TextView)reminderList.getChildAt(positionInList).findViewById(R.id.delete);
                        deleteRow.setTypeface(font);
                        deleteRow.startAnimation(slideDeleteLayout);
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                reminderList.getChildAt(positionInList).findViewById(R.id.reminderContainer).startAnimation(slideReminderLayout);
            }
        } catch (Exception e) {
        }

        return true;
    }
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        // TODO Auto-generated method stub
        return true;
    }
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void onClick(View view) {
        Intent add = new Intent(context, AddReminder.class);
        startActivity(add);
    }
}
