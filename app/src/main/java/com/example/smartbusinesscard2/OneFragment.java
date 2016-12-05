package com.example.smartbusinesscard2;

/**
 * Created by 현욱 on 2016-11-29.
 */
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class OneFragment extends Fragment{

    DBManager dbManager;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    CardmemberAdapter.ItemClick itemClick;
    RecyclerView list;
    LinearLayoutManager linearLayoutManager;
    ArrayList<Cardmember> data1 = new ArrayList<Cardmember>();
    CardmemberAdapter adapter;

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbOpen();
        cursor = sqLiteDatabase.query("CARDMEMBER", null, null, null, null, null, null);
        data1 = new ArrayList<Cardmember>();
        Cardmember cardmember;
        while(cursor.moveToNext()) {
            cardmember = new Cardmember(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
            cardmember._id = cursor.getInt(0);
            cardmember.p_name = cursor.getString(1);
            cardmember.c_name = cursor.getString(2);
            cardmember.phone = cursor.getString(3);
            cardmember.email = cursor.getString(4);
            cardmember.fax = cursor.getString(5);
            cardmember.position = cursor.getString(6);
            cardmember.op_name = cursor.getString(7);
            cardmember.ophone = cursor.getString(8);
            data1.add(cardmember);
        }
        cursor.close();
        dbClose();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.one_fragment, container, false);
        list = (RecyclerView) view.findViewById(R.id.listView);
        list.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(linearLayoutManager);
        list.scrollToPosition(0);
        adapter = new CardmemberAdapter(getActivity(), R.layout.card, data1);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter));
        itemTouchHelper.attachToRecyclerView(list);

        adapter.setItemClick(new CardmemberAdapter.ItemClick() {
            @Override
            public void onClick(View view, int position) {
                dbOpen();
                cursor = sqLiteDatabase.rawQuery("SELECT * FROM CARDMEMBER", null);
                Cursor c = cursor;
                c.moveToPosition(position);
                Intent i = new Intent(getActivity(), PrintInformation2.class);
                System.out.println("sending _id:" + position);
                i.putExtra("_id", position);
                i.putExtra("pname", c.getString(c.getColumnIndexOrThrow("p_name")));
                i.putExtra("comname", c.getString(c.getColumnIndexOrThrow("c_name")));
                i.putExtra("tel1", c.getString(c.getColumnIndexOrThrow("phone")));
                i.putExtra("email1", c.getString(c.getColumnIndexOrThrow("email")));
                i.putExtra("fax1", c.getString(c.getColumnIndexOrThrow("fax")));
                i.putExtra("position", c.getString(c.getColumnIndexOrThrow("position")));
                i.putExtra("op_name", c.getString(c.getColumnIndexOrThrow("op_name")));
                i.putExtra("ophone", c.getString(c.getColumnIndexOrThrow("ophone")));
                startActivity(i);

                cursor.close();
                dbClose();
                dbManager.close();
            }
        });

        list.setAdapter(adapter);
        list.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    void dbOpen() {
        if(dbManager == null) {
            dbManager = new DBManager(getActivity(), "myDB.db", null, 1);
        }
        sqLiteDatabase = dbManager.getWritableDatabase();
    }
    void dbClose() {
        if(sqLiteDatabase != null) {
            if(sqLiteDatabase.isOpen()) {
                sqLiteDatabase.close();
            }
        }
    }
}