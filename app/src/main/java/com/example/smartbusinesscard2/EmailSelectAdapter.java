package com.example.smartbusinesscard2;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yi_te on 2016-11-08.
 */

public class EmailSelectAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<Cardmember> list;

    public EmailSelectAdapter(Context context, int layout, ArrayList<Cardmember> list) {
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
        if(convertView == null) {
            convertView = View.inflate(context, layout, null);
        }
        Cardmember card = list.get(position);
        TextView pnametv = (TextView)convertView.findViewById(R.id.s_name);
        pnametv.setText(card.p_name);
//        TextView cnametv = (TextView)convertView.findViewById(R.id.s_email);
//        cnametv.setText(card.email);
        return convertView;
    }
}