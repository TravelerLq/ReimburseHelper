package com.sas.rh.reimbursehelper.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.Bean.SaveUserBean;
import com.sas.rh.reimbursehelper.Bean.UserBean;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.data.UserData;

import java.util.ArrayList;

import cn.unitid.spark.cm.sdk.business.CBSCertificateStore;
import cn.unitid.spark.cm.sdk.data.entity.Certificate;

public class PersonalDetailActivity extends AppCompatActivity {

    private ImageView backbt;
    private LinearLayout persondetailbt, helpbt, feedback, checkupdatebt, aboutusbt, ll_log_out;
    private TextView tvRealName;
    private TextView tvTel;
    private SaveUserBean saveUserBean;
    private SharedPreferencesUtil sharedPreferencesUtil;
    private ArrayList<Certificate> certificateArrayList;//证书列表;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_detail);
        tvRealName = (TextView) findViewById(R.id.tv_real_name);
        tvTel = (TextView) findViewById(R.id.tv_tel);
        sharedPreferencesUtil = new SharedPreferencesUtil(PersonalDetailActivity.this);

        ll_log_out = (LinearLayout) findViewById(R.id.ll_log_out);
        backbt = (ImageView) findViewById(R.id.backbt);
        persondetailbt = (LinearLayout) findViewById(R.id.persondetailbt);
        helpbt = (LinearLayout) findViewById(R.id.helpbt);
        feedback = (LinearLayout) findViewById(R.id.feedback);
        checkupdatebt = (LinearLayout) findViewById(R.id.checkupdatebt);
        aboutusbt = (LinearLayout) findViewById(R.id.aboutusbt);
        int userId = sharedPreferencesUtil.getUidNum();
        Loger.e("userId==" + userId);
        saveUserBean = UserData.getUserInfo();
        if (saveUserBean != null) {
            tvRealName.setText(UserData.getUserInfo().getName());
            tvTel.setText(UserData.getUserInfo().getUserPhone());
        }

        ll_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // finish();注销的话去登录页面 把保存的值清除掉  把证书、用户信息删掉
                CBSCertificateStore store = CBSCertificateStore.getInstance();
                //查询本地库证书列表,没注册则注册一张
                certificateArrayList = store.getAllCertificateList();
                if (certificateArrayList.size() > 0) {
                    Loger.e("certificateList---size()=" + certificateArrayList.size());
                    store.deleteCertificate(certificateArrayList.get(0).getId());
                    Loger.e("---getName" + certificateArrayList.get(0).getName());

                }

                if (saveUserBean != null) {
                    UserData.removeUser();
                }
                Intent intent = new Intent(PersonalDetailActivity.this, RegFirstStepActivity.class);
                startActivity(intent);
                finish();
            }
        });

        backbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
