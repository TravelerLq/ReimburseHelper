package com.sas.rh.reimbursehelper.newactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.CertUtil.CertServiceUrl;
import com.sas.rh.reimbursehelper.NetworkUtil.ExpenseCategoryUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.Utils;
import com.sas.rh.reimbursehelper.NetworkUtil.VerifyCertUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtil;
import com.sas.rh.reimbursehelper.Util.ToastUtil;
import com.sas.rh.reimbursehelper.view.activity.BaseActivity;
import com.sas.rh.reimbursehelper.view.activity.MainActivity;
import com.sas.rh.reimbursehelper.view.activity.RegFirstStepActivity;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import cn.com.syan.jcee.common.sdk.utils.CertificateConverter;
import cn.com.syan.online.sdk.OnlineApplyResponse;
import cn.com.syan.online.sdk.OnlineClient;
import cn.com.syan.online.sdk.OnlineIssueResponse;
import cn.com.syan.spark.client.sdk.SparkApplication;
import cn.unitid.spark.cm.sdk.business.CBSCertificateStore;
import cn.unitid.spark.cm.sdk.business.CertificateIssueService;
import cn.unitid.spark.cm.sdk.business.CertificateRegisterService;
import cn.unitid.spark.cm.sdk.data.entity.Certificate;
import cn.unitid.spark.cm.sdk.exception.CmSdkException;
import cn.unitid.spark.cm.sdk.listener.ProcessListener;

/**
 * Created by liqing on 18/5/1.
 */

public class RegCertActivity extends BaseActivity {
    private TextView tvTilte;
    private ImageView ivBack;
    private EditText edtName, edtTel, edtIdNo;
    private TextView tvRegister;
    private String edtNameStr;
    private String edtTelStr;
    private String edtIdNoStr;
    private OnlineClient onlineClient;
    private ArrayList<Certificate> certificateArrayList;//证书列表
    private ProgressDialogUtil pdu = new ProgressDialogUtil(RegCertActivity.this, "提示", "正在处理中");
    private String certStr;
    private SharedPreferencesUtil spu;
    private Context context;
    private int userId;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (pdu.getMypDialog().isShowing()) {
                    pdu.dismisspd();
                }
                int status = jsonresult.getIntValue("status");
                Intent intent = null;
                switch (status) {
                    case 200:
                        //直接登录
                        intent = new Intent(context, NewLoginActivity.class);
                        intent.putExtra("id", edtIdNoStr);
                        intent.putExtra("tel", edtTelStr);
                        //拿到的userId 覆盖原来的
                        userId = jsonresult.getIntValue("userId");
                        Loger.e("RegCert--userId" + userId);
                        spu.writeUserId(String.valueOf(userId));
                        spu.setTel(edtTelStr);
                        startActivity(intent);
                        finish();
                        break;
                    case 204:

                        int size = store.getAllCertificateList().size();
                        Loger.e("--certSize()=" + size);
                        if (size > 0) {
                            store.deleteCertificate(store.getAllCertificateList().get(0).getId());
                        }
                        Toast.makeText(context, "请重新申请证书", Toast.LENGTH_SHORT).show();
                        break;
                    case 205:
                        //去注册
                        intent = new Intent(context, NewRegisterActivity.class);
                        intent.putExtra("id", edtIdNoStr);
                        intent.putExtra("tel", edtTelStr);
                        toActivity(context, NewRegisterActivity.class);
                        spu.setTel(edtTelStr);
                        startActivity(intent);
                        finish();
                        break;
                    default:
                        break;
                }


            }
        }
    };
    private CBSCertificateStore store;

    private void verifyAgain() {
        if (certificateArrayList.size() > 0) {
            store.deleteCertificate(certificateArrayList.get(0).getId());
        }
        checkData();
    }

    private JSONObject jsonresult;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_cert;
    }

    @Override
    protected void initData() {
        context = RegCertActivity.this;
        tvTilte = (TextView) findViewById(R.id.tv_bar_title);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        edtName = (EditText) findViewById(R.id.edt_name);
        edtIdNo = (EditText) findViewById(R.id.edt_id_no);
        edtTel = (EditText) findViewById(R.id.edt_tel);
        tvRegister = (TextView) findViewById(R.id.tv_reg_cert);
        tvTilte.setText("审请证书");
        //初始Application
        SparkApplication.init(getApplication());
        //SparkApplication.init(RegCertActivity.getApplication());
        store = CBSCertificateStore.getInstance();
        //查询本地库证书列表,没注册则注册一张
        certificateArrayList = store.getAllCertificateList();

        //注册时，删除本地证书
//        if (certificateArrayList.size() > 0) {
//            store.deleteCertificate(certificateArrayList.get(0).getId());
//        }
        int size = store.getAllCertificateList().size();
        Loger.e("--certSize()=" + size);
        if (size > 0) {
            store.deleteCertificate(store.getAllCertificateList().get(0).getId());

        }
        spu = new SharedPreferencesUtil(context);
        userId = spu.getUidNum();
        onlineClient = new OnlineClient(CertServiceUrl.baseUrl, CertServiceUrl.appKey, CertServiceUrl.appSecret);

        initTestData1();
    }

    private void initTestData() {
        edtTel.setText("15951882547");
        edtName.setText("李青");
        edtIdNo.setText("320322199007171428");
    }

    private void initTestData1() {
        edtTel.setText("18205188981");
        edtName.setText("王朕");
        edtIdNo.setText("320113199407266410");
    }


    @Override
    protected void initListeners() {
        tvRegister.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_reg_cert:

                int size = store.getAllCertificateList().size();
                Loger.e("--certSize()=" + size);
                if (size > 0) {
                    store.deleteCertificate(store.getAllCertificateList().get(0).getId());
                }
                checkData();

                break;
            default:
                break;
        }
    }

    private void checkData() {
        edtNameStr = edtName.getText().toString();
        edtTelStr = edtTel.getText().toString();
        edtIdNoStr = edtIdNo.getText().toString();

        if (TextUtils.isEmpty(edtNameStr)) {
            Toast.makeText(this, "姓名不可为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edtTelStr)) {
            Toast.makeText(this, "电话不可为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edtIdNoStr)) {
            Toast.makeText(this, "身份证号码不可为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //申请证书
        goRegisterCertify();

    }


    private void goRegisterCertify() {
        pdu.showpd();

        CertificateRegisterService certificateRegisterService = new CertificateRegisterService(RegCertActivity.this,
                onlineClient, new ProcessListener<OnlineApplyResponse>() {
            @Override
            public void doFinish(OnlineApplyResponse onlineApplyResponse, String s) {
//                        issueId.setText(onlineApplyResponse.getApplyId());
//                        subject.setText(onlineApplyResponse.getSubject());
//                        algorithm.setText(onlineApplyResponse.getAlgorithm());
//                        extentionText.setText(onlineApplyResponse.getExtensions());
                //接口注册成功后执行
                CertificateIssueService certificateIssueService = new CertificateIssueService(RegCertActivity.this,
                        onlineClient, new ProcessListener<OnlineIssueResponse>() {
                    @Override
                    public void doFinish(OnlineIssueResponse data, String cert) {
                        Log.e("CertificateIssueService", "onFinish--start goLogin");
                        //  goAddUser();


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
                        Loger.e("validate--" + validate);

                        certStr = your_signature;
                        Loger.e("---cerStr= " + certStr);
                        if (TextUtils.isEmpty(certStr)) {
                            ToastUtil.showToast(context, "证书为空", Toast.LENGTH_SHORT);
                        } else {
                            goVerifyCert(certStr);
                        }

                    }

                    @Override
                    public void doException(CmSdkException exception) {
                        if (pdu.getMypDialog().isShowing()) {
                            pdu.dismisspd();
                        }
                        Log.e("CertificateIssueService", "doException");
                        Toast.makeText(RegCertActivity.this, "" + exception.getMessage(), Toast.LENGTH_SHORT).show();
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
                ToastUtil.showToast(RegCertActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT);
                Log.e("firstRegister", "CmSdkException" + e.getMessage());
            }
        });
        try {

            certificateRegisterService.register(edtNameStr, edtIdNoStr, edtTelStr, "1234", null);
        } catch (Exception e) {
            ToastUtil.showToast(RegCertActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT);
            e.printStackTrace();
        }

    }

    //

    private void goVerifyCert(String cert) {

        if (TextUtils.isEmpty(cert)) {
            ToastUtil.showToast(context, "证书为空", Toast.LENGTH_SHORT);
        } else {
            certStr = cert;
        }
        new Thread(VerifyRunnable).start();
    }

    Runnable VerifyRunnable = new Runnable() {
        @Override
        public void run() {

            try {
                JSONObject jsonObject = VerifyCertUtil.verifyCert(userId, certStr);
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
