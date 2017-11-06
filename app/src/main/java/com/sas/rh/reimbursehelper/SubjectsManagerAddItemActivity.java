package com.sas.rh.reimbursehelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SubjectsManagerAddItemActivity extends AppCompatActivity {

    private ImageView backbt;
    private LinearLayout savebt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects_manager_add_item);
        backbt = (ImageView)findViewById(R.id.backbt);
        savebt = (LinearLayout)findViewById(R.id.savebt);

        backbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        savebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
