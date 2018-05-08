package com.sas.rh.reimbursehelper.newactivity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.UserUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.VerifyCertUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.view.activity.BaseActivity;
import com.sas.rh.reimbursehelper.view.activity.MainActivity;

import java.util.ArrayList;

import cn.unitid.spark.cm.sdk.business.CBSCertificateStore;
import cn.unitid.spark.cm.sdk.data.entity.Certificate;

/**
 * Created by liqing on 18/5/1.
 */

public class NewLoginActivity extends BaseActivity {
    private TextView tvTilte, tvLogin, tvForgetPsw;
    private ImageView ivBack;
    private EditText edtAccount, edtPsw;
    private Context context;
    private SharedPreferencesUtil spu;
    private int userId;
    private JSONObject jsonresult;
    private ArrayList<Certificate> certificateArrayList;//证书列表;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                int status = jsonresult.getIntValue("status");
                if (status == 200) {
                    Toast.makeText(context, "登录成功！", Toast.LENGTH_SHORT).show();
                    int role = jsonresult.getIntValue("roleId");
                    spu.setName(jsonresult.getString("name"));
                    spu.setTel(jsonresult.getString("telephoneNumber"));
                    String roleStr = String.valueOf(role);
                    spu.setRole(roleStr);
                    Loger.e("--login-suce -role ==" + spu.getRole());

                    toActivityWithData(context, MainActivity.class, "data", roleStr);

                } else {
                    Toast.makeText(context, "登录失败，请重试！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    private String tel;
    private String idNo;
    private String psw;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_login;
    }

    @Override
    protected void initData() {
        context = NewLoginActivity.this;
        spu = new SharedPreferencesUtil(NewLoginActivity.this);
        tvTilte = (TextView) findViewById(R.id.tv_bar_title);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        edtAccount = (EditText) findViewById(R.id.edt_account);
        edtPsw = (EditText) findViewById(R.id.edt_psw);
        tvForgetPsw = (TextView) findViewById(R.id.tv_forget_psw);
        tvLogin = (TextView) findViewById(R.id.tv_login);
        tvTilte.setText("登录");
        if (getIntent() != null) {
            //    tel = getIntent().getStringExtra("tel");
            idNo = getIntent().getStringExtra("id");
        }
        tel = spu.getTel();
        userId = spu.getUidNum();

        CBSCertificateStore store = CBSCertificateStore.getInstance();
        //查询本地库证书列表,没注册则注册一张
        certificateArrayList = store.getAllCertificateList();
        if (certificateArrayList.size() == 0) {
            toActivity(context, RegCertActivity.class);

        }

    }

    @Override
    protected void initListeners() {
        tvLogin.setOnClickListener(this);
        tvForgetPsw.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_login:
                checkdata();
                break;
            case R.id.tv_forget_psw:
                // toActivity(context,);
                break;
            default:
                break;
        }
    }


    //注册
    private void checkdata() {
        String account = edtAccount.getText().toString().trim();
        psw = edtPsw.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            Toast.makeText(this, "手机号不可为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!tel.equals(account)) {
            Toast.makeText(this, "手机号不正确，请重新填写！", Toast.LENGTH_SHORT).show();
            edtAccount.setText("");
            return;
        }
        if (TextUtils.isEmpty(psw)) {
            Toast.makeText(this, "密码不可为空", Toast.LENGTH_SHORT).show();
            return;
        }

        goLogin();
    }

    private void goLogin() {
        new Thread(LoginRunnable).start();
    }

    private String certStr;
    Runnable LoginRunnable = new Runnable() {
        @Override
        public void run() {

            try {
                JSONObject jsonObject = UserUtil.login(userId, psw);
                if (jsonObject != null) {
                    jsonresult = jsonObject;
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

}
