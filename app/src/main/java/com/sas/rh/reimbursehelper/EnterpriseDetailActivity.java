package com.sas.rh.reimbursehelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class EnterpriseDetailActivity extends AppCompatActivity {
    private EditText cname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_detail);
    }
}
