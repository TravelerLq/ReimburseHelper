package com.sas.rh.reimbursehelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ReimburseBillCreateActivity extends AppCompatActivity {

    private ImageView backbt;
    private TextView selectprjbt,selectdpbt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reimburse_bill_create);
        backbt = (ImageView)findViewById(R.id.backbt) ;
        selectdpbt = (TextView)findViewById(R.id.selectdpbt) ;
        selectprjbt = (TextView)findViewById(R.id.selectprjbt);

        backbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        selectdpbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        selectprjbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
