package com.example.smartbusinesscard2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by 현욱 on 2016-11-05.
 * 명함의 정보를 수정할 수 있는 페이지
 */
public class DialogCardEdit extends Activity implements View.OnClickListener {
    EditText nameEt, posEt, comEt, phoneEt, faxEt, emailEt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_card_edit);

        findViewById(R.id.confirmBtn).setOnClickListener(this);
        findViewById(R.id.cancleBtn).setOnClickListener(this);

        nameEt = (EditText)findViewById(R.id.nameEditText);
        posEt = (EditText)findViewById(R.id.positionEditText);
        comEt = (EditText)findViewById(R.id.comNameEditText);
        phoneEt = (EditText)findViewById(R.id.phoneEditText);
        faxEt = (EditText)findViewById(R.id.faxEditText);
        emailEt = (EditText)findViewById(R.id.emailEditText);

        Intent intent1 = getIntent();

        String name = intent1.getStringExtra("pname1");
        String position = intent1.getStringExtra("pos1");
        String company = intent1.getStringExtra("comname1");
        String phone = intent1.getStringExtra("tel2");
        String fax = intent1.getStringExtra("fax2");
        String email = intent1.getStringExtra("email2");

        System.out.println("Oncreate called");

        nameEt.setText(String.format("%s", name));
        posEt.setText(String.format("%s", position));
        comEt.setText(String.format("%s", company));
        phoneEt.setText(String.format("%s", phone));
        faxEt.setText(String.format("%s", fax));
        emailEt.setText(String.format("%s", email));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancleBtn:
                Intent intent = new Intent(DialogCardEdit.this,DialogCardInformation.class);
                Intent intent3 = new Intent(DialogCardEdit.this, PrintInformation.class);
                intent.putExtra("name4", nameEt.getText().toString());
                intent3.putExtra("name4", nameEt.getText().toString());
                intent.putExtra("pos4", posEt.getText().toString());
                intent3.putExtra("pos4", posEt.getText().toString());
                intent.putExtra("com4", comEt.getText().toString());
                intent3.putExtra("com4", comEt.getText().toString());
                intent.putExtra("phone4", phoneEt.getText().toString());
                intent3.putExtra("phone4", phoneEt.getText().toString());
                intent.putExtra("fax4", faxEt.getText().toString());
                intent3.putExtra("fax4", faxEt.getText().toString());
                intent.putExtra("email4", emailEt.getText().toString());
                intent3.putExtra("email4", emailEt.getText().toString());

                setResult(RESULT_OK,intent);
                setResult(RESULT_OK,intent3);
                finish();
                System.out.println("cancle finish() called");
                break;
            case R.id.confirmBtn:
                System.out.println("push confirmBtn");
                Intent intent1 = new Intent();
                intent1.putExtra("name4", nameEt.getText().toString());
                intent1.putExtra("pos4", posEt.getText().toString());
                intent1.putExtra("com4", comEt.getText().toString());
                intent1.putExtra("phone4", phoneEt.getText().toString());
                intent1.putExtra("fax4", faxEt.getText().toString());
                System.out.println("edit fax4:" + faxEt.getText().toString());
                intent1.putExtra("email4", emailEt.getText().toString());

                setResult(RESULT_OK,intent1);
                System.out.println("startActivityForResult");
                finish();
                System.out.println("finish() called");
                break;
        }
    }
}
