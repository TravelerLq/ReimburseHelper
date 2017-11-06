package com.sas.rh.reimbursehelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sas.rh.reimbursehelper.View.CircleImageView;

public class DepartmentsManageAddItemActivity extends AppCompatActivity {

    private ImageView addDM,backbt;
    private CircleImageView master_head;
    private TextView master_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departments_manage_add_item);

        addDM = (ImageView)findViewById(R.id.addDM);
        backbt= (ImageView)findViewById(R.id.backbt);
        master_head = (CircleImageView)findViewById(R.id.master_head);
        master_name = (TextView)findViewById(R.id.master_name);

        addDM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it =new Intent(DepartmentsManageAddItemActivity.this,DepartmentsManageAddMasterActivity.class);
                startActivityForResult(it,0);
            }
        });

        backbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String sn = data.getStringExtra("slectedname");
        String sp = data.getStringExtra("slectedpath");
        String sid = data.getStringExtra("slectedstaffid");
        System.out.println("aaaaaaaaaa:"+requestCode+":"+sn+":"+sp+":"+sid);
        master_head.setImageResource(Integer.parseInt(sp));
        master_name.setText(sn+"("+sid+")");
    }
}
