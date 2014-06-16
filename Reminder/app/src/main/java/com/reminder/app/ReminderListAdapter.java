package com.reminder.app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Kylie Williamson on 6/15/14.
 */
public class ReminderListAdapter extends BaseAdapter{
    private Context context;
    private List<Reminder> reminderList;

    public ReminderListAdapter(Context context, List<Reminder> reminderList) {
        this.context = context;
        this.reminderList = reminderList;
    }

    @Override
    public int getCount() {
        return reminderList.size();
    }

    @Override
    public Object getItem(int i) {
        return reminderList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return reminderList.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view != null){
            ReminderView update = (ReminderView)view;
            update.setReminder(reminderList.get(i));
            return update;
        }else {
            return new ReminderView(context, reminderList.get(i));
        }
    }
}