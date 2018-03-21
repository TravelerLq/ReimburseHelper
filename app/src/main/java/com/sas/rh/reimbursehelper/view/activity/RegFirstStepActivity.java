package com.sas.rh.reimbursehelper.view.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sas.rh.reimbursehelper.CertUtil.CertServiceUrl;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.ToastUtil;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Map;

import cn.com.syan.jcee.common.sdk.utils.CertificateConverter;
import cn.com.syan.online.sdk.OnlineApplyResponse;
import cn.com.syan.online.sdk.OnlineClient;
import cn.com.syan.online.sdk.OnlineIssueResponse;
import cn.com.syan.online.sdk.OnlineResponse;
import cn.unitid.spark.cm.sdk.business.CertificateIssueService;
import cn.unitid.spark.cm.sdk.business.CertificateRegisterService;
import cn.unitid.spark.cm.sdk.business.VCodeGetService;
import cn.unitid.spark.cm.sdk.exception.CmSdkException;
import cn.unitid.spark.cm.sdk.listener.ProcessListener;

public class RegFirstStepActivity extends AppCompatActivity {

    private EditText realnameet, idnumet, telnumet, yzmet;
    private Button getyzmbt;
    private LinearLayout nexttostep2;
    private OnlineClient onlineClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_first_step);
        realnameet = (EditText) findViewById(R.id.realnameet);
        idnumet = (EditText) findViewById(R.id.idnumet);
        telnumet = (EditText) findViewById(R.id.telnumet);
        yzmet = (EditText) findViewById(R.id.yzmet);
        getyzmbt = (Button) findViewById(R.id.getyzmbt);
        nexttostep2 = (LinearLayout) findViewById(R.id.nexttostep2);
        onlineClient = new OnlineClient(CertServiceUrl.baseUrl, CertServiceUrl.appKey, CertServiceUrl.appSecret);

        final MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60000, 1000);
        initTestData();
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

        // 实名实名认证自动注册
        nexttostep2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String realname = realnameet.getText().toString().trim();
                String idno = idnumet.getText().toString().trim();
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

                String telephone = telnumet.getText().toString();
                if ("".equals(telephone)) {
                    ToastUtil.showToast(RegFirstStepActivity.this, "请输入手机号码", Toast.LENGTH_SHORT);
                    telnumet.requestFocus();
                    return;
                }
                String vcode = yzmet.getText().toString();
                if ("".equals(vcode)) {
                    ToastUtil.showToast(RegFirstStepActivity.this, "请输入验证码", Toast.LENGTH_SHORT);
                    yzmet.requestFocus();
                    return;
                }

                CertificateRegisterService certificateRegisterService = new CertificateRegisterService(RegFirstStepActivity.this, onlineClient, new ProcessListener<OnlineApplyResponse>() {
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
                                Log.e("CertificateIssueService","onFinish");
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
                                Intent it = new Intent(RegFirstStepActivity.this, MainActivity.class);
                                startActivity(it);
                                finish();

                            }

                            @Override
                            public void doException(CmSdkException exception) {
                                Log.e("CertificateIssueService","doException");
                                Toast.makeText(RegFirstStepActivity.this, "" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("firstRegister","showException"+ exception.getMessage());
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
                        Log.e("firstRegister","CmSdkException"+  e.getMessage());
                    }
                });
                try {
                    certificateRegisterService.register(realname, idno, telephone, vcode, null);
                } catch (Exception e) {
                    ToastUtil.showToast(RegFirstStepActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT);
                    e.printStackTrace();
                }

            }
        });

    }

    private void initTestData() {
        realnameet.setText("李青");
        // idnumet.setText("320322199007171111");
        idnumet.setText("320322199007171428");
        telnumet.setText("15951882547");
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
