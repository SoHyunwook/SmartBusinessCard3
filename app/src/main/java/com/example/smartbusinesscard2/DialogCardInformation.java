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
        nameTv.setText(name);

        conameTv = (TextView)findViewById(R.id.comNameTextView);
        String comname = intent.getStringExtra("comname");
        conameTv.setText(comname);

        emailTv = (TextView)findViewById(R.id.emailTextView);
        String email = intent.getStringExtra("email");
        emailTv.setText(email);

        telTv = (TextView)findViewById(R.id.phoneTextView);
        String tel = intent.getStringExtra("tel");
        telTv.setText(tel);

        faxTv = (TextView)findViewById(R.id.faxTextView);
        String fax = intent.getStringExtra("fax");
        faxTv.setText(fax);

        posTv = (TextView)findViewById(R.id.positionTextView);
        String pos = intent.getStringExtra("position");
        posTv.setText(pos);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.informEditBtn:

                break;
            case R.id.saveBtn:

                break;
        }
    }
}
