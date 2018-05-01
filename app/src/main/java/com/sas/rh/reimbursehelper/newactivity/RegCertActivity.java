package com.sas.rh.reimbursehelper.newactivity;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sas.rh.reimbursehelper.NetworkUtil.Utils;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtil;
import com.sas.rh.reimbursehelper.Util.ToastUtil;
import com.sas.rh.reimbursehelper.view.activity.BaseActivity;
import com.sas.rh.reimbursehelper.view.activity.RegFirstStepActivity;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Map;

import cn.com.syan.jcee.common.sdk.utils.CertificateConverter;
import cn.com.syan.online.sdk.OnlineApplyResponse;
import cn.com.syan.online.sdk.OnlineClient;
import cn.com.syan.online.sdk.OnlineIssueResponse;
import cn.unitid.spark.cm.sdk.business.CertificateIssueService;
import cn.unitid.spark.cm.sdk.business.CertificateRegisterService;
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
    private ProgressDialogUtil pdu = new ProgressDialogUtil(RegCertActivity.this, "提示", "正在处理中");


    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_cert;
    }

    @Override
    protected void initData() {
        tvTilte = (TextView) findViewById(R.id.tv_bar_title);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        edtName = (EditText) findViewById(R.id.edt_name);
        edtIdNo = (EditText) findViewById(R.id.edt_id_no);
        edtTel = (EditText) findViewById(R.id.edt_tel);
        tvTilte.setText("审批证书");
    }

    @Override
    protected void initListeners() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_reg_cert:
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
                        if (pdu.getMypDialog().isShowing()) {
                            pdu.dismisspd();
                        }
                        //  goLogin();

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
}
