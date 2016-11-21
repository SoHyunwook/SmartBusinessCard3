package com.example.smartbusinesscard2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created by yi_te on 2016-11-21.
 */
public class EditEmail extends Activity implements View.OnClickListener {
    EditText sub2, msg2, whom2;
    DBManager dbManager;
    SQLiteDatabase sqLiteDatabase;
    public long imsi = -2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_edit);

        FontClass.setDefaultFont(this, "DEFAULT", "NotoSans-Regular.ttf");
        FontClass.setDefaultFont(this, "MONOSPACE", "NotoSans-Regular.ttf");
        FontClass.setDefaultFont(this, "SERIF", "NotoSans-Regular.ttf");
        FontClass.setDefaultFont(this, "SANS_SERIF", "NotoSans-Bold.ttf");

        findViewById(R.id.save_b).setOnClickListener(this);
        findViewById(R.id.cancle_b).setOnClickListener(this);

        sub2 = (EditText) findViewById(R.id.subject3);
        msg2 = (EditText) findViewById(R.id.message3);
        whom2 = (EditText) findViewById(R.id.mail3);

        Intent intent1 = getIntent();

        String sub = intent1.getStringExtra("sub2");
        String msg = intent1.getStringExtra("msg2");
        String whom = intent1.getStringExtra("whom2");

        sub2.setText(String.format("%s", sub));
        msg2.setText(String.format("%s", msg));
        whom2.setText(String.format("%s", whom));
    }

    @Override
    public void onClick(View v) {
        Intent i = getIntent();
        switch (v.getId()) {
            case R.id.cancle_b:
                finish();
                break;
            case R.id.save_b:
                dbOpen();
                ContentValues values = new ContentValues();
                values.put("subject", sub2.getText().toString());
                values.put("message", msg2.getText().toString());
                values.put("to_whom", whom2.getText().toString());
                try {
                    long imsi2 = i.getLongExtra("_id", imsi) + 1;
                    System.out.println("imsi2: " + imsi2);
                    if (imsi2 >= 0) {
                        String sql = "update EMAIL" + " set subject = '" + sub2.getText().toString() +
                                "', message = '" + msg2.getText().toString() +
                                "', to_whom = '" + whom2.getText().toString() +
                                "' where _id = " + imsi2 + ";";
                        System.out.println("sql:" + sql);
                        sqLiteDatabase.execSQL(sql);
                        System.out.println("update db");
                    } else
                        sqLiteDatabase.insert("EMAIL", null, values);
                } catch (SQLiteException e) {
                    System.out.println("insert error");
                }
                dbClose();
                finish();
                System.out.println("finish() called");
                break;
        }
    }

    void dbOpen() {
        if (dbManager == null) {
            dbManager = new DBManager(this, "myDB.db", null, 1);
        }
        sqLiteDatabase = dbManager.getWritableDatabase();
    }

    void dbClose() {
        if (sqLiteDatabase != null) {
            if (sqLiteDatabase.isOpen()) {
                sqLiteDatabase.close();
            }
        }
    }
}