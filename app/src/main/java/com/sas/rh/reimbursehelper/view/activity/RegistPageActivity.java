package com.sas.rh.reimbursehelper.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.newactivity.LoginOrRegisterActivity;

public class RegistPageActivity extends AppCompatActivity {

    LinearLayout startreimburse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_page);
        startreimburse = (LinearLayout)findViewById(R.id.startreimburse);
        startreimburse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Intent it = new Intent(RegistPageActivity.this,BeforeRegisterActivity.class);
            //Intent it = new Intent(RegistPageActivity.this,RegFirstStepActivity.class);
              Intent it = new Intent(RegistPageActivity.this,LoginOrRegisterActivity.class);
                startActivity(it);
                finish();
            }
        });
    }
}
