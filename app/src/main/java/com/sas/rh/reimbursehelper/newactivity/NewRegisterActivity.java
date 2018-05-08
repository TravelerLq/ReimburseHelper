package com.sas.rh.reimbursehelper.newactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.SingleReimbursementUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.UserUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.VerifyCertUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.view.activity.BaseActivity;
import com.sas.rh.reimbursehelper.view.activity.EnterpriseDetailActivity;
import com.sas.rh.reimbursehelper.view.activity.MainActivity;


/**
 * Created by liqing on 18/5/1.
 */

public class NewRegisterActivity extends BaseActivity {
    private TextView tvTilte, tvRegister;
    private ImageView ivBack;
    private EditText edtAccount, edtPsw;
    private SharedPreferencesUtil spu;
    private Context context;
    private int userId;
    private JSONObject jsonresult;
    private String account;
    private String psw;
    private String tel;
    private String idNo;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                int status = jsonresult.getIntValue("status");
                if (status == 200) {
                    String roleType = spu.getRoleType();
                    Loger.e("---getRoleType" + roleType);
                    if (roleType != null && roleType.equals("0")) {
                        //选择公司进入的，注册成功后－去注册公司 －登录
                        Intent intent = new Intent(context, EnterpriseDetailActivity.class);
                        // intent.putExtra("id", edtIdNoStr);
                        intent.putExtra("tel", tel);
                        startActivity(intent);
                        Toast.makeText(context, "注册成功，去登陆", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(context, NewLoginActivity.class);
                        // intent.putExtra("id", edtIdNoStr);
                        intent.putExtra("tel", tel);
                        startActivity(intent);
                        Toast.makeText(context, "注册成功，去登陆", Toast.LENGTH_SHORT).show();
                    }

                    //  toActivity(context, NewLoginActivity.class);
                } else {
                    // 证书注册时，已经知道是没有注册过的用户，所以，这里只有注册失败，则重新注册！
                    Toast.makeText(context, "注册失败，请重新注册！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_register;
    }

    @Override
    protected void initData() {
        context = NewRegisterActivity.this;
        spu = new SharedPreferencesUtil(context);
        Loger.e("---new Register--" + idNo);
        tvTilte = (TextView) findViewById(R.id.tv_bar_title);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        edtAccount = (EditText) findViewById(R.id.edt_account);
        edtPsw = (EditText) findViewById(R.id.edt_psw);
        tvRegister = (TextView) findViewById(R.id.tv_regist);
        tvTilte.setText("注册");
        //
//        intent.putExtra("id", edtIdNoStr);
//        intent.putExtra("tel", edtTelStr);
        if (getIntent() != null) {
            //  tel = getIntent().getStringExtra("tel");
            // idNo = getIntent().getStringExtra("id");
        }
        spu = new SharedPreferencesUtil(context);
        userId = spu.getUidNum();
        tel = spu.getTel();
        idNo = spu.getIdNo();

        Loger.e("register--userId" + userId + "tel--" + tel + "idNo--" + idNo);
    }

    @Override
    protected void initListeners() {
        ivBack.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_regist:
                Loger.e("---tvregister--");
                checkdata();
                break;
            default:
                break;
        }
    }

    //注册
    private void checkdata() {
        account = edtAccount.getText().toString().trim();
        psw = edtPsw.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            Toast.makeText(this, "手机号不可为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!tel.equals(account)) {
            Toast.makeText(this, "手机号和证书验证不一致，请重新填写！", Toast.LENGTH_SHORT).show();
            edtAccount.setText("");
            return;
        }

        if (TextUtils.isEmpty(psw)) {
            Toast.makeText(this, "密码不可为空", Toast.LENGTH_SHORT).show();
            return;
        }

        goRegister();
    }


    private void goRegister() {
        new Thread(RegistRunnable).start();
    }

    private String certStr;
    Runnable RegistRunnable = new Runnable() {
        @Override
        public void run() {

            try {
                JSONObject jsonObject = UserUtil.register(userId, psw, idNo);
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
