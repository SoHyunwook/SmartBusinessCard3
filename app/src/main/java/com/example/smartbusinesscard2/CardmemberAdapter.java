package com.example.smartbusinesscard2;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
public class CardmemberAdapter extends RecyclerView.Adapter<CardmemberAdapter.ViewHolder> implements ItemTouchHelperListener{
    Context context;
    int layout;
    int randnum = 0;
    String[] randColor = {"#FAED7D", "#BCE55C", "#6799FF", "#FFB2D9", "#FFBB00", "#FFA7A7", "#EAEAEA", "#6B9900"};
    ArrayList<Cardmember> list;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    DBManager dbManager;
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
        holder.cardview.setBackgroundColor(Color.parseColor(randColor[randnum % 8]));
        randnum++;
        holder.cardview.setRadius(20);
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
        return convertView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView2;
        TextView textView3;
        CardView cardview;
        public ViewHolder(View itemView) {
            super(itemView);
            textView2 = (TextView) itemView.findViewById(R.id.textView2);
            textView3 = (TextView) itemView.findViewById(R.id.textView3);
            cardview = (CardView)itemView.findViewById(R.id.cardview);
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

        return true;
    }

    @Override
    public void onItemRemove(int position) {
        list.remove(position);
        System.out.println("position: " + position);
        if(dbManager == null) {
            dbManager = new DBManager(context, "myDB.db", null, 1);
        }
        sqLiteDatabase = dbManager.getWritableDatabase();
        cursor = sqLiteDatabase.rawQuery("SELECT * FROM CARDMEMBER", null);
        Cursor c = cursor;
        c.moveToPosition(position);
        System.out.println("c op_name:" + c.getString(c.getColumnIndexOrThrow("op_name")));
        dbManager.deleteCallDetail(c.getString(c.getColumnIndexOrThrow("op_name")), c.getString(c.getColumnIndexOrThrow("ophone")));
        cursor.close();
        sqLiteDatabase.close();
        notifyItemRemoved(position);
    }
}
