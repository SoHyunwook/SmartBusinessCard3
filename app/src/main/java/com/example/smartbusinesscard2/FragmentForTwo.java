package com.example.smartbusinesscard2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

/**
 * Created by 현욱 on 2016-11-30.
 */
public class FragmentForTwo extends AppCompatActivity {
    DBManager dbManager;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    Cardmember cardmember;
    CardmemberAdapter.ItemClick itemClick;
    RecyclerView list;
    LinearLayoutManager linearLayoutManager;
    ArrayList<Cardmember> data1 = new ArrayList<Cardmember>();
    CardmemberAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_for_two);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        list = (RecyclerView) findViewById(R.id.listView22);
        list.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(linearLayoutManager);
        list.scrollToPosition(0);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new CardmemberAdapter(this, R.layout.card, data1);

        Intent intent = getIntent();
        String groupName = intent.getStringExtra("group").toString();
        System.out.println("groupName: " + groupName);
        dbOpen();
        cursor = sqLiteDatabase.query("CARDMEMBER", null, null, null, null, null, null);
        data1 = new ArrayList<Cardmember>();
        switch (groupName) {
            case "cj":
                while(cursor.moveToNext()) {
                    if(cursor.getString(2).toString().equals("CJ") || cursor.getString(2).toString().equals("Cj") || cursor.getString(2).toString().equals("cj")) {
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
                }
                break;
            case "kb":
                while(cursor.moveToNext()) {
                    if(cursor.getString(2).toString().equals("KB") || cursor.getString(2).toString().equals("Kb") || cursor.getString(2).toString().equals("kb")) {
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
                }
                break;
            case "lg":
                while(cursor.moveToNext()) {
                    if(cursor.getString(2).toString().equals("LG") || cursor.getString(2).toString().equals("Lg") || cursor.getString(2).toString().equals("lg")) {
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
                }
                break;
            case "lotte":
                while(cursor.moveToNext()) {
                    if(cursor.getString(2).toString().equals("LOTTE") || cursor.getString(2).toString().equals("Lotte") || cursor.getString(2).toString().equals("lotte")) {
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
                }
                break;
            case "samsung":
                while(cursor.moveToNext()) {
                    if(cursor.getString(2).toString().equals("SAMSUNG") || cursor.getString(2).toString().equals("Samsung") || cursor.getString(2).toString().equals("samsung")) {
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
                }
                break;
            case "shinsegae":
                while(cursor.moveToNext()) {
                    if(cursor.getString(2).toString().equals("SHINSEGAE") || cursor.getString(2).toString().equals("Shinsegae") || cursor.getString(2).toString().equals("shinsegae")) {
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
                }
                break;
        }
        cursor.close();
        dbClose();
        adapter = new CardmemberAdapter(this, R.layout.card, data1);
        list.setAdapter(adapter);
        list.setItemAnimator(new DefaultItemAnimator());
    }

    void dbOpen() {
        if(dbManager == null) {
            dbManager = new DBManager(this, "myDB.db", null, 1);
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
