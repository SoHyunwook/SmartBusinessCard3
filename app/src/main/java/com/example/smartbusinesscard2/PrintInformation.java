package com.example.smartbusinesscard2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Created by 현욱 on 2016-11-07.
 * 명함을 앨범에서 불러오거나 사진으로 찍은 후,
 * 그 정보를 출력해주는 페이지에 해당하는 java파일
 * 이곳에서 수정, 저장이 가능
 */
public class PrintInformation extends AppCompatActivity implements View.OnClickListener {

    TextView nameTv, conameTv, emailTv, telTv, faxTv, posTv;
    RadioGroup rg, rg1, rg2;
    RadioButton rb, rb1, rb2;
    int id, id1, id2;
    DBManager dbManager;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    public int imsi = -2;
    public String phonenum = "";
    public String pname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("PrintInformation");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.print_information);

        FontClass.setDefaultFont(this, "DEFAULT", "NotoSans-Regular.ttf");
        FontClass.setDefaultFont(this, "MONOSPACE", "NotoSans-Regular.ttf");
        FontClass.setDefaultFont(this, "SERIF", "NotoSans-Regular.ttf");
        FontClass.setDefaultFont(this, "SANS_SERIF", "NotoSans-Bold.ttf");

        findViewById(R.id.informEditBtn1).setOnClickListener(this);
        findViewById(R.id.saveBtn1).setOnClickListener(this);
        findViewById(R.id.backBtn7).setOnClickListener(this);
        findViewById(R.id.ivContactItem1).setOnClickListener(this);

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

        rg = (RadioGroup)findViewById(R.id.radioGroup3);
        rg1 = (RadioGroup)findViewById(R.id.radioGroup5);
        rg2 = (RadioGroup)findViewById(R.id.radioGroup4);
    }

    public void onClick(View v) {
        Intent i = getIntent();
        switch (v.getId()) {
            case R.id.ivContactItem1:
                String data = telTv.getText().toString();
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + data));
                startActivity(myIntent);
                break;
            case R.id.backBtn7:
                finish();
                break;
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
                String personname="", companyname="", positionname="";
                id = rg.getCheckedRadioButtonId();
                System.out.println("rg id:" + id);
                rb = (RadioButton)findViewById(id);
                id1 = rg1.getCheckedRadioButtonId();
                System.out.println("rg id1:" + id1);
                rb1 = (RadioButton)findViewById(id1);
                id2 = rg2.getCheckedRadioButtonId();
                System.out.println("rg id2:" + id2);
                rb2 = (RadioButton)findViewById(id2);
                dbOpen();
                ContentValues values = new ContentValues();
                System.out.println("rb: " + rb.getText().toString());
                if(rb.getText().toString().equals("Name")) {
                    personname = nameTv.getText().toString();
                    values.put("p_name", nameTv.getText().toString());
                    values.put("op_name", nameTv.getText().toString());
                }
                if(rb.getText().toString().equals("Position")) {
                    positionname = nameTv.getText().toString();
                    values.put("position", nameTv.getText().toString());
                }
                if(rb.getText().toString().equals("Company Name")) {
                    companyname = nameTv.getText().toString();
                    values.put("c_name", nameTv.getText().toString());
                }
                if(rb1.getText().toString().equals("Name")) {
                    personname = conameTv.getText().toString();
                    values.put("p_name", conameTv.getText().toString());
                    values.put("op_name", conameTv.getText().toString());
                }
                System.out.println("rb1: " + rb1.getText().toString());
                if(rb1.getText().toString().equals("Position")) {
                    positionname = conameTv.getText().toString();
                    values.put("position", conameTv.getText().toString());
                }
                if(rb1.getText().toString().equals("Company Name")) {
                    companyname = conameTv.getText().toString();
                    values.put("c_name", conameTv.getText().toString());
                }
                if(rb2.getText().toString().equals("Name")) {
                    personname = posTv.getText().toString();
                    values.put("p_name", posTv.getText().toString());
                    values.put("op_name", posTv.getText().toString());
                }
                System.out.println("rb2: " + rb2.getText().toString());
                if(rb2.getText().toString().equals("Position")) {
                    positionname = posTv.getText().toString();
                    values.put("position", posTv.getText().toString());
                }
                if(rb2.getText().toString().equals("Company Name")) {
                    companyname = posTv.getText().toString();
                    values.put("c_name", posTv.getText().toString());
                }

                values.put("phone", telTv.getText().toString());
                values.put("ophone", telTv.getText().toString());
                values.put("email", emailTv.getText().toString());
                values.put("fax", faxTv.getText().toString());
                try {
                    System.out.println("_id:" + i.getLongExtra("_id", imsi));
                    int imsi2 = i.getIntExtra("_id", imsi) + 1;
                    String phonenum = i.getStringExtra("ophone");
                    String op_name = i.getStringExtra("op_name");
                    System.out.println("imsi2: " + imsi2);
                    if(imsi2 >= 0) {
                        String sql = "update CARDMEMBER" + " set p_name = '" + personname  +
                                "', c_name = '" + companyname +
                                "', phone = '" + telTv.getText().toString() +
                                "', email = '" + emailTv.getText().toString() +
                                "', fax = '" + faxTv.getText().toString() +
                                "', position = '" + positionname +
                                "' where op_name = '" +op_name +
                                "' and ophone = '" + phonenum +
                                "';";
                        System.out.println("sql:" + sql);
                        sqLiteDatabase.execSQL(sql);
                        System.out.println("update db");
                    }
                    else
                        sqLiteDatabase.insert("CARDMEMBER", null, values);
                } catch (SQLiteException e) {
                    System.out.println("insert error");
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
