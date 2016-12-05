package com.example.smartbusinesscard2;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by yi_te on 2016-11-09.
 */

public class EmailActivity extends Activity implements DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener {
    AlarmManager mManager;
    private GregorianCalendar mCalendar;
    private DatePicker mDate;
    private TimePicker mTime;
    private NotificationManager mNotification;
    static long result, date;
    EditText subject, message, address;
    static String s_subject, s_message, s_address, s_time;
    static ArrayList address2 = new ArrayList();
    String aaa;
    static int count = 0, end;
    ArrayList<String> arraylist;
    String select_item;
    int flag;
    private SelectCompany add_c = new SelectCompany();
    private SelectPosition add_p = new SelectPosition();

    DBManager dbManager;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        date = System.currentTimeMillis();

        arraylist = new ArrayList<String>();
        arraylist.add("Select Email");
        arraylist.add("Company");
        arraylist.add("Position");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arraylist);
        Spinner sp = (Spinner) this.findViewById(R.id.mail);
        sp.setPrompt("Select Email");
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        System.out.println("position = "+position);
                        select_item = String.valueOf(arraylist.get(position));
                        System.out.println("select_item = "+select_item);
                        if(select_item.toString().equals("Company")){
                            flag = 1;
                            Intent intent = new Intent(EmailActivity.this, SelectCompany.class);
                            startActivity(intent);
                        }
                        else if(select_item.toString().equals("Position")){
                            flag = 2;
                            Intent intent = new Intent(EmailActivity.this, SelectPosition.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        subject = (EditText)findViewById(R.id.subject);
        message = (EditText)findViewById(R.id.message);
//        address = (EditText)findViewById(R.id.mail);

        mNotification = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mCalendar = new GregorianCalendar();
        Log.i("현재시간", mCalendar.getTime().toString());

        mDate = (DatePicker) findViewById(R.id.date_picker);
        mDate.init(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), this);
        mTime = (TimePicker) findViewById(R.id.time_picker);
        mTime.setOnTimeChangedListener(this);

        Button cancel = (Button) findViewById(R.id.cancel);
        Button send = (Button) findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start = 4, index = 0;
                s_subject = subject.getText().toString();
                s_message = message.getText().toString();
                if(flag == 1)
                    s_address = add_c.EmailAddress;
                else if(flag == 2)
                    s_address = add_p.Address;
                //s_address = address.getText().toString();
                result = mCalendar.getTimeInMillis();
                s_time = mCalendar.getTime().toString();

                address2.clear();
                for(int i=4; i<s_address.length(); i++){
                    System.out.println("length = " + s_address.length());
                    System.out.println("s_address = " + s_address);
                    if(s_address.charAt(i) == ','){
                        end = i;
                        address2.add(index++, s_address.substring(start,end));
                        System.out.println("substring = " + s_address.substring(start, end));
                        start = end+1;
                        count++;
                    }
                }
                aaa = s_address.substring(4,end); // null때문
                System.out.println("aaa = " + aaa);
                System.out.println("index = " + index);
                index=0;
                System.out.println("index = " + index);
                dbOpen();
                ContentValues values = new ContentValues();
                values.put("subject", subject.getText().toString());
                values.put("message", message.getText().toString());
                values.put("show_time", s_time);
                values.put("time", result);
                values.put("to_whom", aaa);
                values.put("or_time", result);
                values.put("or_sub", subject.getText().toString());
                System.out.println("msg = " + message.getText().toString());
                try {
                    sqLiteDatabase.insert("EMAIL", null, values);
                    System.out.println("success");
                } catch (SQLiteException e) {
                    System.out.println("insert error");
                }
                dbClose();
                setTime();
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setTime() {
        int count2 = 0;
        dbOpen();
        sqLiteDatabase = dbManager.getReadableDatabase();
        cursor = sqLiteDatabase.rawQuery("SELECT * FROM EMAIL ORDER BY time ASC;", null);
        PendingIntent[] sender = new PendingIntent[cursor.getCount()];
        while(cursor.moveToNext()){
            if(date > cursor.getLong(3))
                continue;
            Intent i = new Intent(EmailActivity.this, APIConnected.class);
            sender[count2] = PendingIntent.getActivity(EmailActivity.this, count2, i, 0);
            mManager.set(AlarmManager.RTC_WAKEUP, result, sender[count2]);
            count2++;
            System.out.println("count2 = " + count2);
        }
        cursor.close();
        dbClose();
        dbManager.close();
    }

    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mCalendar.set(year, monthOfYear, dayOfMonth);
        System.out.println("date changed = " + mCalendar.getTimeInMillis());
    }

    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        mCalendar.set(mDate.getYear(), mDate.getMonth(), mDate.getDayOfMonth(), hourOfDay, minute);
        System.out.println("time changed = " + mCalendar.getTimeInMillis());
    }

    public PendingIntent pendingIntent() {
        Intent i = new Intent(EmailActivity.this, APIConnected.class);
        PendingIntent pi = PendingIntent.getActivity(EmailActivity.this, 0, i, 0);
        return pi;
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