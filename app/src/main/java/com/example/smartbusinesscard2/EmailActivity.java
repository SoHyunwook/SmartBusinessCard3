package com.example.smartbusinesscard2;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.client.util.ExponentialBackOff;

import com.google.api.services.gmail.GmailScopes;

import com.google.api.services.gmail.model.*;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by yi_te on 2016-11-09.
 */

public class EmailActivity extends Activity implements DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener {
    AlarmManager mManager;
    private GregorianCalendar mCalendar;
    private DatePicker mDate;
    private TimePicker mTime;
    private NotificationManager mNotification;
    static long result;
    EditText subject, message, address;
    static String s_subject, s_message, s_address, s_time;

    DBManager dbManager;
    SQLiteDatabase sqLiteDatabase;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        FontClass.setDefaultFont(this, "DEFAULT", "NotoSans-Regular.ttf");
        FontClass.setDefaultFont(this, "MONOSPACE", "NotoSans-Regular.ttf");
        FontClass.setDefaultFont(this, "SERIF", "NotoSans-Regular.ttf");
        FontClass.setDefaultFont(this, "SANS_SERIF", "NotoSans-Bold.ttf");

        subject = (EditText)findViewById(R.id.subject);
        message = (EditText)findViewById(R.id.message);
        address = (EditText)findViewById(R.id.mail);

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
                s_subject = subject.getText().toString();
                s_message = message.getText().toString();
                s_address = address.getText().toString();
                result = setTime();
                s_time = mCalendar.getTime().toString();

                dbOpen();
                ContentValues values = new ContentValues();
                values.put("subject", subject.getText().toString());
                values.put("message", message.getText().toString());
                values.put("show_time", s_time);
                values.put("time", result);
                values.put("to_whom", address.getText().toString());
                System.out.println("msg = "+ message.getText().toString());
                try {
                    sqLiteDatabase.insert("EMAIL", null, values);
                    System.out.println("success");
                } catch (SQLiteException e) {
                    System.out.println("insert error");
                }
                dbClose();
                System.out.println("result = " + result);
                mManager.set(AlarmManager.RTC_WAKEUP, result, pendingIntent());
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

    private long setTime() {
        return mCalendar.getTimeInMillis();
    }

    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mCalendar.set(year, monthOfYear, dayOfMonth);
    }

    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        mCalendar.set(mDate.getYear(), mDate.getMonth(), mDate.getDayOfMonth(), hourOfDay, minute);
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