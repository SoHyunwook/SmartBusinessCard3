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
import android.view.View;

import java.util.ArrayList;

/**
 * Created by 현욱 on 2016-12-03.
 */
public class FragmentForThree extends AppCompatActivity {
    DBManager dbManager;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor, mycursor;
    Cardmember cardmember;
    CardmemberAdapter.ItemClick itemClick;
    RecyclerView list;
    LinearLayoutManager linearLayoutManager;
    ArrayList<Cardmember> data1 = new ArrayList<Cardmember>();
    CardmemberAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_for_three);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        String[] level1 = {"Professor", "Associate Professor", "Chairman", "President", "CEO"};
        String[] level2 = {"Vice Chairman", "Vice President"};
        String[] level3 = {"Representative Director", "Managing Director", "Senior Managing Director", "Director"};
        String[] level4 = {"Manager", "General Manager"};
        String[] level5 = {"Assistant Manager", "Deputy General Manager"};
        String[] level6 = {"Staff"};

        list = (RecyclerView) findViewById(R.id.listView23);
        list.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(linearLayoutManager);
        list.scrollToPosition(0);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new CardmemberAdapter(this, R.layout.card, data1);

        Intent intent = getIntent();
        String posName = intent.getStringExtra("pos").toString();
        System.out.println("posName: " + posName);
        dbOpen();
        cursor = sqLiteDatabase.query("CARDMEMBER", null, null, null, null, null, null);
        mycursor = sqLiteDatabase.query("USER", null, null, null, null, null, null);
        mycursor.moveToFirst();
        String userName = mycursor.getString(6).toString();
        System.out.println("userName:" + userName);
        String myLevel = "";
        for(int i = 0; i < 5; i++) {
            if(userName.toString().equals(level1[i])) {
                myLevel = "level1";
                System.out.println("1");
            }
        }
        if(myLevel.toString().equals("")) {
            for(int i = 0; i < 2; i++) {
                if(userName.toString().equals(level2[i])) {
                    myLevel = "level2";
                    System.out.println("2");
                }
            }
        }
        if(myLevel.toString().equals("")) {
            for(int i = 0; i < 4; i++) {
                if(userName.toString().equals(level3[i])) {
                    myLevel = "level3";
                    System.out.println("3");
                }
            }
        }
        if(myLevel.toString().equals("")) {
            for(int i = 0; i < 2; i++) {
                if(userName.toString().equals(level4[i])) {
                    myLevel = "level4";
                    System.out.println("4");
                }
            }
        }
        if(myLevel.toString().equals("")) {
            for(int i = 0; i < 2; i++) {
                if(userName.toString().equals(level5[i])) {
                    myLevel = "level5";
                    System.out.println("5");
                }
            }
        }
        if(myLevel.toString().equals("")) {
            myLevel = "level6";
            System.out.println("6");
        }
        System.out.println("myLevel: " + myLevel);
/*나랑 같은 위치에 있는애, 위에 있는애, 아래 있는애
* 만약에 myLevel이 level2면, level1만 위, 나머지 아래
* 5,2,4,2,2,1*/
        data1 = new ArrayList<Cardmember>();
        switch (posName) {
            case "up":
                while(cursor.moveToNext()) {
                    if(myLevel.equals("level1")) {
                    }
                    if(myLevel.equals("level2")) {
                        for(int i = 0; i < 5; i++) {
                            if(cursor.getString(6).toString().equals(level1[i])) {
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
                    }
                    if(myLevel.equals("level3")) {
                        for(int i = 0; i < 5; i++) {
                            if (cursor.getString(6).toString().equals(level1[i])) {
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
                        for(int i = 0; i < 2; i++) {
                            if (cursor.getString(6).toString().equals(level2[i])) {
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
                    }
                    if(myLevel.equals("level4")) {
                        for(int i = 0; i < 5; i++) {
                            if (cursor.getString(6).toString().equals(level1[i])) {
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
                        for(int i = 0; i < 2; i++) {
                            if (cursor.getString(6).toString().equals(level2[i])) {
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
                        for(int i = 0; i < 4; i++) {
                            if (cursor.getString(6).toString().equals(level3[i])) {
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
                    }
                    if(myLevel.equals("level5")) {
                        for(int i = 0; i < 5; i++) {
                            if (cursor.getString(6).toString().equals(level1[i])) {
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
                        for(int i = 0; i < 2; i++) {
                            if (cursor.getString(6).toString().equals(level2[i])) {
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
                        for(int i = 0; i < 4; i++) {
                            if (cursor.getString(6).toString().equals(level3[i])) {
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
                        for(int i = 0; i < 2; i++) {
                            if (cursor.getString(6).toString().equals(level4[i])) {
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
                    }
                    if(myLevel.equals("level6")) {
                        for(int i = 0; i < 5; i++) {
                            if (cursor.getString(6).toString().equals(level1[i])) {
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
                        for(int i = 0; i < 2; i++) {
                            if (cursor.getString(6).toString().equals(level2[i])) {
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
                        for(int i = 0; i < 4; i++) {
                            if (cursor.getString(6).toString().equals(level3[i])) {
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
                        for(int i = 0; i < 2; i++) {
                            if (cursor.getString(6).toString().equals(level4[i])) {
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
                        for(int i = 0; i < 2; i++) {
                            if (cursor.getString(6).toString().equals(level5[i])) {
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
                    }
                }
                break;
            case "same":
                while(cursor.moveToNext()) {
                    if(myLevel.equals("level1")) {
                        for(int i = 0; i < 5; i++) {
                            if (cursor.getString(6).toString().equals(level1[i])) {
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
                    }
                    if(myLevel.equals("level2")) {
                        for(int i = 0; i < 2; i++) {
                            if (cursor.getString(6).toString().equals(level2[i])) {
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
                    }
                    if(myLevel.equals("level3")) {
                        for(int i = 0; i < 4; i++) {
                            if (cursor.getString(6).toString().equals(level3[i])) {
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
                    }
                    if(myLevel.equals("level4")) {
                        for(int i = 0; i < 2; i++) {
                            if (cursor.getString(6).toString().equals(level4[i])) {
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
                    }
                    if(myLevel.equals("level5")) {
                        for(int i = 0; i < 2; i++) {
                            if (cursor.getString(6).toString().equals(level5[i])) {
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
                    }
                    if(myLevel.equals("level6")) {
                        for(int i = 0; i < 1; i++) {
                            if (cursor.getString(6).toString().equals(level6[i])) {
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
                    }
                }
                break;
            case "low":
                while(cursor.moveToNext()) {
                    if(myLevel.equals("level6")) {

                    }
                    if(myLevel.equals("level5")) {
                        for(int i = 0; i < 1; i++) {
                            if(cursor.getString(6).toString().equals(level6[i])) {
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
                    }
                    if(myLevel.equals("level4")) {
                        for(int i = 0; i < 1; i++) {
                            if (cursor.getString(6).toString().equals(level6[i])) {
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
                        for(int i = 0; i < 2; i++) {
                            if (cursor.getString(6).toString().equals(level5[i])) {
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
                    }
                    if(myLevel.equals("level3")) {
                        for(int i = 0; i < 1; i++) {
                            if (cursor.getString(6).toString().equals(level6[i])) {
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
                        for(int i = 0; i < 2; i++) {
                            if (cursor.getString(6).toString().equals(level5[i])) {
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
                        for(int i = 0; i < 2; i++) {
                            if (cursor.getString(6).toString().equals(level4[i])) {
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
                    }
                    if(myLevel.equals("level2")) {
                        for(int i = 0; i < 1; i++) {
                            if (cursor.getString(6).toString().equals(level6[i])) {
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
                        for(int i = 0; i < 2; i++) {
                            if (cursor.getString(6).toString().equals(level5[i])) {
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
                        for(int i = 0; i < 2; i++) {
                            if (cursor.getString(6).toString().equals(level4[i])) {
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
                        for(int i = 0; i < 4; i++) {
                            if (cursor.getString(6).toString().equals(level3[i])) {
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
                    }
                    if(myLevel.equals("level1")) {
                        for(int i = 0; i < 1; i++) {
                            if (cursor.getString(6).toString().equals(level6[i])) {
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
                        for(int i = 0; i < 2; i++) {
                            if (cursor.getString(6).toString().equals(level5[i])) {
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
                        for(int i = 0; i < 2; i++) {
                            if (cursor.getString(6).toString().equals(level4[i])) {
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
                        for(int i = 0; i < 4; i++) {
                            if (cursor.getString(6).toString().equals(level3[i])) {
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
                        for(int i = 0; i < 2; i++) {
                            if (cursor.getString(6).toString().equals(level2[i])) {
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
                    }
                }
                break;
        }
        cursor.close();
        mycursor.close();
        dbClose();
        adapter = new CardmemberAdapter(this, R.layout.card, data1);


        adapter.setItemClick(new CardmemberAdapter.ItemClick() {
            @Override
            public void onClick(View view, int position) {
                dbOpen();
                cursor = sqLiteDatabase.rawQuery("SELECT * FROM CARDMEMBER", null);
                Cursor c = cursor;
                c.moveToPosition(position);
                Intent i = new Intent(FragmentForThree.this, PrintInformation2.class);
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
