package com.example.smartbusinesscard2;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yi_te on 2016-11-16.
 */
public class EmailInfoAdapter extends  BaseAdapter {
    Context context;
    int layout;
    ArrayList<EmailInformation> list;

    public EmailInfoAdapter(Context context, int layout, ArrayList<EmailInformation> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, layout, null);
        }
        EmailInformation Email = list.get(position);
        TextView sub = (TextView) convertView.findViewById(R.id.sub2);
        sub.setText(Email.subject);
        TextView time = (TextView) convertView.findViewById(R.id.time2);
        time.setText(Email.show_time);
        return convertView;
    }
}