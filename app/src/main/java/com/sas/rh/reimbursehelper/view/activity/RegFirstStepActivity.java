package com.sas.rh.reimbursehelper.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.Util.ToastUtil;
import com.sas.rh.reimbursehelper.data.UserData;
import com.sas.rh.reimbursehelper.service.NoticeMsgService;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Map;

import cn.com.syan.jcee.common.sdk.utils.CertificateConverter;
import cn.com.syan.online.sdk.OnlineApplyResponse;
import cn.com.syan.online.sdk.OnlineClient;
import cn.com.syan.online.sdk.OnlineIssueResponse;
import cn.com.syan.online.sdk.OnlineResponse;
import cn.unitid.spark.cm.sdk.business.CBSCertificateStore;
import cn.unitid.spark.cm.sdk.business.CertificateIssueService;
import cn.unitid.spark.cm.sdk.business.CertificateRegisterService;
import cn.unitid.spark.cm.sdk.business.VCodeGetService;
import cn.unitid.spark.cm.sdk.exception.CmSdkException;
import cn.unitid.spark.cm.sdk.listener.ProcessListener;

public class RegFirstStepActivity extends AppCompatActivity {

    private SharedPreferencesUtil sharedPreferencesUtil;
    private EditText realnameet, idnumet, telnumet, yzmet;
    private Button getyzmbt;
    private LinearLayout nexttostep2;
    private Button btnLogin;
    private LinearLayout llRegister;
    private OnlineClient onlineClient;
    private EditText edtEmail;
    private EditText edtAccount;
    private EditText edtPassWord;
    private String realname;
    private String idno;
    private String telephone;
    private String emailStr;
    private JSONObject jsonresult;
    private CBSCertificateStore store = null;
    private Intent serviceIntenta;
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // super.handleMessage(msg);
            Loger.e("jsonRsult--" + jsonresult.toJSONString());
            if (msg.what == 1) {
                String userJson = jsonresult.getString("user");
                UserBean userBean = JSON.parseObject(userJson, UserBean.class);
                companyId = userBean.getCompanyId();
                sharedPreferencesUtil.writeCompanyId(String.valueOf(companyId));
                SaveUserBean saveUserBean = new SaveUserBean(userBean.getName(), userBean.getIdCardNumber(), userBean.getUserPhone());

                Loger.e("userBean" + userBean.getName());
                int status = jsonresult.getIntValue("status");

                Loger.e("status--" + status);
                if (status == 200) {
                    int userId = userBean.getUserId();
                    Loger.e("userId--" + userId);
                    UserData.saveUser(saveUserBean);

                    sharedPreferencesUtil.writeUserId(String.valueOf(userId));
                    serviceIntenta = new Intent(RegFirstStepActivity.this, NoticeMsgService.class);
                    startService(serviceIntenta);
//                    SaveUserBean saveUserBean = new SaveUserBean(account, psw, realname, emailStr, telephone, idno);
//                    UserData.saveUser(saveUserBean);
//                    SaveUserBean saveUserBean1 = UserData.getUserInfo();
                    // Loger.e("name" + saveUserBean1.getName() + "tel" + saveUserBean1.getUserPhone());
                    // saveData(account, psw, idno, telephone, emailStr, realname);
                    ToastUtil.showToast(RegFirstStepActivity.this, "成功！", Toast.LENGTH_LONG);
                    Intent it = new Intent(RegFirstStepActivity.this, MainActivity.class);
                    startActivity(it);
                    finish();

                } else {
                    String msgToast = jsonresult.getString("msg");
                    Loger.e("msgToast--" + msgToast);
                    // ToastUtil.showToast(RegFirstStepActivity.this, msgToast, Toast.LENGTH_LONG);
                }


            } else if (msg.what == 0) {
                ToastUtil.showToast(RegFirstStepActivity.this, "通信异常，请检查网络连接！", Toast.LENGTH_LONG);
            } else if (msg.what == -1) {
                ToastUtil.showToast(RegFirstStepActivity.this, "通信模块异常！", Toast.LENGTH_LONG);
            }
        }
    };
    private int companyId;

    private void saveData(String account, String psw, String id, String tel, String email, String realName) {

        /**
         * 保存用户信息
         */

        SharedPreferences userInfo = getSharedPreferences("save", MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();//获取Editor
        //得到Editor后，写入需要保存的数据
        editor.putString("account", account);
        editor.putString("psw", psw);
        editor.putString("id", id);
        editor.putString("tel", tel);
        editor.putString("email", email);
        editor.putString("realname", realName);
        editor.commit();
        Loger.e("---save-success");


    }

//    private void getUserInfo() {
//        SharedPreferences userInfo = getSharedPreferences("save", MODE_PRIVATE);
//        String account = userInfo.getString("account", null);//读取username
//        edtAccount.setText(account);
//        String psw = userInfo.getString("psw", null);//读取psw
//        edtPassWord.setText(psw);
//        String id = userInfo.getString("id", null);//读取username
//        idnumet.setText(id);
//        String tel = userInfo.getString("tel", null);//读取psw
//        telnumet.setText(tel);
//        String email = userInfo.getString("email", null);//读取psw
//        edtEmail.setText(email);
//        String realName = userInfo.getString("realname", null);
//        realnameet.setText(realName);
//
//        Loger.e("--" + "account:" + account + "， psw:" + psw + "id" + id + "tel:" + tel + "email:" + email);
//
//    }

    private String account;
    private String psw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   getUserInfo();

        sharedPreferencesUtil = new SharedPreferencesUtil(RegFirstStepActivity.this);
        setContentView(R.layout.activity_reg_first_step);
        AndPermission.with(this)
                .requestCode(100)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        AndPermission.rationaleDialog(RegFirstStepActivity.this, rationale).show();
                    }
                }).callback(this)
                .start();
        btnLogin = (Button) findViewById(R.id.btn_login);
        llRegister = (LinearLayout) findViewById(R.id.ll_register);
        edtPassWord = (EditText) findViewById(R.id.edt_psw);
        edtAccount = (EditText) findViewById(R.id.edt_acount);
        edtEmail = (EditText) findViewById(R.id.edt_emil);
        realnameet = (EditText) findViewById(R.id.realnameet);
        idnumet = (EditText) findViewById(R.id.idnumet);
        telnumet = (EditText) findViewById(R.id.telnumet);
        yzmet = (EditText) findViewById(R.id.yzmet);
        getyzmbt = (Button) findViewById(R.id.getyzmbt);
        nexttostep2 = (LinearLayout) findViewById(R.id.nexttostep2);
        onlineClient = new OnlineClient(CertServiceUrl.baseUrl, CertServiceUrl.appKey, CertServiceUrl.appSecret);

        final MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60000, 1000);
        //  initTestData();
        SaveUserBean saveUserBean = UserData.getUserInfo();
        if (saveUserBean != null) {
            edtAccount.setText(saveUserBean.getUserName());
            edtPassWord.setText(saveUserBean.getUserPwd());

            edtEmail.setText(saveUserBean.getUserMail());

            realnameet.setText(saveUserBean.getName());

            idnumet.setText(saveUserBean.getUserID());
            telnumet.setText(saveUserBean.getUserPhone());


        }
        //register
        llRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegFirstStepActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        //btnLogin
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //去登录
                checkData();
                login();
            }
        });

        //给Button设置点击时间,触发倒计时
        getyzmbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //开始计时

                String telephone = telnumet.getText().toString();
                if ("".equals(telephone)) {
                    ToastUtil.showToast(RegFirstStepActivity.this, "请输入手机号码", Toast.LENGTH_SHORT);
                    telnumet.requestFocus();
                    return;
                }
                String vcode = yzmet.getText().toString();
                //ToastUtil.showToast(RegFirstStepActivity.this, "请输入验证码", Toast.LENGTH_SHORT);
                yzmet.requestFocus();

                VCodeGetService vCodeGetService = new VCodeGetService(onlineClient, new ProcessListener<OnlineResponse>() {
                    @Override
                    public void doFinish(OnlineResponse onlineResponse, String s) {
                        if (onlineResponse.getRet() == 0) {
                            myCountDownTimer.start();
                            ToastUtil.showToast(RegFirstStepActivity.this, "短信已发送请注意查收", Toast.LENGTH_SHORT);
                        } else {
                            ToastUtil.showToast(RegFirstStepActivity.this, "短信发送失败", Toast.LENGTH_SHORT);
                        }

                    }

                    @Override
                    public void doException(CmSdkException e) {
                        e.printStackTrace();
                        ToastUtil.showToast(RegFirstStepActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT);
                    }
                });
                vCodeGetService.get(telephone);

                return;


            }
        });


    }

    private void checkData() {


//        account = edtAccount.getText().toString().trim();
//        psw = edtPassWord.getText().toString().trim();


        realname = realnameet.getText().toString().trim();
        idno = idnumet.getText().toString().trim();
//        if (TextUtils.isEmpty(account)) {
//            ToastUtil.showToast(RegFirstStepActivity.this, "请输入账号", Toast.LENGTH_SHORT);
//            edtPassWord.requestFocus();
//            return;
//        }
//
//        if (TextUtils.isEmpty(psw)) {
//            ToastUtil.showToast(RegFirstStepActivity.this, "请输入密码", Toast.LENGTH_SHORT);
//            edtAccount.requestFocus();
//            return;
//        }

        if (TextUtils.isEmpty(realname)) {
            ToastUtil.showToast(RegFirstStepActivity.this, "请输入姓名", Toast.LENGTH_SHORT);
            realnameet.requestFocus();
            return;
        }
        if ("".equals(idno)) {
            ToastUtil.showToast(RegFirstStepActivity.this, "请输入身份证号", Toast.LENGTH_SHORT);
            idnumet.requestFocus();
            return;
        }

        telephone = telnumet.getText().toString();
        if ("".equals(telephone)) {
            ToastUtil.showToast(RegFirstStepActivity.this, "请输入手机号码", Toast.LENGTH_SHORT);
            telnumet.requestFocus();
            return;
        }
        String vcode = yzmet.getText().toString();
        vcode = "1234";
//                if ("".equals(vcode)) {
//                    ToastUtil.showToast(RegFirstStepActivity.this, "请输入验证码", Toast.LENGTH_SHORT);
//                    yzmet.requestFocus();
//                    return;
//                }

//        emailStr = edtEmail.getText().toString();
//        if (TextUtils.isEmpty(emailStr)) {
//            ToastUtil.showToast(RegFirstStepActivity.this, "请输入邮箱", Toast.LENGTH_SHORT);
//            edtEmail.requestFocus();
//            return;
//        }


    }

    private void goLogin() {
        new Thread(RunnableLogin).start();
    }

    private void goAddUser() {
        new Thread(RunnableAddUser).start();
    }


    Runnable RunnableLogin = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject jsonObject = UserUtil.selectbyidcardnumber(idno);
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


    Runnable RunnableAddUser = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject jsonObject = UserUtil.addUser(telephone, realname, emailStr, idno, account, psw);
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

    public void login() {


        store = CBSCertificateStore.getInstance();
        if (store.getAllCertificateList().size() > 0) {
            // goAddUser();
            Log.e("certificate--size=", "" + store.getAllCertificateList().size());
            goLogin();

        } else {
            //去申请证书
            //无证书时候
            goRegisterCertify();
//            CertificateRegisterService certificateRegisterService = new CertificateRegisterService(RegFirstStepActivity.this,
//                    onlineClient, new ProcessListener<OnlineApplyResponse>() {
//                @Override
//                public void doFinish(OnlineApplyResponse onlineApplyResponse, String s) {
////                        issueId.setText(onlineApplyResponse.getApplyId());
////                        subject.setText(onlineApplyResponse.getSubject());
////                        algorithm.setText(onlineApplyResponse.getAlgorithm());
////                        extentionText.setText(onlineApplyResponse.getExtensions());
//                    //接口注册成功后执行
//                    CertificateIssueService certificateIssueService = new CertificateIssueService(RegFirstStepActivity.this, onlineClient, new ProcessListener<OnlineIssueResponse>() {
//                        @Override
//                        public void doFinish(OnlineIssueResponse data, String cert) {
//                            Log.e("CertificateIssueService", "onFinish");
//                            //  goAddUser();
//                            goLogin();
//                            String your_signature = data.getSignCert();
//                            String your_encryption = data.getEncCert();
//
//                            X509Certificate x509Certificate = null;
//                            try {
//                                x509Certificate = CertificateConverter.fromBase64(your_signature);
//
//                            } catch (CertificateException e) {
//                                e.printStackTrace();
//                            }
//
//                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//                            String validate = formatter.format(x509Certificate.getNotBefore()) + " - " + formatter.format(x509Certificate.getNotAfter());
//
//
//                            //certValidate.setText(validate);
////                                Intent it = new Intent(RegFirstStepActivity.this, MainActivity.class);
////                                //   Intent it = new Intent(RegFirstStepActivity.this, SignVerifyP1Activity.class);
////                                startActivity(it);
////                                finish();
//
//                        }
//
//                        @Override
//                        public void doException(CmSdkException exception) {
//                            Log.e("CertificateIssueService", "doException");
//                            Toast.makeText(RegFirstStepActivity.this, "" + exception.getMessage(), Toast.LENGTH_SHORT).show();
//                            Log.e("firstRegister", "showException" + exception.getMessage());
//                        }
//                    });
//                    String exts = onlineApplyResponse.getExtensions();
//                    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
//                    Map<String, String> retMap = gson.fromJson(exts, new TypeToken<Map<String, String>>() {
//                    }.getType());
//
//                    certificateIssueService.issue(onlineApplyResponse.getApplyId(), onlineApplyResponse.getSubject(), retMap, onlineApplyResponse.getAlgorithm(), true);
//
//                }
//
//                @Override
//                public void doException(CmSdkException e) {
//                    ToastUtil.showToast(RegFirstStepActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT);
//                    Log.e("firstRegister", "CmSdkException" + e.getMessage());
//                }
//            });
//            try {
//
//                certificateRegisterService.register(realname, idno, telephone, vcode, null);
//            } catch (Exception e) {
//                ToastUtil.showToast(RegFirstStepActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT);
//                e.printStackTrace();
//            }
//
//        }


        }
    }


    private void goRegisterCertify() {

        CertificateRegisterService certificateRegisterService = new CertificateRegisterService(RegFirstStepActivity.this,
                onlineClient, new ProcessListener<OnlineApplyResponse>() {
            @Override
            public void doFinish(OnlineApplyResponse onlineApplyResponse, String s) {
//                        issueId.setText(onlineApplyResponse.getApplyId());
//                        subject.setText(onlineApplyResponse.getSubject());
//                        algorithm.setText(onlineApplyResponse.getAlgorithm());
//                        extentionText.setText(onlineApplyResponse.getExtensions());
                //接口注册成功后执行
                CertificateIssueService certificateIssueService = new CertificateIssueService(RegFirstStepActivity.this, onlineClient, new ProcessListener<OnlineIssueResponse>() {
                    @Override
                    public void doFinish(OnlineIssueResponse data, String cert) {
                        Log.e("CertificateIssueService", "onFinish");
                        //  goAddUser();
                        goLogin();
                        String your_signature = data.getSignCert();
                        String your_encryption = data.getEncCert();

                        X509Certificate x509Certificate = null;
                        try {
                            x509Certificate = CertificateConverter.fromBase64(your_signature);

                        } catch (CertificateException e) {
                            e.printStackTrace();
                        }

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String validate = formatter.format(x509Certificate.getNotBefore()) + " - " + formatter.format(x509Certificate.getNotAfter());


                        //certValidate.setText(validate);
//                                Intent it = new Intent(RegFirstStepActivity.this, MainActivity.class);
//                                //   Intent it = new Intent(RegFirstStepActivity.this, SignVerifyP1Activity.class);
//                                startActivity(it);
//                                finish();

                    }

                    @Override
                    public void doException(CmSdkException exception) {
                        Log.e("CertificateIssueService", "doException");
                        Toast.makeText(RegFirstStepActivity.this, "" + exception.getMessage(), Toast.LENGTH_SHORT).show();
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
                ToastUtil.showToast(RegFirstStepActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT);
                Log.e("firstRegister", "CmSdkException" + e.getMessage());
            }
        });
        try {

            certificateRegisterService.register(realname, idno, telephone, "1234", null);
        } catch (Exception e) {
            ToastUtil.showToast(RegFirstStepActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT);
            e.printStackTrace();
        }

    }


    private void initTestData() {
        edtAccount.setText("Velly");
        edtPassWord.setText("123456");
        realnameet.setText("李青");
        // idnumet.setText("320322199007171111");
        idnumet.setText("320322199007171428");
        telnumet.setText("15951882547");
        edtEmail.setText("2312565623@qq.com");
//        realnameet.setText("屠正松");
//        // idnumet.setText("320322199007171111");
//        idnumet.setText("320113199310156418");
//        telnumet.setText("15951882547");
    }

    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            getyzmbt.setClickable(false);
            getyzmbt.setText(l / 1000 + "秒后重新获取");

        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            getyzmbt.setText("重新获取");
            //设置可点击
            getyzmbt.setClickable(true);
        }
    }
}
