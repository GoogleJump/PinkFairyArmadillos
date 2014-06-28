package com.reminder.app;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.app.Activity;

public class DrawerItemCustomAdapter extends ArrayAdapter<NavItem> {

    Context mContext;
    int layoutResourceId;
    NavItem data[] = null;
    int submenu1 = 1;
    int submenu2 = 2;
    Typeface font;

    public DrawerItemCustomAdapter(Context mContext, int layoutResourceId, NavItem[] data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
        font = Typeface.createFromAsset(mContext.getAssets(), "Roboto-Thin.ttf");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        if(position == 1 || position == 2) {
            row = inflater.inflate(R.layout.row_nav_item_sub_menu, parent, false);
            TextView drawerItem = (TextView) row.findViewById(R.id.drawerItem);
            drawerItem.setTypeface(font);
            NavItem folder = data[position];
            drawerItem.setText(folder.name);
        }
        else
        {
            row = inflater.inflate(layoutResourceId, parent, false);
            TextView drawerItem = (TextView) row.findViewById(R.id.drawerItem);
            drawerItem.setTypeface(font);
            NavItem folder = data[position];
            drawerItem.setText(folder.name);
        }
        return row;
    }

}