package com.example.smartbusinesscard2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Created by 현욱 on 2016-11-03.
 * 앱 사용자의 정보를 나타내주는 페이지(이는 앱 초기 실행시에만 나타남.
 * 이후에는 나타나지 않는 페이지)
 */
public class DialogCardInformation extends Activity implements View.OnClickListener {
    TextView nameTv, conameTv, emailTv, telTv, faxTv, posTv;
    DBManager dbManager;
    SQLiteDatabase sqLiteDatabase;
    RadioGroup rg, rg1, rg2;
    RadioButton rb, rb1, rb2;
    int id, id1, id2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_card_information);

        Intent intent = getIntent();

        findViewById(R.id.informEditBtn).setOnClickListener(this);
        findViewById(R.id.saveBtn).setOnClickListener(this);



        nameTv = (TextView)findViewById(R.id.nameTextView);
        String name = intent.getStringExtra("pname");
        nameTv.setText(String.format("%s", name));

        conameTv = (TextView)findViewById(R.id.comNameTextView);
        String comname = intent.getStringExtra("comname");
        conameTv.setText(String.format("%s", comname));

        emailTv = (TextView)findViewById(R.id.emailTextView);
        String email = intent.getStringExtra("email1");
        emailTv.setText(String.format("%s", email));

        telTv = (TextView)findViewById(R.id.phoneTextView);
        String tel = intent.getStringExtra("tel1");
        telTv.setText(String.format("%s", tel));

        faxTv = (TextView)findViewById(R.id.faxTextView);
        String fax = intent.getStringExtra("fax1");
        faxTv.setText(String.format("%s", fax));

        posTv = (TextView)findViewById(R.id.positionTextView);
        String pos = intent.getStringExtra("position");
        posTv.setText(String.format("%s", pos));

        rg = (RadioGroup)findViewById(R.id.radioGroup);
        rg1 = (RadioGroup)findViewById(R.id.radioGroup1);
        rg2 = (RadioGroup)findViewById(R.id.radioGroup2);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.informEditBtn:
                Intent intent1 = new Intent(DialogCardInformation.this,DialogCardEdit.class);

                intent1.putExtra("pname1", nameTv.getText());
                intent1.putExtra("comname1", conameTv.getText());
                intent1.putExtra("email2", emailTv.getText());
                intent1.putExtra("tel2", telTv.getText());
                intent1.putExtra("fax2", faxTv.getText());
                intent1.putExtra("pos1", posTv.getText());

                startActivityForResult(intent1, 1);
                break;
            case R.id.saveBtn:
                id = rg.getCheckedRadioButtonId();
                System.out.println("rg id:" + id);
                rb = (RadioButton)findViewById(id);
                id1 = rg1.getCheckedRadioButtonId();
                rb1 = (RadioButton)findViewById(id1);
                id2 = rg2.getCheckedRadioButtonId();
                rb2 = (RadioButton)findViewById(id2);
                dbOpen();
                ContentValues values = new ContentValues();
                if(rb.getText().toString().equals("Name"))
                    values.put("u_name", nameTv.getText().toString());
                if(rb.getText().toString().equals("Position"))
                    values.put("position", nameTv.getText().toString());
                if(rb.getText().toString().equals("Company Name"))
                    values.put("c_name", nameTv.getText().toString());
                if(rb1.getText().toString().equals("Name"))
                    values.put("u_name", conameTv.getText().toString());
                if(rb1.getText().toString().equals("Position"))
                    values.put("position", conameTv.getText().toString());
                if(rb1.getText().toString().equals("Company Name"))
                    values.put("c_name", conameTv.getText().toString());
                if(rb2.getText().toString().equals("Name"))
                    values.put("u_name", posTv.getText().toString());
                if(rb2.getText().toString().equals("Position"))
                    values.put("position", posTv.getText().toString());
                if(rb2.getText().toString().equals("Company Name"))
                    values.put("c_name", posTv.getText().toString());
                values.put("phone", telTv.getText().toString());
                values.put("email", emailTv.getText().toString());
                values.put("fax", faxTv.getText().toString());
                try {
                    sqLiteDatabase.insert("USER", null, values);
                } catch (SQLiteException e) {
                }
                dbClose();
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) // 액티비티가 정상적으로 종료되었을 경우
        {
            if(requestCode==1) // requestCode==1 로 호출한 경우에만 처리합니다.
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
