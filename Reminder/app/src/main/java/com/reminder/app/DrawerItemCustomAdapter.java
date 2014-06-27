package com.reminder.app;

import android.content.Context;
import android.graphics.Typeface;
import android.view.*;
import android.widget.*;
import android.app.Activity;

public class DrawerItemCustomAdapter extends ArrayAdapter<NavItem> {

    Context mContext;
    int layoutResourceId;
    NavItem data[] = null;

    public DrawerItemCustomAdapter(Context mContext, int layoutResourceId, NavItem[] data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);
        TextView drawerItem = (TextView)listItem.findViewById(R.id.drawerItem);
        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "Roboto-Thin.ttf");
        drawerItem.setTypeface(font);
        NavItem folder = data[position];
        drawerItem.setText(folder.name);
        return listItem;
    }

}