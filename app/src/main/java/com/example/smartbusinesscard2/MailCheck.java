package com.example.smartbusinesscard2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yi_te on 2016-11-09.
 */

public class MailCheck extends AppCompatActivity{
    DBManager dbManager;
    SQLiteDatabase sqLiteDatabase;
    EmailInfoAdapter adapter;
    ArrayList<EmailInformation> data = null;
    Cursor cursor;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_mail);

        FontClass.setDefaultFont(this, "DEFAULT", "NotoSans-Regular.ttf");
        FontClass.setDefaultFont(this, "MONOSPACE", "NotoSans-Regular.ttf");
        FontClass.setDefaultFont(this, "SERIF", "NotoSans-Regular.ttf");
        FontClass.setDefaultFont(this, "SANS_SERIF", "NotoSans-Bold.ttf");

        dbOpen();
        list = (ListView)findViewById(R.id.listview2);
        cursor = sqLiteDatabase.query("EMAIL", null, null, null, null, null, null);
        data = new ArrayList<EmailInformation>();
        EmailInformation emailinfo;
        while(cursor.moveToNext()){
            emailinfo = new EmailInformation();
            emailinfo._id = cursor.getInt(0);
            emailinfo.subject = cursor.getString(1);
            emailinfo.message = cursor.getString(2);
            emailinfo.time = cursor.getString(3);
            emailinfo.show_time = cursor.getString(4);
            emailinfo.to_whom = cursor.getString(5);
            data.add(emailinfo);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        cursor.close();
        dbClose();
        dbManager.close();

        adapter = new EmailInfoAdapter(this, R.layout.email, data);
        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dbOpen();
                System.out.println("onItemClick: " + id);
                System.out.println("onItemClick position:" + position);
                cursor = sqLiteDatabase.rawQuery("SELECT * FROM EMAIL", null);
                Cursor c = cursor;
                c.moveToPosition(position);
                Intent i = new Intent(MailCheck.this, PrintEmail.class);
                i.putExtra("_id", id);
                i.putExtra("sbj", c.getString(c.getColumnIndexOrThrow("subject")));
                i.putExtra("msg", c.getString(c.getColumnIndexOrThrow("message")));
                i.putExtra("time", c.getString(c.getColumnIndexOrThrow("time")));
                i.putExtra("s_time", c.getString(c.getColumnIndexOrThrow("show_time")));
                i.putExtra("whom", c.getString(c.getColumnIndexOrThrow("to_whom")));
//                i.putExtra("position", c.getString(c.getColumnIndexOrThrow("position")));
                startActivity(i);
                System.out.println("ggggggggggggggggg");

                cursor.close();
                dbClose();
                dbManager.close();
                System.out.println("onclick end");
            }
        });
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
