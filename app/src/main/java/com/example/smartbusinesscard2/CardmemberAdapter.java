package com.example.smartbusinesscard2;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 현욱 on 2016-11-08.
 */
public class CardmemberAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<Cardmember> list;

    public CardmemberAdapter(Context context, int layout, ArrayList<Cardmember> list) {
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
        TextView pnametv = (TextView)convertView.findViewById(R.id.textView2);
        pnametv.setText(card.p_name);
        TextView cnametv = (TextView)convertView.findViewById(R.id.textView3);
        cnametv.setText(card.c_name);
        TextView idtv = (TextView)convertView.findViewById(R.id.textView);
        idtv.setText(String.valueOf(card._id));
        return convertView;
    }
}
