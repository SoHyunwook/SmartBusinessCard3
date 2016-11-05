package com.example.smartbusinesscard2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by 현욱 on 2016-11-03.
 */
public class DialogCardInformation extends Activity implements View.OnClickListener {
    TextView nameTv, conameTv, emailTv, telTv, faxTv, posTv;
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
        System.out.println("intent email:" + email);
        emailTv.setText(String.format("%s", email));

        telTv = (TextView)findViewById(R.id.phoneTextView);
        String tel = intent.getStringExtra("tel1");
        System.out.println("intent tel:" + tel);
        telTv.setText(String.format("%s", tel));

        faxTv = (TextView)findViewById(R.id.faxTextView);
        String fax = intent.getStringExtra("fax1");
        System.out.println("intent fax:" + fax);
        faxTv.setText(String.format("%s", fax));

        posTv = (TextView)findViewById(R.id.positionTextView);
        String pos = intent.getStringExtra("position");
        posTv.setText(String.format("%s", pos));

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
                Intent intent2 = getIntent();
                nameTv.setText(String.format("%s", intent2.getStringExtra("name4")));
                conameTv.setText(String.format("%s", intent2.getStringExtra("com4")));
                emailTv.setText(String.format("%s", intent2.getStringExtra("email4")));
                telTv.setText(String.format("%s", intent2.getStringExtra("phone4")));
                faxTv.setText(String.format("%s", intent2.getStringExtra("fax4")));
                System.out.println("intent.getStringExtra(fax4):" + intent2.getStringExtra("fax4"));
                posTv.setText(String.format("%s", intent2.getStringExtra("pos4")));
            }
        }
    }
}
