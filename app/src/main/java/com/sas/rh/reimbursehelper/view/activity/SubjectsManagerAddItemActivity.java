package com.sas.rh.reimbursehelper.view.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sas.rh.reimbursehelper.NetUtil.XfkmUtils;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtil;
import com.sas.rh.reimbursehelper.Util.ToastUtil;

import net.sf.json.JSONObject;

public class SubjectsManagerAddItemActivity extends AppCompatActivity {

    private ImageView backbt;
    private EditText sname;
    private LinearLayout savebt;
    private JSONObject jsonresult;
    private String subjectname;
    private ProgressDialogUtil pdu =new ProgressDialogUtil(SubjectsManagerAddItemActivity.this,"上传提示","正在提交中");
    private Handler kemuxinxiback = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            pdu.dismisspd();
            if(msg.what == 1){
//                    System.out.println("ResultCode:" + jsonresult.get("ResultCode") + "\t" + "HostTime:"
//            + jsonresult.get("HostTime") + "\t" + "Note:" + jsonresult.get("Note"));
                ToastUtil.showToast(SubjectsManagerAddItemActivity.this,jsonresult.get("HostTime")+":"+jsonresult.get("Note").toString(), Toast.LENGTH_LONG);
                if(jsonresult.get("ResultCode").toString().trim().equals("00")){
                    finish();
                }
            }else if(msg.what == 0){
                ToastUtil.showToast(SubjectsManagerAddItemActivity.this,"通信异常，请检查网络连接！", Toast.LENGTH_LONG);
            }else if(msg.what == -1){
                ToastUtil.showToast(SubjectsManagerAddItemActivity.this,"通信模块异常！", Toast.LENGTH_LONG);
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects_manager_add_item);
        backbt = (ImageView)findViewById(R.id.backbt);
        sname = (EditText)findViewById(R.id.sname) ;
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
                CreateSubjectInfo();
            }
        });
    }

    private void CreateSubjectInfo(){
        if(sname.getText().toString().trim().equals("")){
            ToastUtil.showToast(SubjectsManagerAddItemActivity.this,"请填科目名称", Toast.LENGTH_LONG);
            return;
        }
        subjectname = sname.getText().toString().trim();
        pdu.showpd();
        new Thread(sendCreateSubjectInfoThread).start();
    }

    Runnable sendCreateSubjectInfoThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try{
                JSONObject jo = new XfkmUtils().insert(1,2,subjectname);
                if(jo != null){
                    jsonresult = jo;
                    kemuxinxiback.sendEmptyMessage(1);
                }else{
                    kemuxinxiback.sendEmptyMessage(0);
                }
            }catch(Exception e){
                kemuxinxiback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };
}
