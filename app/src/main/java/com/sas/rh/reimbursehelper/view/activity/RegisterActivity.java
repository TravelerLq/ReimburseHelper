package com.sas.rh.reimbursehelper.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.Bean.SaveUserBean;
import com.sas.rh.reimbursehelper.Bean.UserBean;
import com.sas.rh.reimbursehelper.CertUtil.CertServiceUrl;
import com.sas.rh.reimbursehelper.NetworkUtil.UserUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtil;
import com.sas.rh.reimbursehelper.Util.ToastUtil;
import com.sas.rh.reimbursehelper.data.UserData;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.logging.LogRecord;

import cn.com.syan.jcee.common.sdk.utils.CertificateConverter;
import cn.com.syan.online.sdk.OnlineApplyResponse;
import cn.com.syan.online.sdk.OnlineClient;
import cn.com.syan.online.sdk.OnlineIssueResponse;
import cn.unitid.spark.cm.sdk.business.CBSCertificateStore;
import cn.unitid.spark.cm.sdk.business.CertificateIssueService;
import cn.unitid.spark.cm.sdk.business.CertificateRegisterService;
import cn.unitid.spark.cm.sdk.exception.CmSdkException;
import cn.unitid.spark.cm.sdk.listener.ProcessListener;

/**
 * Created by liqing on 18/4/9.
 */

public class RegisterActivity extends BaseActivity {
    private ImageView ivBack;
    private TextView tvTitle;
    private Button btnRegister;
    private EditText edtRealName;
    private EditText edtUserID, edtAccount, edtPsw, edtEmail;
    private EditText edtTel;
    private String realName;
    private String id;
    private String tel;
    private String vcode;
    private OnlineClient onlineClient;
    SharedPreferencesUtil sharedPreferencesUtil;
    private CBSCertificateStore store = null;
    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                //注册成功 去注册证书
//                UserBean userBean = n
                int status = jsonresult.getIntValue("status");
                if (status == 200) {
                    //用户信息添加成功 去注册证书
                    int userId = jsonresult.getInteger("userId");
                    sharedPreferencesUtil.writeUserId(String.valueOf(userId));
                    UserData.saveUser(new SaveUserBean(realName, id, tel));
                    pdu.showpd();
                    goRegisterCertify();
//                    toActivity(RegisterActivity.this, MainActivity.class);
//                    finish();


                } else {
                    //添加失败，说明数据库已经存在该用户，则去登陆页登录
                    ToastUtil.showToast(RegisterActivity.this, "您已注册，请直接登录！", Toast.LENGTH_LONG);

                    deleteCertify();
                    Intent intent = new Intent(RegisterActivity.this, RegFirstStepActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }
    };

    private void deleteCertify() {
        if (store != null) {
            if (store.getAllCertificateList().size() > 0) {
                //
                store.deleteCertificate(store.getAllCertificateList().get(0).getId());
                Log.e("certificate--size=", "" + store.getAllCertificateList().size());

            }
        }


    }

    private UserBean userBean;
    private String account;
    private String psw;
    private String emailStr;
    private ProgressDialogUtil pdu = new ProgressDialogUtil(RegisterActivity.this, "提示", "正在加载中");


    private void goRegisterCertify() {

        CertificateRegisterService certificateRegisterService = new CertificateRegisterService(RegisterActivity.this,
                onlineClient, new ProcessListener<OnlineApplyResponse>() {
            @Override
            public void doFinish(OnlineApplyResponse onlineApplyResponse, String s) {


//                        issueId.setText(onlineApplyResponse.getApplyId());
//                        subject.setText(onlineApplyResponse.getSubject());
//                        algorithm.setText(onlineApplyResponse.getAlgorithm());
//                        extentionText.setText(onlineApplyResponse.getExtensions());
                //接口注册成功后执行
                CertificateIssueService certificateIssueService = new CertificateIssueService(RegisterActivity.this, onlineClient, new ProcessListener<OnlineIssueResponse>() {
                    @Override
                    public void doFinish(OnlineIssueResponse data, String cert) {
                        if (pdu.getMypDialog().isShowing()) {
                            pdu.dismisspd();
                        }
                        Log.e("CertificateIssueService", "onFinish");

                        String your_signature = data.getSignCert();
                        String your_encryption = data.getEncCert();

                        X509Certificate x509Certificate = null;
                        try {
                            x509Certificate = CertificateConverter.fromBase64(your_signature);

                        } catch (CertificateException e) {
                            e.printStackTrace();
                        }

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String validate = formatter.format(x509Certificate.getNotBefore()) + " - " +
                                formatter.format(x509Certificate.getNotAfter());

//                        goToAddUser();
                        //注册成功，去加入公司 or注册公司

                        toActivity(RegisterActivity.this, JoinOrRegisterCompanyActivity.class);

                    }

                    @Override
                    public void doException(CmSdkException exception) {
                        if (pdu.getMypDialog().isShowing()) {
                            pdu.dismisspd();
                        }
                        Log.e("CertificateIssueService", "doException");
                        Toast.makeText(RegisterActivity.this, "" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("firstRegister", "showException" + exception.getMessage());
                    }
                });
                String exts = onlineApplyResponse.getExtensions();
                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                Map<String, String> retMap = gson.fromJson(exts, new TypeToken<Map<String, String>>() {
                }.getType());

                certificateIssueService.issue(onlineApplyResponse.getApplyId(), onlineApplyResponse.getSubject(), retMap, onlineApplyResponse.getAlgorithm(), true);

            }

            @Override
            public void doException(CmSdkException e) {
                if (pdu.getMypDialog().isShowing()) {
                    pdu.dismisspd();
                }
                ToastUtil.showToast(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT);
                Log.e("RegisterActivity", "CmSdkException" + e.getMessage());
            }
        });
        try {

            certificateRegisterService.register(realName, id, tel, "1234", null);
        } catch (Exception e) {
            ToastUtil.showToast(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT);
            e.printStackTrace();
        }

    }

    private void saveAndMain() {

    }

    private void saveAndLogin() {
        Intent intent = new Intent(RegisterActivity.this, RegFirstStepActivity.class);
        UserData.saveUser(new SaveUserBean(realName, tel, id));
        finish();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initData() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_bar_title);
        btnRegister = (Button) findViewById(R.id.btn_register);
        edtRealName = (EditText) findViewById(R.id.edt_real_name);
        edtUserID = (EditText) findViewById(R.id.edt_userID);
        edtTel = (EditText) findViewById(R.id.edt_tel);

        edtAccount = (EditText) findViewById(R.id.edt_acount);
        edtPsw = (EditText) findViewById(R.id.edt_tel);
        edtEmail = (EditText) findViewById(R.id.edt_psw);

        onlineClient = new OnlineClient(CertServiceUrl.baseUrl, CertServiceUrl.appKey, CertServiceUrl.appSecret);
        sharedPreferencesUtil = new SharedPreferencesUtil(RegisterActivity.this);
        store = CBSCertificateStore.getInstance();
        if (store.getAllCertificateList().size() > 0) {
            //
            store.deleteCertificate(store.getAllCertificateList().get(0).getId());
            Log.e("certificate--size=", "" + store.getAllCertificateList().size());

        }

     //  initTestData();


    }


    @Override
    protected void initListeners() {
        ivBack.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                // intTestData();
                checkData();
                goToAddUser();

                //  goRegisterCertify();
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }

    }

    private void initTestData() {
//        edtAccount.setText("12");
//        edtPsw.setText("12");
//        edtEmail.setText("245633");
//        edtRealName.setText("李青");
//        edtUserID.setText("320322199007171428");
//        edtTel.setText("15951882547");


        edtAccount.setText("12");
        edtPsw.setText("12");
        edtEmail.setText("245633");
        edtRealName.setText("王朕");
        edtUserID.setText("320113199407266410");
        edtTel.setText("18205188981");

    }

    private void goToAddUser() {
        new Thread(RunnableAddUser).start();

    }

    private JSONObject jsonresult;
    Runnable RunnableAddUser = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject jsonObject = UserUtil.addUser(tel, realName, emailStr, id, account, psw);
                if (jsonObject != null) {
                    jsonresult = jsonObject;
                    myHandler.sendEmptyMessage(1);
                } else {
                    myHandler.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                myHandler.sendEmptyMessage(-1);
                e.printStackTrace();
            }


        }
    };

    private void checkData() {


        account = edtAccount.getText().toString().trim();
        psw = edtPsw.getText().toString().trim();


        realName = edtRealName.getText().toString().trim();
        id = edtUserID.getText().toString();
        tel = edtTel.getText().toString();

        if (TextUtils.isEmpty(account)) {
            ToastUtil.showToast(RegisterActivity.this, "请输入账号", Toast.LENGTH_SHORT);
            edtAccount.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(psw)) {
            ToastUtil.showToast(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT);
            edtPsw.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(realName)) {
            ToastUtil.showToast(RegisterActivity.this, "请输入姓名", Toast.LENGTH_SHORT);
            edtRealName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(id)) {
            ToastUtil.showToast(RegisterActivity.this, "请输入身份证号", Toast.LENGTH_SHORT);
            edtUserID.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(tel)) {
            ToastUtil.showToast(RegisterActivity.this, "请输入手机号码", Toast.LENGTH_SHORT);
            edtTel.requestFocus();
            return;
        }
        vcode = "1234";
//                if ("".equals(vcode)) {
//                    ToastUtil.showToast(RegFirstStepActivity.this, "请输入验证码", Toast.LENGTH_SHORT);
//                    yzmet.requestFocus();
//                    return;
//                }

        emailStr = edtEmail.getText().toString();
        if (TextUtils.isEmpty(emailStr)) {
            ToastUtil.showToast(RegisterActivity.this, "请输入邮箱", Toast.LENGTH_SHORT);
            edtEmail.requestFocus();
            return;
        }


    }
}
