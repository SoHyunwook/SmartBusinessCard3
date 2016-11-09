package com.example.smartbusinesscard2;

/**
 * Created by 현욱 on 2016-11-09.
 */
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


public class EmailActivity extends Activity implements DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener {
    AlarmManager mManager;
    private GregorianCalendar mCalendar; // ?ㅼ젙 ?쇱떆
    private DatePicker mDate; // ?쇱옄 ?ㅼ젙 ?대옒??
    private TimePicker mTime; // ?쒖옉 ?ㅼ젙 ?대옒??
    private NotificationManager mNotification; // ?듭? 愿??硫ㅻ쾭 蹂??
    static long result;
    EditText subject, message;
    static String s_subject, s_message;

    DBManager dbManager;
    SQLiteDatabase sqLiteDatabase;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        subject = (EditText)findViewById(R.id.subject);
        message = (EditText)findViewById(R.id.message);

        mNotification = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); // ?듭? 留ㅻ땲?瑜?痍⑤뱷
        mManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE); // ?뚮엺 留ㅻ땲?瑜?痍⑤뱷
        mCalendar = new GregorianCalendar(); // ?꾩옱 ?쒓컖??痍⑤뱷
        Log.i("HelloAlarmActivity", mCalendar.getTime().toString());
        System.out.println("?꾩옱?쒓컙" + mCalendar.getTimeInMillis());

        //?쇱떆 ?ㅼ젙 ?대옒?ㅻ줈 ?꾩옱 ?쒓컖???ㅼ젙
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

                dbOpen();
                ContentValues values = new ContentValues();
                values.put("subject", s_subject);
                values.put("message", s_message);
                try {
                    sqLiteDatabase.insert("EMAIL", null, values);
                    System.out.println("success");
                } catch (SQLiteException e) {
                    System.out.println("insert error");
                }
                dbClose();
                result = setTime();
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
        System.out.println("?몄젣?쒓컙 = " + mCalendar.getTimeInMillis());
        System.out.println("?몄젣?쒓컙 = " + mCalendar.getTime().toString());
        return mCalendar.getTimeInMillis();
    }

    //?쇱옄 ?ㅼ젙 ?대옒?ㅼ쓽 ?곹깭蹂??由ъ뒪??
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mCalendar.set(year, monthOfYear, dayOfMonth);
        Log.i("datechange", mCalendar.getTime().toString());
        Date date = mCalendar.getTime();
        System.out.println("date = " + date);
        System.out.println("?좎쭨 = " + mCalendar.getTimeInMillis());
    }

    //?쒓컖 ?ㅼ젙 ?대옒?ㅼ쓽 ?곹깭蹂??由ъ뒪??
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        mCalendar.set(mDate.getYear(), mDate.getMonth(), mDate.getDayOfMonth(), hourOfDay, minute);
        Log.i("timechange", mCalendar.getTime().toString());
        Date time = mCalendar.getTime();
        System.out.println("time = " + time);
        System.out.println("?쒓컙 = " + mCalendar.getTimeInMillis());
    }

    //?뚮엺???ㅼ젙 ?쒓컖??諛쒖깮?섎뒗 ?명뀗???묒꽦
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