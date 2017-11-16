package com.sas.rh.reimbursehelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PersonalDetailActivity extends AppCompatActivity {

    private ImageView backbt;
    private LinearLayout persondetailbt,helpbt,feedback,checkupdatebt,aboutusbt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_detail);
        backbt = (ImageView)findViewById(R.id.backbt);
        persondetailbt = (LinearLayout)findViewById(R.id.persondetailbt);
        helpbt = (LinearLayout)findViewById(R.id.helpbt) ;
        feedback = (LinearLayout)findViewById(R.id.feedback) ;
        checkupdatebt = (LinearLayout)findViewById(R.id.checkupdatebt) ;
        aboutusbt = (LinearLayout)findViewById(R.id.aboutusbt);

        backbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        persondetailbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        helpbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        checkupdatebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        aboutusbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



    }
}
