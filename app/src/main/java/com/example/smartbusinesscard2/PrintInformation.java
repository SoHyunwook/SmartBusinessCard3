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
 * 명함을 앨범에서 불러오거나 사진으로 찍은 후,
 * 그 정보를 출력해주는 페이지에 해당하는 java파일
 * 이곳에서 수정, 저장이 가능
 */
public class PrintInformation extends AppCompatActivity implements View.OnClickListener {

    TextView nameTv, conameTv, emailTv, telTv, faxTv, posTv;
    DBManager dbManager;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.print_information);

        findViewById(R.id.informEditBtn1).setOnClickListener(this);
        findViewById(R.id.saveBtn1).setOnClickListener(this);

        Intent intent = getIntent();

        nameTv = (TextView)findViewById(R.id.nameTextView1);
        String name = intent.getStringExtra("pname");
        nameTv.setText(String.format("%s", name));

        conameTv = (TextView)findViewById(R.id.comNameTextView1);
        String comname = intent.getStringExtra("comname");
        conameTv.setText(String.format("%s", comname));

        emailTv = (TextView)findViewById(R.id.emailTextView1);
        String email = intent.getStringExtra("email1");
        emailTv.setText(String.format("%s", email));

        telTv = (TextView)findViewById(R.id.phoneTextView1);
        String tel = intent.getStringExtra("tel1");
        telTv.setText(String.format("%s", tel));

        faxTv = (TextView)findViewById(R.id.faxTextView1);
        String fax = intent.getStringExtra("fax1");
        faxTv.setText(String.format("%s", fax));

        posTv = (TextView)findViewById(R.id.positionTextView1);
        String pos = intent.getStringExtra("position");
        posTv.setText(String.format("%s", pos));
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.informEditBtn1:
                Intent intent1 = new Intent(PrintInformation.this,DialogCardEdit.class);

                intent1.putExtra("pname1", nameTv.getText());
                intent1.putExtra("comname1", conameTv.getText());
                intent1.putExtra("email2", emailTv.getText());
                intent1.putExtra("tel2", telTv.getText());
                intent1.putExtra("fax2", faxTv.getText());
                intent1.putExtra("pos1", posTv.getText());

                startActivityForResult(intent1, 1);
                break;
            case R.id.saveBtn1:
                dbOpen();
                ContentValues values = new ContentValues();
                values.put("u_name", nameTv.getText().toString());
                values.put("c_name", conameTv.getText().toString());
                values.put("phone", telTv.getText().toString());
                values.put("email", emailTv.getText().toString());
                values.put("fax", faxTv.getText().toString());
                values.put("position", posTv.getText().toString());
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
