package com.reminder.app;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ReminderListAdapter extends BaseExpandableListAdapter {
    private Activity activity;
    private ArrayList<Reminder> reminderList;
    private int count;
    private Context context;
    private PendingIntent pendingIntent;

    public ReminderListAdapter(Activity activity, ArrayList<Reminder> reminderList) {
        this.activity = activity;
        this.reminderList = reminderList;
        this.count = 0;
        this.context = activity.getBaseContext();
    }

    public String remove(int position) {
        Reminder reminderToDelete = reminderList.get(position);
        String reminderIDToDelete = reminderToDelete.getId();
        reminderList.remove(position);
        notifyDataSetChanged();
        return reminderIDToDelete;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return reminderList.get(groupPosition).getReminders();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final String[] childText = (String[]) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.reminder_child, null);
        }
        TextView reminder = (TextView)convertView.findViewById(R.id.childItem);
        reminder.setText(childText[childPosition]);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(reminderList.get(groupPosition).getReminders() != null)
            return reminderList.get(groupPosition).getReminders().length;
        else
            return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return reminderList.get(groupPosition).getTitle();
    }

    @Override
    public int getGroupCount() {
        return reminderList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.reminder, null);
        }
        TextView reminderTitle = (TextView)convertView.findViewById(R.id.reminderTitle);
        reminderTitle.setText(headerTitle);
        ImageView settings = (ImageView)convertView.findViewById(R.id.settings);
        ImageView expandableIcon = (ImageView)convertView.findViewById(R.id.expandIcon);

        if(getChildrenCount(groupPosition) == 0 && count <= 11) {
            expandableIcon.setVisibility(View.INVISIBLE);
        }
        count++;

        if(isExpanded) {
            settings.setVisibility(View.VISIBLE);
            Drawable downIcon = context.getResources().getDrawable(R.drawable.down);
            expandableIcon.setImageDrawable(downIcon);
        }
        else {
            settings.setVisibility(View.INVISIBLE);
            Drawable forwardIcon = context.getResources().getDrawable(R.drawable.forward);
            expandableIcon.setImageDrawable(forwardIcon);
        }

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("testing", "edit");
            }
        });
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}