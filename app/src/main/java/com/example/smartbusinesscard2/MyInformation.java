package com.example.smartbusinesscard2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by 현욱 on 2016-11-07.
 * 앱 사용자의 정보를 출력해주는 java파일
 */
public class MyInformation extends AppCompatActivity implements View.OnClickListener {

    TextView nameTv, conameTv, emailTv, telTv, faxTv, posTv;
    DBManager dbManager;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_information);

        FontClass.setDefaultFont(this, "DEFAULT", "NotoSans-Regular.ttf");
        FontClass.setDefaultFont(this, "MONOSPACE", "NotoSans-Regular.ttf");
        FontClass.setDefaultFont(this, "SERIF", "NotoSans-Regular.ttf");
        FontClass.setDefaultFont(this, "SANS_SERIF", "NotoSans-Bold.ttf");

        findViewById(R.id.informEditBtn2).setOnClickListener(this);
        findViewById(R.id.backBtn).setOnClickListener(this);
        findViewById(R.id.saveBtn2).setOnClickListener(this);

        nameTv = (TextView)findViewById(R.id.nameTextView2);
        conameTv = (TextView)findViewById(R.id.comNameTextView2);
        emailTv = (TextView)findViewById(R.id.emailTextView2);
        telTv = (TextView)findViewById(R.id.phoneTextView2);
        faxTv = (TextView)findViewById(R.id.faxTextView2);
        posTv = (TextView)findViewById(R.id.positionTextView2);

        dbManager = new DBManager(this, "myDB.db", null, 1);
        sqLiteDatabase = dbManager.getReadableDatabase();
        cursor = sqLiteDatabase.query("USER", null, null, null, null, null, null);
        cursor.moveToFirst();
        nameTv.setText(cursor.getString(1).toString());
        conameTv.setText(cursor.getString(2).toString());
        emailTv.setText(cursor.getString(4).toString());
        telTv.setText(cursor.getString(3).toString());
        faxTv.setText(cursor.getString(5).toString());
        posTv.setText(cursor.getString(6).toString());

        cursor.close();
        sqLiteDatabase.close();
        dbManager.close();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.informEditBtn2:
                Intent intent1 = new Intent(MyInformation.this,DialogCardEdit.class);

                intent1.putExtra("pname1", nameTv.getText());
                intent1.putExtra("comname1", conameTv.getText());
                intent1.putExtra("email2", emailTv.getText());
                intent1.putExtra("tel2", telTv.getText());
                intent1.putExtra("fax2", faxTv.getText());
                intent1.putExtra("pos1", posTv.getText());

                startActivityForResult(intent1, 1);
                break;
            case R.id.backBtn:
                finish();
                break;
            case R.id.saveBtn2:
                dbOpen();
                ContentValues values = new ContentValues();
                values.put("u_name", nameTv.getText().toString());
                values.put("c_name", conameTv.getText().toString());
                values.put("phone", telTv.getText().toString());
                values.put("email", emailTv.getText().toString());
                values.put("fax", faxTv.getText().toString());
                values.put("position", posTv.getText().toString());
                sqLiteDatabase.update("USER", values, "_id=1", null);
                try {
                    sqLiteDatabase.insert("USER", null, values);
                } catch (SQLiteException e) {
                    System.out.println("insert error");
                }
                dbClose();
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) // 액티비티가 정상적으로 종료되었을 경우
        {
            if (requestCode == 1) // requestCode==1 로 호출한 경우에만 처리합니다.
            {
                nameTv.setText(String.format("%s", data.getStringExtra("name4")));
                conameTv.setText(String.format("%s", data.getStringExtra("com4")));
                emailTv.setText(String.format("%s", data.getStringExtra("email4")));
                telTv.setText(String.format("%s", data.getStringExtra("phone4")));
                faxTv.setText(String.format("%s", data.getStringExtra("fax4")));
                System.out.println("data.getStringExtra(fax4):" + data.getStringExtra("fax4"));
                posTv.setText(String.format("%s", data.getStringExtra("pos4")));

            }
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
