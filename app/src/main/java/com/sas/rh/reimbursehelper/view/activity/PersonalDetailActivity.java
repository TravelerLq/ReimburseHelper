package com.sas.rh.reimbursehelper.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.Bean.SaveUserBean;
import com.sas.rh.reimbursehelper.Bean.UserBean;
import com.sas.rh.reimbursehelper.NetworkUtil.CompanyUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.Util.ToastUtil;
import com.sas.rh.reimbursehelper.data.UserData;
import com.sas.rh.reimbursehelper.newactivity.LoginOrRegisterActivity;
import com.sas.rh.reimbursehelper.newactivity.NewLoginActivity;

import java.util.ArrayList;

import cn.unitid.spark.cm.sdk.business.CBSCertificateStore;
import cn.unitid.spark.cm.sdk.data.entity.Certificate;

public class PersonalDetailActivity extends BaseActivity {

    private ImageView backbt;
    private LinearLayout persondetailbt, helpbt, feedback, checkupdatebt, aboutusbt, ll_log_out;
    private TextView tvRealName;
    private TextView tvTel;
    private SaveUserBean saveUserBean;
    private SharedPreferencesUtil sharedPreferencesUtil;
    private ArrayList<Certificate> certificateArrayList;//证书列表;
    private int userId;
    private TextView tvCodeContent;
    private LinearLayout linearLayout;
    private TextView tvGetCode;
    private String role;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {

                shareCode = jsonObjectCode.getString("shareCode");
                tvCodeContent.setText(shareCode);

            } else if (msg.what == 0) {
                ToastUtil.showToast(PersonalDetailActivity.this, "通信异常，请检查网络连接！", Toast.LENGTH_LONG);
            } else if (msg.what == -1) {
                ToastUtil.showToast(PersonalDetailActivity.this, "通信模块异常！", Toast.LENGTH_LONG);
            }
        }
    };
    private JSONObject jsonObjectCode;
    private String shareCode;

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
        tvCodeContent = (TextView) findViewById(R.id.tv_get_code_content);
        linearLayout = (LinearLayout) findViewById(R.id.ll_get_code);
        tvGetCode = (TextView) findViewById(R.id.tv_get_code);

        tvGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCompanyCode();
            }
        });

        userId = sharedPreferencesUtil.getUidNum();
        //2 公司领导层 3.部门主管 role
        role = sharedPreferencesUtil.getRole();
        if (role.equals("3")) {
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.GONE);
        }

        Loger.e("userId==" + userId);
//        saveUserBean = UserData.getUserInfo();
//        if (saveUserBean != null) {
//            tvRealName.setText(UserData.getUserInfo().getName());
//            tvTel.setText(UserData.getUserInfo().getUserPhone());
//        }
        tvRealName.setText(sharedPreferencesUtil.getName());
        tvTel.setText(sharedPreferencesUtil.getTel());

        ll_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                warningDialog("确定注销账号？");
                // finish();注销的话去登录页面 把保存的值清除掉  把证书、用户信息删掉
                //  clearData();

            }
        });

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

    private void clearData() {
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
        sharedPreferencesUtil.writeCompanyId("");
        sharedPreferencesUtil.writeUserId("");
        sharedPreferencesUtil.setIdNo("");

        Intent intent = new Intent(PersonalDetailActivity.this, LoginOrRegisterActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_detail;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListeners() {

    }

    private void getCompanyCode() {
        new Thread(RunnableGetCompanyCode).start();

    }


    Runnable RunnableGetCompanyCode = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject jsonObject = CompanyUtil.getShareCode(userId);
                if (jsonObject != null) {
                    jsonObjectCode = jsonObject;
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                handler.sendEmptyMessage(-1);
                e.printStackTrace();
            }


        }
    };


    @Override
    public void onClick(View view) {

    }


    /**
     * 提示对话框
     *
     * @param message
     */
    protected void warningDialog(String message) {
        new AlertDialog.Builder(PersonalDetailActivity.this)
                .setTitle(getResources().getString(R.string.notice))
                .setMessage(message)
                .setPositiveButton(getResources().getString(R.string.sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //清空之前扫描的料单数据
                        //  VsdApplication.getInstance().getWaitStoreMaterialBeanList().clear();
                        clearData();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create().show();
    }
}
