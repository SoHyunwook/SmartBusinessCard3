package com.example.smartbusinesscard2;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 현욱 on 2016-11-08.
 */
public class CardmemberAdapter extends RecyclerView.Adapter<CardmemberAdapter.ViewHolder> {
    Context context;
    int layout;
    ArrayList<Cardmember> list;
    private ItemClick itemClick;
    public interface ItemClick {
        public void onClick(View view,int position);
    }

    //아이템 클릭시 실행 함수 등록 함수
    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public CardmemberAdapter(Context context, int layout, ArrayList<Cardmember> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int Position = position;
        Cardmember item = list.get(position);

        holder.textView2.setText(item.p_name.toString());
        holder.textView3.setText(item.c_name.toString());
        holder.textView.setText(item._id+"");
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.onClick(v, Position);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView2;
        TextView textView3;
        TextView textView;
        CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            textView2 = (TextView) itemView.findViewById(R.id.textView2);
            textView3 = (TextView) itemView.findViewById(R.id.textView3);
            textView = (TextView) itemView.findViewById(R.id.textView);
            cardview = (CardView)itemView.findViewById(R.id.cardview);

        }
    }
}
