package com.sas.rh.reimbursehelper.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.sas.rh.reimbursehelper.NetUtil.YuangongUtils;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.ToastUtil;

import net.sf.json.JSONObject;

public class MembersManageAddStaffActivity extends AppCompatActivity {

    ImageView backbt;
    private EditText telnum,sname;
    private Spinner dp_sp,qx_dp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_manage_add_staff);
        backbt = (ImageView)findViewById(R.id.backbt);
        telnum =  (EditText)findViewById(R.id.telnum);
        sname =  (EditText)findViewById(R.id.sname);
        dp_sp =  (Spinner)findViewById(R.id.dp_sp);
        qx_dp =  (Spinner)findViewById(R.id.qx_dp);


        backbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

//    private void CreateDepartmentInfo(){
//        if(telnum.getText().toString().trim().equals("")){
//            ToastUtil.showToast(MembersManageAddStaffActivity.this,"请填写员工名称", Toast.LENGTH_LONG);
//            return;
//        }
//        if(sname.getText().toString().trim().equals("")){
//            ToastUtil.showToast(MembersManageAddStaffActivity.this,"请填写员工名称", Toast.LENGTH_LONG);
//            return;
//        }
//
//        if(dmaster_id  == null || dmaster_id.trim().equals("")){
//            ToastUtil.showToast(MembersManageAddStaffActivity.this,"请选择部门负责人", Toast.LENGTH_LONG);
//            return;
//        }
//        dpname = dname.getText().toString().trim();
//        pdu.showpd();
//        new Thread(sendCreateMemberInfoThread).start();
//    }

//    Runnable sendCreateMemberInfoThread = new Runnable() {
//        @Override
//        public void run() {
//            // TODO Auto-generated method stub
//
//            try{
//                JSONObject jo = new YuangongUtils().insert(,);
//                if(jo != null){
//                    jsonresult = jo;
//                    bumenxinxiback.sendEmptyMessage(1);
//                }else{
//                    bumenxinxiback.sendEmptyMessage(0);
//                }
//            }catch(Exception e){
//                bumenxinxiback.sendEmptyMessage(-1);
//                e.printStackTrace();
//            }
//        }
//
//    };
}
