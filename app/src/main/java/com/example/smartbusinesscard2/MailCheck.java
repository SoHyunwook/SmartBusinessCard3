package com.example.smartbusinesscard2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by yi_te on 2016-11-09.
 */
public class MailCheck extends AppCompatActivity{
    TextView su, me;
    DBManager dbManager;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_mail);

        su = (TextView)findViewById(R.id.subj);
        me = (TextView)findViewById(R.id.mess);

        dbManager = new DBManager(this, "myDB.db", null, 1);
        sqLiteDatabase = dbManager.getReadableDatabase();
        cursor = sqLiteDatabase.query("EMAIL", null, null, null, null, null, null);
        cursor.moveToFirst();
        System.out.println("?뚰쁽??+cursor.moveToFirst()");
                su.setText(cursor.getString(1).toString());
        System.out.println("?닿굅"+cursor.getString(1).toString());
        System.out.println("?쒕컻"+cursor.getString(2).toString());
        me.setText(cursor.getString(2).toString());

        cursor.close();
        sqLiteDatabase.close();
        dbManager.close();
    }
}

