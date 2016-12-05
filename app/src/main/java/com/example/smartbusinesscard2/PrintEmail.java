package com.example.smartbusinesscard2;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by yi_te on 2016-11-16.
 */
public class PrintEmail extends AppCompatActivity implements View.OnClickListener {
    TextView sub1, msg1, whom1, s_time1;
    private MailCheck get2 = new MailCheck();
    DBManager dbManager;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_print);

        FontClass.setDefaultFont(this, "DEFAULT", "NotoSans-Regular.ttf");
        FontClass.setDefaultFont(this, "MONOSPACE", "NotoSans-Regular.ttf");
        FontClass.setDefaultFont(this, "SERIF", "NotoSans-Regular.ttf");
        FontClass.setDefaultFont(this, "SANS_SERIF", "NotoSans-Bold.ttf");

        findViewById(R.id.delete_b).setOnClickListener(this);
        findViewById(R.id.ok_b).setOnClickListener(this);

        Intent intent = getIntent();

        sub1 = (TextView) findViewById(R.id.subject2);
        String subject = intent.getStringExtra("sbj");
        sub1.setText(String.format("%s", subject));

        msg1 = (TextView) findViewById(R.id.message2);
        String message = intent.getStringExtra("msg");
        msg1.setText(String.format("%s", message));

        whom1 = (TextView) findViewById(R.id.email2);
        String email = intent.getStringExtra("whom");
        whom1.setText(String.format("%s", email));

        s_time1 = (TextView) findViewById(R.id.time2);
        String time2 = intent.getStringExtra("s_time");
        s_time1.setText(String.format("%s", time2));
    }

    public void onClick(View v) {
        Intent i = getIntent();
        switch (v.getId()) {
            case R.id.delete_b:
                AlertDialog.Builder builder = new AlertDialog.Builder(PrintEmail.this);
                builder.setMessage("Do you want to delete?")
                        .setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int whichButton){
                                dbOpen();
                                cursor = sqLiteDatabase.rawQuery("SELECT * FROM EMAIL", null);
                                Cursor c1 = cursor;
                                c1.moveToPosition(get2.position2);
                                System.out.println("position:" + get2.position2);
                                String sql = "delete from EMAIL" + " where or_time = '"+ c1.getString(c1.getColumnIndexOrThrow("or_time")) + "' and or_sub = '" + c1.getString(c1.getColumnIndexOrThrow("or_sub")) + "';";
                                System.out.println("sql delete:" + sql);
                                try {
                                    sqLiteDatabase.execSQL(sql);
                                    System.out.println("complete delete");
                                } catch(SQLiteException e) {
                                    System.out.println("error delete:" + e);
                                }
                                Intent intent = new Intent(PrintEmail.this, MailCheck.class);
                                startActivity(intent);
                                cursor.close();
                                dbClose();
                                dbManager.close();
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });
                AlertDialog dialog = builder.create();    // 알림창 객체 생성
                dialog.show();    // 알림창 띄우기
                break;
            case R.id.ok_b:
                finish();
                break;
        }
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