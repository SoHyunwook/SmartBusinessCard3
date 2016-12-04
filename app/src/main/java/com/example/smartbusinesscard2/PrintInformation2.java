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
 * Created by 현욱 on 2016-11-08.
 * listview에서 클릭했을 때, 나오는 정보화면
 */
public class PrintInformation2  extends AppCompatActivity implements View.OnClickListener {
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.print_information_2);

        FontClass.setDefaultFont(this, "DEFAULT", "NotoSans-Regular.ttf");
        FontClass.setDefaultFont(this, "MONOSPACE", "NotoSans-Regular.ttf");
        FontClass.setDefaultFont(this, "SERIF", "NotoSans-Regular.ttf");
        FontClass.setDefaultFont(this, "SANS_SERIF", "NotoSans-Bold.ttf");

        findViewById(R.id.informEditBtn5).setOnClickListener(this);
        findViewById(R.id.saveBtn5).setOnClickListener(this);
        findViewById(R.id.backBtn5).setOnClickListener(this);
        findViewById(R.id.ivContactItem1).setOnClickListener(this);

        Intent intent = getIntent();

        nameTv = (TextView)findViewById(R.id.nameTextView5);
        String name = intent.getStringExtra("pname");
        nameTv.setText(String.format("%s", name));

        conameTv = (TextView)findViewById(R.id.comNameTextView5);
        String comname = intent.getStringExtra("comname");
        conameTv.setText(String.format("%s", comname));

        emailTv = (TextView)findViewById(R.id.emailTextView5);
        String email = intent.getStringExtra("email1");
        emailTv.setText(String.format("%s", email));

        telTv = (TextView)findViewById(R.id.phoneTextView5);
        String tel = intent.getStringExtra("tel1");
        telTv.setText(String.format("%s", tel));

        faxTv = (TextView)findViewById(R.id.faxTextView5);
        String fax = intent.getStringExtra("fax1");
        faxTv.setText(String.format("%s", fax));

        posTv = (TextView)findViewById(R.id.positionTextView5);
        String pos = intent.getStringExtra("position");
        posTv.setText(String.format("%s", pos));

        rg = (RadioGroup)findViewById(R.id.radioGroup6);
        rg1 = (RadioGroup)findViewById(R.id.radioGroup7);
        rg2 = (RadioGroup)findViewById(R.id.radioGroup8);
    }

    public void onClick(View v) {
        Intent i = getIntent();
        switch (v.getId()) {
            case R.id.ivContactItem1:
                String data = telTv.getText().toString();
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + data));
                startActivity(myIntent);
                break;
            case R.id.backBtn5:
                finish();
                break;
            case R.id.informEditBtn5:
                Intent intent1 = new Intent(PrintInformation2.this,DialogCardEdit.class);

                intent1.putExtra("pname1", nameTv.getText());
                intent1.putExtra("comname1", conameTv.getText());
                intent1.putExtra("email2", emailTv.getText());
                intent1.putExtra("tel2", telTv.getText());
                intent1.putExtra("fax2", faxTv.getText());
                intent1.putExtra("pos1", posTv.getText());

                startActivityForResult(intent1, 1);
                break;
            case R.id.saveBtn5:
                id = rg.getCheckedRadioButtonId();
                System.out.println("rg id:" + id);
                rb = (RadioButton)findViewById(id);
                id1 = rg1.getCheckedRadioButtonId();
                rb1 = (RadioButton)findViewById(id1);
                id2 = rg2.getCheckedRadioButtonId();
                rb2 = (RadioButton)findViewById(id2);
                dbOpen();
                ContentValues values = new ContentValues();
                if(rb.getText().toString().equals("Name")) {
                    values.put("p_name", nameTv.getText().toString());
                    values.put("op_name", nameTv.getText().toString());
                }
                if(rb.getText().toString().equals("Position"))
                    values.put("position", nameTv.getText().toString());
                if(rb.getText().toString().equals("Company Name"))
                    values.put("c_name", nameTv.getText().toString());
                if(rb1.getText().toString().equals("Name")) {
                    values.put("p_name", conameTv.getText().toString());
                    values.put("op_name", conameTv.getText().toString());
                }
                if(rb1.getText().toString().equals("Position"))
                    values.put("position", conameTv.getText().toString());
                if(rb1.getText().toString().equals("Company Name"))
                    values.put("c_name", conameTv.getText().toString());
                if(rb2.getText().toString().equals("Name")) {
                    values.put("p_name", posTv.getText().toString());
                    values.put("op_name", posTv.getText().toString());
                }
                if(rb2.getText().toString().equals("Position"))
                    values.put("position", posTv.getText().toString());
                if(rb2.getText().toString().equals("Company Name"))
                    values.put("c_name", posTv.getText().toString());
                values.put("phone", telTv.getText().toString());
                values.put("ophone", telTv.getText().toString());
                values.put("email", emailTv.getText().toString());
                values.put("fax", faxTv.getText().toString());
                try {
                    int imsi2 = i.getIntExtra("_id", imsi) + 1;
                    String phonenum = i.getStringExtra("ophone");
                    String op_name = i.getStringExtra("op_name");
                    System.out.println("imsi2: " + imsi2);
                    if(imsi2 >= 0) {
                        String sql = "update CARDMEMBER" + " set p_name = '" + nameTv.getText().toString()  +
                                "', c_name = '" + conameTv.getText().toString() +
                                "', phone = '" + telTv.getText().toString() +
                                "', email = '" + emailTv.getText().toString() +
                                "', fax = '" + faxTv.getText().toString() +
                                "', position = '" + posTv.getText().toString() +
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
                System.out.println("Print");
                finish();
                startActivity(new Intent(this, MainActivity.class));
                System.out.println("end of Print");
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
