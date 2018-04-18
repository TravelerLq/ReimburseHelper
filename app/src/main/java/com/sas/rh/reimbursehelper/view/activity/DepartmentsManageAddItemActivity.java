package com.sas.rh.reimbursehelper.view.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.DepartmentUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtil;
import com.sas.rh.reimbursehelper.Util.TimePickerUtils;
import com.sas.rh.reimbursehelper.Util.ToastUtil;
import com.sas.rh.reimbursehelper.widget.CircleImageView;

import java.util.Arrays;
import java.util.List;

public class DepartmentsManageAddItemActivity extends AppCompatActivity {

    private ImageView addDM,backbt;
    private CircleImageView master_head;
    private EditText dname,dlimit;
    private TextView master_name;
    private TextView tvAuthority;
    private LinearLayout savebt,llExpenseAuthority;
    private SharedPreferencesUtil spu;
    private JSONObject jsonresult;
    private String dpname;//部门名称
    private String dplimit;//报销限额
    private String dmaster_id = "";//部门
    private ProgressDialogUtil pdu =new ProgressDialogUtil(DepartmentsManageAddItemActivity.this,"上传提示","正在提交中");
    private Handler bumenxinxiback = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            pdu.dismisspd();
            if(msg.what == 1){
//                    System.out.println("ResultCode:" + jsonresult.get("ResultCode") + "\t" + "HostTime:"
//            + jsonresult.get("HostTime") + "\t" + "Note:" + jsonresult.get("Note"));
                ToastUtil.showToast(DepartmentsManageAddItemActivity.this,"加载完毕", Toast.LENGTH_LONG);
                if(jsonresult != null){
                    finish();
                }
            }else if(msg.what == 0){
                ToastUtil.showToast(DepartmentsManageAddItemActivity.this,"通信异常，请检查网络连接！", Toast.LENGTH_LONG);
            }else if(msg.what == -1){
                ToastUtil.showToast(DepartmentsManageAddItemActivity.this,"通信模块异常！", Toast.LENGTH_LONG);
            }
        }

    };
    private String tvAuthorityStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departments_manage_add_item);
        tvAuthority=(TextView)findViewById(R.id.tv_expense_authority) ;
        addDM = (ImageView)findViewById(R.id.addDM);
        backbt= (ImageView)findViewById(R.id.backbt);
        dname = (EditText)findViewById(R.id.dname);
        dlimit = (EditText)findViewById(R.id.dlimit);
        master_head = (CircleImageView)findViewById(R.id.master_head);
        master_name = (TextView)findViewById(R.id.master_name);
        savebt = (LinearLayout) findViewById(R.id.savebt);
        llExpenseAuthority=(LinearLayout) findViewById(R.id.ll_expense_authority);


        tvAuthority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Resources res = getResources();
                String[] status = res.getStringArray(R.array.approval_no);
                List<String> allStatus = Arrays.asList(status);
                TimePickerUtils.getInstance().onListDataPicker(DepartmentsManageAddItemActivity.this, allStatus, tvAuthority);
            }
        });

        spu = new SharedPreferencesUtil(DepartmentsManageAddItemActivity.this);

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

        savebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateDepartmentInfo();
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
        dmaster_id = sid;
        master_name.setText(sn+"("+sid+")");
    }


    private void CreateDepartmentInfo(){
        if(dname.getText().toString().trim().equals("")){
            ToastUtil.showToast(DepartmentsManageAddItemActivity.this,"请部门目名称", Toast.LENGTH_LONG);
            return;
        }
        if(dlimit.getText().toString().trim().equals("")){
            ToastUtil.showToast(DepartmentsManageAddItemActivity.this,"请填报销限额", Toast.LENGTH_LONG);
            return;
        }
        if(dmaster_id  == null || dmaster_id.trim().equals("")){
            ToastUtil.showToast(DepartmentsManageAddItemActivity.this,"请选择部门负责人", Toast.LENGTH_LONG);
            return;
        }
        if(dmaster_id  == null || tvAuthority.getText().toString().trim().equals("")){

            ToastUtil.showToast(DepartmentsManageAddItemActivity.this,"报销级别不可以为空", Toast.LENGTH_LONG);
            return;
        }
        tvAuthorityStr = tvAuthority.getText().toString().trim();
        dpname = dname.getText().toString().trim();
        dplimit = dlimit.getText().toString().trim();
        pdu.showpd();
        new Thread(sendCreateDepartmentInfoThread).start();
    }

    Runnable sendCreateDepartmentInfoThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try{
                JSONObject jo = new DepartmentUtil().addDepartment(dpname,  Byte.valueOf(tvAuthorityStr),Double.parseDouble(dplimit),spu.getUidNum());
                if(jo != null){
                    jsonresult = jo;
                    bumenxinxiback.sendEmptyMessage(1);
                }else{
                    bumenxinxiback.sendEmptyMessage(0);
                }
            }catch(Exception e){
                bumenxinxiback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };
}
