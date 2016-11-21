package com.example.smartbusinesscard2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by yi_te on 2016-11-16.
 */
public class PrintEmail extends AppCompatActivity implements View.OnClickListener {
    TextView sub1, msg1, whom1, s_time1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_print);

        findViewById(R.id.edit_b).setOnClickListener(this);
        findViewById(R.id.ok_b).setOnClickListener(this);

        Intent intent = getIntent();

        sub1 = (TextView) findViewById(R.id.subject2);
        String subject = intent.getStringExtra("sbj");
        sub1.setText(String.format("%s", subject));

        msg1 = (TextView) findViewById(R.id.message2);
        String message = intent.getStringExtra("msg");
        msg1.setText(String.format("%s", message));

        whom1 = (TextView) findViewById(R.id.email2);
        String email = intent.getStringExtra("whom");
        whom1.setText(String.format("%s", email));

        s_time1 = (TextView) findViewById(R.id.time2);
        String time2 = intent.getStringExtra("s_time");
        s_time1.setText(String.format("%s", time2));
    }

    public void onClick(View v) {
        Intent i = getIntent();
        switch (v.getId()) {
            case R.id.edit_b:
                Intent intent1 = new Intent(PrintEmail.this, EditEmail.class);
                intent1.putExtra("sub2", sub1.getText());
                intent1.putExtra("msg2", msg1.getText());
                intent1.putExtra("whom2", whom1.getText());
                intent1.putExtra("time2", s_time1.getText());
                startActivityForResult(intent1, 1);
                break;
            case R.id.ok_b:
                finish();
                break;
        }
    }
}